package helpers

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"strconv"
	dto "szyszko-api/presentation/dto/common"
	"time"

	"errors"

	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"
	"golang.org/x/crypto/bcrypt"
)

type Claims struct {
	Username string `json:"username"`
	jwt.RegisteredClaims
}

func MustGetenv(key string) string {
	v, ok := os.LookupEnv(key)
	if !ok || v == "" {
		log.Fatalf("missing required env: %s", key)
	}
	return v
}

func TryBindDataQuery(c *gin.Context) (*dto.DataQuery, error) {

	var query dto.DataQuery
	if err := c.ShouldBindQuery(&query); err != nil {
		return nil, fmt.Errorf("Invalid query parameters: %v", err)
	}

	filterJSON := c.Query("filters")
	if filterJSON != "" {
		var filters []dto.Filter
		if err := json.Unmarshal([]byte(filterJSON), &filters); err != nil {
			return nil, fmt.Errorf("Invalid filters: %v", err)
		}
		query.Filters = filters
	}

	return &query, nil
}

const cost = bcrypt.DefaultCost

func HashPassword(password string) (string, error) {
	hashedBytes, err := bcrypt.GenerateFromPassword([]byte(password), cost)
	if err != nil {
		return "", err
	}
	return string(hashedBytes), nil
}

func CheckPassword(password string, hashed string) error {
	err := bcrypt.CompareHashAndPassword([]byte(hashed), []byte(password))
	if err != nil {
		return errors.New("wrong password")
	}
	return nil
}

var (
	JwtKey            []byte
	expirationSeconds int64
)

func GenerateToken(username string) (string, int64, error) {
	expirationTime := time.Now().Add(time.Duration(expirationSeconds) * time.Second)

	claims := &Claims{
		Username: username,
		RegisteredClaims: jwt.RegisteredClaims{
			ExpiresAt: jwt.NewNumericDate(expirationTime),
		},
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	tokenString, err := token.SignedString(JwtKey)
	if err != nil {
		return "", 0, err
	}

	return tokenString, expirationSeconds, nil
}

func InitJWTConfig() {
	secondsStr := MustGetenv("TOKEN_EXPIRATION_SECONDS")
	key := MustGetenv("TOKEN_KEY")

	JwtKey = []byte(key)

	s, err := strconv.ParseInt(secondsStr, 10, 64)
	if err != nil {
		log.Fatalf("Nieprawidłowa liczba sekund: %v", err)
	}
	expirationSeconds = s
}

func sendLogToDiscord(message string, mentionRoleID string) {
	webhookURL := MustGetenv("DISCORD_WEBHOOK_URL")

	content := message
	if mentionRoleID != "" {
		content = "<@&" + mentionRoleID + ">\n" + content
	}

	payload := map[string]string{"content": content}
	jsonData, _ := json.Marshal(payload)

	http.Post(webhookURL, "application/json", bytes.NewBuffer(jsonData))
}

type responseBodyWriter struct {
	gin.ResponseWriter
	body *bytes.Buffer
}

func (w *responseBodyWriter) Write(b []byte) (int, error) {
	w.body.Write(b)
	return w.ResponseWriter.Write(b)
}

func DiscordLogger() gin.HandlerFunc {
	roleId := MustGetenv("DISCORD_METION_ROLE_ID")

	return func(c *gin.Context) {
		var reqBody []byte
		if c.Request.Body != nil {
			reqBody, _ = io.ReadAll(c.Request.Body)
			c.Request.Body = io.NopCloser(bytes.NewBuffer(reqBody))
		}

		respBody := &bytes.Buffer{}
		writer := &responseBodyWriter{body: respBody, ResponseWriter: c.Writer}
		c.Writer = writer

		c.Next()

		status := c.Writer.Status()
		method := c.Request.Method
		path := c.Request.URL.RequestURI()
		ip := c.ClientIP()

		if status >= 500 {
			logMsg := fmt.Sprintf("🚨 **[500 Error]**\n```http\n%s %s\nStatus: %d %s\nIP: %s\n\n[Request body]:\n%s\n\n[Response body]:\n%s\n```",
				method, path, status, http.StatusText(status), ip, string(reqBody), respBody.String())

			sendLogToDiscord(logMsg, roleId)
		}
	}
}
