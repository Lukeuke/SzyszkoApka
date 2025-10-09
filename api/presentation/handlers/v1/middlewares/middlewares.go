package middlewares

import (
	"log"
	"net/http"
	"strings"
	helpers "szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"

	dto "szyszko-api/presentation/dto/common"

	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"
)

func AuthMiddleware(uow *repository.UnitOfWork) gin.HandlerFunc {
	return func(c *gin.Context) {
		authHeader := c.GetHeader("Authorization")
		if authHeader == "" {
			c.JSON(http.StatusUnauthorized, dto.ErrorResult[string]("Missing bearer token header"))
			c.Abort()
			return
		}

		tokenStr := authHeader[len("Bearer "):]

		claims := &helpers.Claims{}
		token, err := jwt.ParseWithClaims(tokenStr, claims, func(token *jwt.Token) (interface{}, error) {
			return helpers.JwtKey, nil
		})

		if err != nil || !token.Valid {
			c.JSON(http.StatusUnauthorized, dto.ErrorResult[string]("Bearer token is expired"))
			c.Abort()
			return
		}

		isEnabled, err := uow.IdentityRepo.IsEnabled(claims.Username)

		if err != nil {
			log.Printf("IsEnabled error: %v", err)
			c.Status(500)
			c.Abort()
			return
		}

		if !isEnabled {
			c.JSON(403, dto.ErrorResult[string]("User is disabled"))
			c.Abort()
			return
		}

		c.Set("user", claims.Username)
		c.Next()
	}
}

func UnAuthorizedCache() gin.HandlerFunc {
	return func(c *gin.Context) {

		if c.Request.Method != http.MethodGet {
			c.Next()
			return
		}

		authHeader := c.GetHeader("Authorization")

		if authHeader == "" || !strings.HasPrefix(authHeader, "Bearer ") {
			c.Header("Cache-Control", "public, max-age=300, stale-while-revalidate=3600")
			c.Next()
			return
		}

		tokenStr := strings.TrimPrefix(authHeader, "Bearer ")
		claims := &helpers.Claims{}

		token, err := jwt.ParseWithClaims(tokenStr, claims, func(token *jwt.Token) (interface{}, error) {
			return helpers.JwtKey, nil
		})

		if err != nil || !token.Valid {
			c.Header("Cache-Control", "public, max-age=300, stale-while-revalidate=3600")
		} else {
			c.Set("claims", claims)
		}

		c.Next()
	}
}
