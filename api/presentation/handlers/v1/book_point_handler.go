package v1

import (
	"fmt"
	"log"
	"net/http"
	"szyszko-api/domain"
	repository "szyszko-api/infrastructure/repositories"

	helpers "szyszko-api/application/helpers"

	dto "szyszko-api/presentation/dto/book_point"

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
	bookPoints.POST("/", handler.insertNewBookPoint)
	bookPoints.DELETE("/:id", handler.deleteBookPoint)        // TODO: Role: Admin
	bookPoints.POST("/approve/:id", handler.approveBookPoint) // TODO: Role: Admin
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

func (h *BookPointHandler) insertNewBookPoint(c *gin.Context) {
	var cmd dto.CreateBookPointCommand

	if err := c.ShouldBindJSON(&cmd); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	dao := domain.BookPoint{
		Description: cmd.Description,
		Lat:         cmd.Lat,
		Lon:         cmd.Lon,
	}

	id, err := h.uow.BookPointRepo.Insert(c.Request.Context(), &dao)
	if err != nil {
		log.Printf("insertNewBookPoint error: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.Header("Location", fmt.Sprintf("/book-points/%s", id.String()))
	c.Status(201)
}

func (h *BookPointHandler) approveBookPoint(c *gin.Context) {
	idParam := c.Param("id")
	id, err := uuid.Parse(idParam)

	if err != nil {
		log.Printf("Błąd parsowania UUID: %v\n", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	err = h.uow.BookPointRepo.Approve(c.Request.Context(), id)
	if err != nil {
		log.Printf("approveBookPoint error: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.Status(204)
}

func (h *BookPointHandler) deleteBookPoint(c *gin.Context) {
	idParam := c.Param("id")
	id, err := uuid.Parse(idParam)

	if err != nil {
		log.Printf("Błąd parsowania UUID: %v\n", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	err = h.uow.BookPointRepo.Remove(c.Request.Context(), id)
	if err != nil {
		log.Printf("deleteBookPoint error: %v", err)
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.Status(204)
}
