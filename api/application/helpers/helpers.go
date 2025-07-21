package helpers

import (
	"encoding/json"
	"fmt"
	"log"
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
