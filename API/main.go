package main

import (
	"szyszko-api/application"
	"szyszko-api/application/helpers"
	repository "szyszko-api/infrastructure/repositories"
	handlers "szyszko-api/presentation/handlers"

	"github.com/joho/godotenv"
)

func main() {
	_ = godotenv.Load()

	url := helpers.MustGetenv("SUPABASE_URL")
	key := helpers.MustGetenv("SUPABASE_KEY")

	application.InitSupabaseClient(url, key)

	uow := repository.NewUnitOfWork(application.SupabaseClient)

	r := handlers.NewRouter(uow)

	r.Run()
}
