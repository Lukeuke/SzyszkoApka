package v1

import (
	"log"
	"net/http"
	repository "szyszko-api/infrastructure/repositories"

	helpers "szyszko-api/application/helpers"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

type BookPointHandler struct {
	uow *repository.UnitOfWork
}

func NewBookPointHandler(uow *repository.UnitOfWork) *BookPointHandler {
	return &BookPointHandler{
		uow: uow,
	}
}

func RegisterBookPoints(group *gin.RouterGroup, uow *repository.UnitOfWork) {
	handler := NewBookPointHandler(uow)

	bookPoints := group.Group("/book-points")

	bookPoints.GET("/", handler.getAllBookPoints)
	bookPoints.GET("/:id", handler.getBookPointByID)
}

func (h *BookPointHandler) getAllBookPoints(c *gin.Context) {
	query, err := helpers.TryBindDataQuery(c)

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	result, err := h.uow.BookPointRepo.GetAll(c.Request.Context(), query)
	if err != nil {
		log.Printf("GetAll error: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.Header("Cache-Control", "public, max-age=300, stale-while-revalidate=3600")
	c.JSON(http.StatusOK, result)
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

	c.Header("Cache-Control", "public, max-age=300, stale-while-revalidate=3600")
	c.JSON(http.StatusOK, point)
}
