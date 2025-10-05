package handler

import (
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
	if os.Getenv("VERCEL_ENV") != "production" {
		_ = godotenv.Load()
	}
	helpers.InitJWTConfig()

	url := helpers.MustGetenv("SUPABASE_URL")
	key := helpers.MustGetenv("SUPABASE_KEY")

	application.InitSupabaseClient(url, key)

	uow := repository.NewUnitOfWork(application.SupabaseClient)

	router = handlers.NewRouter(uow)
}

func Handler(w http.ResponseWriter, r *http.Request) {
	router.ServeHTTP(w, r)
}
