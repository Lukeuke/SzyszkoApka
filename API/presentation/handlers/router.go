package handlers

import (
	repository "szyszko-api/infrastructure/repositories"
	v1 "szyszko-api/presentation/handlers/v1"

	"github.com/gin-gonic/gin"
)

func NewRouter(uow *repository.UnitOfWork) *gin.Engine {
	r := gin.Default()

	v1Group := r.Group("/v1")
	{
		v1.RegisterBookPoints(v1Group, uow)
	}

	return r
}
