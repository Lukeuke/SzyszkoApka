package handlers

import (
	"net/http"
	"strings"
	"szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"
	v1 "szyszko-api/presentation/handlers/v1"

	"github.com/gin-gonic/gin"
)

func NewRouter(uow *repository.UnitOfWork) *gin.Engine {
	r := gin.Default()

	r.Use(func(c *gin.Context) {
		xAppId := c.GetHeader("X-App-ID")
		host := c.Request.Host

		appId := helpers.MustGetenv("APP_ID")

		if strings.HasPrefix(host, "localhost") || strings.HasPrefix(host, "127.0.0.1") || strings.HasPrefix(host, "[::1]") || xAppId == appId {
			c.Next()
		} else {
			c.JSON(http.StatusForbidden, gin.H{
				"message": "Access denied",
			})
			c.Abort()
			return
		}
	})

	v1Group := r.Group("/api/v1")
	{
		v1.RegisterBookPoints(v1Group, uow)
		v1.RegisterIdentity(v1Group, uow)
	}

	return r
}
