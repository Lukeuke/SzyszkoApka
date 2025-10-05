package handler

import (
	"log"
	"net/http"
	"os"
	"szyszko-api/application"
	"szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"
	handlers "szyszko-api/presentation/handlers"

	"github.com/joho/godotenv"
)

var (
	router http.Handler
	once   sync.Once
)

func init() {
	log.Println("Initializing...")

	if os.Getenv("VERCEL_ENV") != "production" {
		_ = godotenv.Load()
	}

	log.Println("Loading config...")
	helpers.InitJWTConfig()

		if os.Getenv("VERCEL_ENV") != "production" {
			_ = godotenv.Load()
		}

	log.Println("Initializing supabase client...")
	application.InitSupabaseClient(url, key)

		url := helpers.MustGetenv("SUPABASE_URL")
		key := helpers.MustGetenv("SUPABASE_KEY")

	router = handlers.NewRouter(uow)

	log.Println("Initialied.")
}

	router.ServeHTTP(w, r)
	log.Println("Serving HTTP")
}
