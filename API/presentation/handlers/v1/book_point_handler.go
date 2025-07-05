package v1

import (
	"log"
	"net/http"
	repository "szyszko-api/infrastructure/repositories"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

type BookPointHandler struct {
	uow *repository.UnitOfWork
}

func RegisterBookPoints(group *gin.RouterGroup, uow *repository.UnitOfWork) {
	handler := &BookPointHandler{uow: uow}

	bookPoints := group.Group("/book_points")

	bookPoints.GET("/", handler.getAllBookPoints)
	bookPoints.GET("/:id", handler.getBookPointByID)
}

func (h *BookPointHandler) getAllBookPoints(c *gin.Context) {
	points, err := h.uow.BookPointRepo.GetAll(c.Request.Context())
	if err != nil {
		log.Printf("GetAll error: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, points)
}

func (h *BookPointHandler) getBookPointByID(c *gin.Context) {
	idParam := c.Param("id")
	id, err := uuid.Parse(idParam)

	if err != nil {
		log.Printf("Błąd parsowania UUID: %v\n", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	point, err := h.uow.BookPointRepo.GetByID(c.Request.Context(), id)
	if err != nil {
		log.Printf("GetByID error: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, point)
}
