package http

import (
	"github.com/gin-gonic/gin"
)

func NewRouter() *gin.Engine {
	r := gin.Default()

	// v1Group := r.Group("/v1")
	// {
	//     v1.RegisterBookPoints(v1Group)
	// }

	return r
}
