package v1

import (
	"fmt"
	"log"
	"net/http"
	"szyszko-api/domain"
	repository "szyszko-api/infrastructure/repositories"
	"time"

	helpers "szyszko-api/application/helpers"

	dto "szyszko-api/presentation/dto/book_point"
	results "szyszko-api/presentation/dto/common"
	"szyszko-api/presentation/handlers/v1/middlewares"

	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	"golang.org/x/time/rate"
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
	rateLimiter := middlewares.NewRateLimiter(rate.Every(8*time.Hour), 3)

	bookPoints := group.Group("/book-points")

	bookPoints.GET("/", middlewares.UnAuthorizedCache(), handler.getAllBookPoints)
	bookPoints.GET("/:id", middlewares.UnAuthorizedCache(), handler.getBookPointByID)
	bookPoints.POST("/", middlewares.UnAuthorizedRateLimit(uow, rateLimiter), handler.insertNewBookPoint)
	bookPoints.PUT("/:id", middlewares.AuthMiddleware(uow), handler.editBookPoint)             // Authorized only
	bookPoints.DELETE("/:id", middlewares.AuthMiddleware(uow), handler.deleteBookPoint)        // Authorized only
	bookPoints.POST("/approve/:id", middlewares.AuthMiddleware(uow), handler.approveBookPoint) // Authorized only
}

func (h *BookPointHandler) getAllBookPoints(c *gin.Context) {
	query, err := helpers.TryBindDataQuery(c)

	if err != nil {
		c.JSON(http.StatusBadRequest, results.ErrorResult[error](err))
		return
	}

	result, err := h.uow.BookPointRepo.GetAll(c.Request.Context(), query)
	if err != nil {
		log.Printf("GetAll error: %v", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	c.JSON(http.StatusOK, result)
}

func (h *BookPointHandler) getBookPointByID(c *gin.Context) {
	idParam := c.Param("id")
	id, err := uuid.Parse(idParam)

	if err != nil {
		log.Printf("Błąd parsowania UUID: %v\n", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	point, err := h.uow.BookPointRepo.GetByID(c.Request.Context(), id)
	if err != nil {
		log.Printf("GetByID error: %v", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	c.JSON(http.StatusOK, point)
}

func (h *BookPointHandler) insertNewBookPoint(c *gin.Context) {
	var cmd dto.CreateBookPointCommand

	if err := c.ShouldBindJSON(&cmd); err != nil {
		c.JSON(http.StatusBadRequest, results.ErrorResult[error](err))
		return
	}

	dao := domain.BookPoint{
		Title:       cmd.Title,
		Description: cmd.Description,
		Lat:         cmd.Lat,
		Lon:         cmd.Lon,
	}

	id, err := h.uow.BookPointRepo.Insert(c.Request.Context(), &dao)
	if err != nil {
		log.Printf("insertNewBookPoint error: %v", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	c.Header("Location", fmt.Sprintf("/book-points/%s", id.String()))
	c.Status(201)
}

func (h *BookPointHandler) editBookPoint(c *gin.Context) {
	var cmd dto.EditBookPointCommand

	if err := c.ShouldBindJSON(&cmd); err != nil {
		c.JSON(http.StatusBadRequest, results.ErrorResult[error](err))
		return
	}

	idParam := c.Param("id")
	id, err := uuid.Parse(idParam)

	if err != nil {
		log.Printf("Błąd parsowania UUID: %v\n", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	point, err := h.uow.BookPointRepo.GetByID(c.Request.Context(), id)
	if err != nil {
		log.Printf("GetByID error: %v", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	log.Printf("Editing book point %v\n", id)

	point.Approved = cmd.Approved
	point.Title = cmd.Title
	point.Description = cmd.Description
	point.Lat = cmd.Lat
	point.Lon = cmd.Lon

	_, err = h.uow.BookPointRepo.Edit(c.Request.Context(), point)
	if err != nil {
		log.Printf("Edit error: %v", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	c.Header("Cache-Control", "no-cache, no-store, must-revalidate")
	c.JSON(http.StatusOK, point)
}

func (h *BookPointHandler) approveBookPoint(c *gin.Context) {
	idParam := c.Param("id")
	id, err := uuid.Parse(idParam)

	if err != nil {
		log.Printf("Błąd parsowania UUID: %v\n", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	err = h.uow.BookPointRepo.Approve(c.Request.Context(), id)
	if err != nil {
		log.Printf("approveBookPoint error: %v", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	c.Status(204)
}

func (h *BookPointHandler) deleteBookPoint(c *gin.Context) {
	idParam := c.Param("id")
	id, err := uuid.Parse(idParam)

	if err != nil {
		log.Printf("Błąd parsowania UUID: %v\n", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	err = h.uow.BookPointRepo.Remove(c.Request.Context(), id)
	if err != nil {
		log.Printf("deleteBookPoint error: %v", err)
		c.JSON(http.StatusInternalServerError, results.ErrorResult[error](err))
		return
	}

	c.Status(204)
}
