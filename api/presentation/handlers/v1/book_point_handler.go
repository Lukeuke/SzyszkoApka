package v1

import (
	"fmt"
	"log"
	"net/http"
	repository "szyszko-api/infrastructure/repositories"
	"time"

	helpers "szyszko-api/application/helpers"
	cache "szyszko-api/application/services"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
)

type BookPointHandler struct {
	uow   *repository.UnitOfWork
	cache *cache.Cache
}

func NewBookPointHandler(uow *repository.UnitOfWork) *BookPointHandler {
	return &BookPointHandler{
		uow:   uow,
		cache: cache.NewCache(5 * time.Minute),
	}
}

func RegisterBookPoints(group *gin.RouterGroup, uow *repository.UnitOfWork) {
	handler := NewBookPointHandler(uow)

	bookPoints := group.Group("/book-points")

	bookPoints.GET("/", handler.getAllBookPoints)
	bookPoints.GET("/:id", handler.getBookPointByID)
}

func (h *BookPointHandler) getAllBookPoints(c *gin.Context) {
	// cacheKey := "all_book_points"

	// if data, exists := h.cache.Get(cacheKey); exists {
	// 	c.JSON(http.StatusOK, data)
	// 	return
	// }

	query, err := helpers.TryBindDataQuery(c)

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	fmt.Printf("%v", query)

	result, err := h.uow.BookPointRepo.GetAll(c.Request.Context(), query)
	if err != nil {
		log.Printf("GetAll error: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	// h.cache.Set(cacheKey, result)

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

	cacheKey := "book_point_" + id.String()

	if data, exists := h.cache.Get(cacheKey); exists {
		c.JSON(http.StatusOK, data)
		return
	}

	point, err := h.uow.BookPointRepo.GetByID(c.Request.Context(), id)
	if err != nil {
		log.Printf("GetByID error: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	h.cache.Set(cacheKey, point)

	c.JSON(http.StatusOK, point)
}
