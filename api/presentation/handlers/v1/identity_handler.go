package v1

import (
	"log"
	"net/http"
	"szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"
	dto "szyszko-api/presentation/dto/identity"

	"github.com/gin-gonic/gin"
)

type IdentityHandler struct {
	uow *repository.UnitOfWork
}

func NewIdentityHandler(uow *repository.UnitOfWork) *IdentityHandler {
	return &IdentityHandler{
		uow: uow,
	}
}

func RegisterIdentity(group *gin.RouterGroup, uow *repository.UnitOfWork) {
	handler := NewIdentityHandler(uow)
	identity := group.Group("/identity")

	identity.POST("/", handler.login)
}

func (h *IdentityHandler) login(c *gin.Context) {
	var cmd dto.LoginCommand

	// Bind the incoming JSON to the cmd struct
	if err := c.ShouldBindJSON(&cmd); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	identity, err := h.uow.IdentityRepo.GetByUsername(c.Request.Context(), cmd.Username)

	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}

	if identity == nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "identity not found"})
		return
	}

	log.Printf("Received password: %s", cmd.Password)
	log.Printf("Stored password hash: %s", identity.PasswordHash)

	if helpers.CheckPassword(cmd.Password, identity.PasswordHash) != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "Wrong username or password"})
		return
	}

	c.JSON(http.StatusOK, "Logged in")
}
