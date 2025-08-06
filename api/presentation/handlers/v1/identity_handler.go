package v1

import (
	"net/http"
	"szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"
	results "szyszko-api/presentation/dto/common"
	dto "szyszko-api/presentation/dto/identity"

	"szyszko-api/presentation/handlers/v1/middlewares"

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
	identity.POST("/password-change", middlewares.AuthMiddleware(uow), handler.changePassword) // Authorized
}

func (h *IdentityHandler) login(c *gin.Context) {
	var cmd dto.LoginCommand

	if err := c.ShouldBindJSON(&cmd); err != nil {
		c.JSON(http.StatusBadRequest, results.ErrorResult[any](err))
		return
	}

	identity, err := h.uow.IdentityRepo.GetByUsername(c.Request.Context(), cmd.Username)

	if err != nil {
		c.JSON(http.StatusBadRequest, results.ErrorResult[any](err))
		return
	}

	if identity == nil {
		c.JSON(http.StatusNotFound, results.ErrorResult[string]("Identity not found"))
		return
	}

	if helpers.CheckPassword(cmd.Password, identity.PasswordHash) != nil {
		c.JSON(http.StatusBadRequest, results.ErrorResult[string]("Wrong username or password"))
		return
	}

	token, expirationSeconds, err := helpers.GenerateToken(identity.Username)

	if err != nil {
		c.JSON(http.StatusNotFound, results.ErrorResult[string]("Failed generating token. See logs for more info"))
	}

	c.JSON(http.StatusOK, gin.H{
		"token":      token,
		"expires_in": expirationSeconds,
	})
}

func (h *IdentityHandler) changePassword(c *gin.Context) {
	var cmd dto.ChangePasswordCommand

	if err := c.ShouldBindJSON(&cmd); err != nil {
		c.JSON(http.StatusBadRequest, results.ErrorResult[any](err))
		return
	}

	usernameIface, exists := c.Get("user")
	if !exists {
		c.Status(201)
		return
	}

	username, ok := usernameIface.(string)
	if !ok {
		c.JSON(http.StatusInternalServerError, results.ErrorResult[string]("Internal error: invalid user context"))
		return
	}

	identity, err := h.uow.IdentityRepo.GetByUsername(c.Request.Context(), username)
	if err != nil {
		c.JSON(http.StatusInternalServerError, results.ErrorResult[string]("Server error"))
		return
	}

	if identity == nil {
		c.JSON(http.StatusNotFound, results.ErrorResult[string]("User does not exist"))
		return
	}

	if err := helpers.CheckPassword(cmd.OldPassword, identity.PasswordHash); err != nil {
		c.JSON(http.StatusUnauthorized, results.ErrorResult[string]("Wrong old password"))
		return
	}

	newHashed, err := helpers.HashPassword(cmd.NewPassword)
	if err != nil {
		c.JSON(http.StatusInternalServerError, results.ErrorResult[string]("Error while hashing new password"))
		return
	}

	if err := h.uow.IdentityRepo.UpdatePassword(c.Request.Context(), username, newHashed); err != nil {
		c.JSON(http.StatusInternalServerError, results.ErrorResult[string]("Error while updating password"))
		return
	}

	c.Status(http.StatusNoContent)
}
