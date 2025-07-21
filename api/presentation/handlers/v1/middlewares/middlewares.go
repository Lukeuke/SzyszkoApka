package middlewares

import (
	"log"
	"net/http"
	helpers "szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"

	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"
)

func AuthMiddleware(uow *repository.UnitOfWork) gin.HandlerFunc {
	return func(c *gin.Context) {
		authHeader := c.GetHeader("Authorization")
		if authHeader == "" {
			c.JSON(http.StatusUnauthorized, gin.H{"error": "Missing bearer token header"})
			c.Abort()
			return
		}

		tokenStr := authHeader[len("Bearer "):]

		claims := &helpers.Claims{}
		token, err := jwt.ParseWithClaims(tokenStr, claims, func(token *jwt.Token) (interface{}, error) {
			return helpers.JwtKey, nil
		})

		if err != nil || !token.Valid {
			c.Status(201)
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
			c.JSON(403, gin.H{"error": "User is disabled"})
			c.Abort()
			return
		}

		c.Set("user", claims.Username)
		c.Next()
	}
}
