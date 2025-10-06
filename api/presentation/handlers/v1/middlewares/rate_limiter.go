package middlewares

import (
	"log"
	"net/http"
	"sync"
	helpers "szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"
	dto "szyszko-api/presentation/dto/common"

	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"
	"golang.org/x/time/rate"
)

type RateLimiter struct {
	limiters map[string]*rate.Limiter
	mu       sync.Mutex
	rate     rate.Limit
	burst    int
}

func NewRateLimiter(r rate.Limit, b int) *RateLimiter {
	return &RateLimiter{
		limiters: make(map[string]*rate.Limiter),
		rate:     r,
		burst:    b,
	}
}

func (r *RateLimiter) GetLimiter(ip string) *rate.Limiter {
	r.mu.Lock()
	defer r.mu.Unlock()

	limiter, exists := r.limiters[ip]
	if !exists {
		limiter = rate.NewLimiter(r.rate, r.burst)
		r.limiters[ip] = limiter
	}
	return limiter
}

func UnAuthorizedRateLimit(uow *repository.UnitOfWork, rl *RateLimiter) gin.HandlerFunc {
	return func(c *gin.Context) {
		authHeader := c.GetHeader("Authorization")

		if authHeader == "" {
			limitUnauthorized(c, rl)
			return
		}

		tokenStr := authHeader[len("Bearer "):]
		claims := &helpers.Claims{}

		token, err := jwt.ParseWithClaims(tokenStr, claims, func(token *jwt.Token) (interface{}, error) {
			return helpers.JwtKey, nil
		})

		if err != nil || !token.Valid {
			limitUnauthorized(c, rl)
			return
		}

		isEnabled, err := uow.IdentityRepo.IsEnabled(claims.Username)
		if err != nil {
			log.Printf("IsEnabled error: %v", err)
			c.Status(http.StatusInternalServerError)
			c.Abort()
			return
		}

		if !isEnabled {
			limitUnauthorized(c, rl)
			return
		}

		c.Next()
	}
}

func limitUnauthorized(c *gin.Context, rl *RateLimiter) {
	ip := c.ClientIP()
	limiter := rl.GetLimiter(ip)

	if !limiter.Allow() {
		c.JSON(http.StatusTooManyRequests, dto.ErrorResult[string]("Unauthorized rate limit hit"))
		c.Abort()
		return
	}

	c.Next()
}
