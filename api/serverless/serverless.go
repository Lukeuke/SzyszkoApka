package handler

import (
	"net/http"
	"szyszko-api/application"
	"szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"
	handlers "szyszko-api/presentation/handlers"

	"github.com/joho/godotenv"
)

var router http.Handler

func init() {
	_ = godotenv.Load()

	url := helpers.MustGetenv("SUPABASE_URL")
	key := helpers.MustGetenv("SUPABASE_KEY")

	application.InitSupabaseClient(url, key)

	uow := repository.NewUnitOfWork(application.SupabaseClient)

	router = handlers.NewRouter(uow)
}

func Handler(w http.ResponseWriter, r *http.Request) {
	router.ServeHTTP(w, r)
}
