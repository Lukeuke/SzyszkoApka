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

var router http.Handler

func init() {
	log.Println("Initializing...")

	if os.Getenv("VERCEL_ENV") != "production" {
		_ = godotenv.Load()
	}

	log.Println("Loading config...")
	helpers.InitJWTConfig()

	url := helpers.MustGetenv("SUPABASE_URL")
	key := helpers.MustGetenv("SUPABASE_KEY")

	log.Println("Initializing supabase client...")
	application.InitSupabaseClient(url, key)

	uow := repository.NewUnitOfWork(application.SupabaseClient)

	router = handlers.NewRouter(uow)

	log.Println("Initialied.")
}

func Handler(w http.ResponseWriter, r *http.Request) {
	router.ServeHTTP(w, r)
}
