package helpers

import (
	"encoding/json"
	"fmt"
	"log"
	"os"
	dto "szyszko-api/presentation/dto/common"

	"errors"

	"github.com/gin-gonic/gin"
	"golang.org/x/crypto/bcrypt"
)

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
