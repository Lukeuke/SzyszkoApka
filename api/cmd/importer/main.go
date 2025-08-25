package main

import (
	"context"
	"encoding/json"
	"log"
	"os"
	"szyszko-api/application"
	"szyszko-api/application/helpers"
	"szyszko-api/domain"
	repository "szyszko-api/infrastructure/repositories"

	"github.com/joho/godotenv"
)

func main() {
	_ = godotenv.Load("../../.env")

	helpers.InitJWTConfig()

	url := helpers.MustGetenv("SUPABASE_URL")
	key := helpers.MustGetenv("SUPABASE_KEY")

	biblioteczki, _ := os.ReadFile("biblioteczki.json")
	var data []domain.BookPoint
	err := json.Unmarshal(biblioteczki, &data)

	if err != nil {
		log.Printf("Error reading file: %v\n", err)
		return
	}

	application.InitSupabaseClient(url, key)

	uow := repository.NewUnitOfWork(application.SupabaseClient)

	ctx := context.Background()

	log.Printf("Found %v book points to import\n", len(data))

	for i, curr := range data {

		exists, err := uow.BookPointRepo.ExistsByExternalKey(ctx, curr.ExternalKey)

		if err != nil {
			log.Printf("Failed to execute ExistsByExternalKey: %v\n", err)
			continue
		}

		if exists {
			log.Printf("Exists, skipping: %s\n", curr.ExternalKey)
			continue
		}

		_, err = uow.BookPointRepo.Insert(ctx, &curr)
		if err != nil {
			log.Printf("Failed to insert BookPoint (Title: %s): %v", curr.Title, err)
			continue
		}

		log.Printf("Inserted: %s (%d/%d)", curr.Title, i+1, len(data))
	}
}
