package middlewares

import (
	"net/http"
	"strings"
	"sync"
	helpers "szyszko-api/application/helpers"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/golang-jwt/jwt/v5"
)

type ipEntry struct {
	Count     int
	FirstSeen time.Time
}

type InMemoryRateLimiter struct {
	mu       sync.Mutex
	limiters map[string]*ipEntry
	limit    int
	window   time.Duration
}

func NewInMemoryRateLimiter(limit int, window time.Duration) *InMemoryRateLimiter {
	rl := &InMemoryRateLimiter{
		limiters: make(map[string]*ipEntry),
		limit:    limit,
		window:   window,
	}

	go rl.cleanup()
	return rl
}

func (r *InMemoryRateLimiter) UnAuthorizedRateLimit() gin.HandlerFunc {
	return func(c *gin.Context) {
		authHeader := c.GetHeader("Authorization")
		tokenStr := strings.TrimPrefix(authHeader, "Bearer ")
		claims := &helpers.Claims{}

		token, err := jwt.ParseWithClaims(tokenStr, claims, func(token *jwt.Token) (interface{}, error) {
			return helpers.JwtKey, nil
		})

		if err == nil && token != nil && token.Valid {
			c.Next()
			return
		}

		ip := c.ClientIP()
		now := time.Now()

		r.mu.Lock()
		entry, exists := r.limiters[ip]

		if !exists || now.Sub(entry.FirstSeen) > r.window {
			r.limiters[ip] = &ipEntry{Count: 1, FirstSeen: now}
			r.mu.Unlock()
			c.Next()
			return
		}

		if entry.Count >= r.limit {
			r.mu.Unlock()
			retryIn := entry.FirstSeen.Add(r.window).Sub(now)
			c.Header("Retry-After", retryIn.String())
			c.AbortWithStatusJSON(http.StatusTooManyRequests, gin.H{
				"error": "Rate limit exceeded for unauthenticated user",
			})
			return
		}

		entry.Count++
		r.mu.Unlock()
		c.Next()
	}
}

func (r *InMemoryRateLimiter) cleanup() {
	for {
		time.Sleep(time.Hour)
		r.mu.Lock()
		now := time.Now()
		for ip, entry := range r.limiters {
			if now.Sub(entry.FirstSeen) > r.window {
				delete(r.limiters, ip)
			}
		}
		r.mu.Unlock()
	}
}
