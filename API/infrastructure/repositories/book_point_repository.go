package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"log"

	"szyszko-api/domain"

	"github.com/google/uuid"
	supabase "github.com/supabase-community/supabase-go"
)

type BookPointRepository interface {
	Insert(ctx context.Context, bp *domain.BookPoint) error
	GetByID(ctx context.Context, id uuid.UUID) (*domain.BookPoint, error)
	GetAll(ctx context.Context) ([]domain.BookPoint, error)
}

type supabaseBookPointRepository struct {
	client *supabase.Client
}

func NewSupabaseBookPointRepository(url, apiKey string) BookPointRepository {
	client, err := supabase.NewClient(url, apiKey, &supabase.ClientOptions{})
	if err != nil {
		log.Fatalf("failed to initialize supabase client: %v", err)
	}

	return &supabaseBookPointRepository{
		client: client,
	}
}

func (r *supabaseBookPointRepository) Insert(ctx context.Context, bp *domain.BookPoint) error {
	bp.ID = uuid.New()
	bp.CreatedAt = bp.CreatedAt.UTC()
	bp.UpdatedAt = bp.UpdatedAt.UTC()

	data, count, err := r.client.From("book_points").Insert(bp, false, "", "representation", "").Execute()
	if err != nil {
		return err
	}

	fmt.Printf("Inserted %d records, response: %s\n", count, string(data))
	return nil
}

func (r *supabaseBookPointRepository) GetByID(ctx context.Context, id uuid.UUID) (*domain.BookPoint, error) {
	data, _, err := r.client.From("book_points").
		Select("*", "exact", false).
		Eq("id", id.String()).
		Execute()

	if err != nil {
		return nil, err
	}

	var results []domain.BookPoint
	if err := json.Unmarshal(data, &results); err != nil {
		return nil, fmt.Errorf("unmarshal GetByID response: %w", err)
	}

	if len(results) == 0 {
		return nil, nil
	}

	return &results[0], nil
}

func (r *supabaseBookPointRepository) GetAll(ctx context.Context) ([]domain.BookPoint, error) {
	data, _, err := r.client.From("book_points").
		Select("*", "exact", false).
		Execute()

	if err != nil {
		return nil, err
	}

	var results []domain.BookPoint
	if err := json.Unmarshal(data, &results); err != nil {
		return nil, fmt.Errorf("unmarshal GetAll response: %w", err)
	}

	log.Printf("GetAll returned %d records", len(results))
	return results, nil
}
