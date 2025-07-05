package main

import (
	"log"
	"net/http"

	"szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"
	handlers "szyszko-api/presentation/handlers"

	"github.com/gin-gonic/gin"
	"github.com/joho/godotenv"
)

func main() {
	_ = godotenv.Load()

	url := helpers.MustGetenv("SUPABASE_URL")
	key := helpers.MustGetenv("SUPABASE_KEY")

	r := handlers.NewRouter()

	bookPointRepo := repository.NewSupabaseBookPointRepository(url, key)

	r.GET("/book_points", func(c *gin.Context) {
		points, err := bookPointRepo.GetAll(c.Request.Context())
		if err != nil {
			log.Printf("GetAll error: %v", err)
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusOK, points)
	})

	r.Run()
}
