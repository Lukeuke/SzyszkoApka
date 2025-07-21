package middlewares

import (
	"net/http"
	helpers "szyszko-api/application/helpers"

	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"
)

func AuthMiddleware() gin.HandlerFunc {
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

		c.Set("user", claims.Username)
		c.Next()
	}
}
