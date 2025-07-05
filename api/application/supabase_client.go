package application

import (
	"log"

	supabase "github.com/supabase-community/supabase-go"
)

var SupabaseClient *supabase.Client

func InitSupabaseClient(url string, key string) {
	client, err := supabase.NewClient(url, key, &supabase.ClientOptions{})
	if err != nil {
		log.Fatalf("failed to initialize supabase client: %v", err)
	}
	SupabaseClient = client
}
