package repository

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"log"
	"time"

	"szyszko-api/domain"
	base "szyszko-api/infrastructure/repositories/base"
	dto "szyszko-api/presentation/dto/common"

	"github.com/google/uuid"
	supabase "github.com/supabase-community/supabase-go"
)

type BookPointRepository interface {
	Insert(ctx context.Context, bp *domain.BookPoint) (uuid.UUID, error)
	Edit(ctx context.Context, bp *domain.BookPoint) (uuid.UUID, error)
	Remove(ctx context.Context, id uuid.UUID) error
	Approve(ctx context.Context, id uuid.UUID) error
	GetByID(ctx context.Context, id uuid.UUID) (*domain.BookPoint, error)
	GetAll(ctx context.Context, dataQuery *dto.DataQuery) (dto.DataResult[domain.BookPoint], error)
	ExistsByExternalKey(ctx context.Context, externalKey string) (bool, error)
	AssignFileName(ctx context.Context, id uuid.UUID, fileName string) (bool, error)
	AttachmentsCount(ctx context.Context, id uuid.UUID) (int, error)
}

type supabaseBookPointRepository struct {
	client *supabase.Client
}

func NewSupabaseBookPointRepository(client *supabase.Client) BookPointRepository {
	return &supabaseBookPointRepository{
		client: client,
	}
}

func (r *supabaseBookPointRepository) Insert(ctx context.Context, bp *domain.BookPoint) (uuid.UUID, error) {
	bp.ID = uuid.New()
	bp.CreatedAt = time.Now().UTC()
	bp.UpdatedAt = time.Now().UTC()
	userID, ok := ctx.Value("user_id").(string)

	if !ok {
		return uuid.Nil, errors.New("user_id not found in context")
	}

	bp.CreatedBy = userID

	data, count, err := r.client.From("book_points").Insert(bp, false, "", "representation", "").Execute()
	if err != nil {
		return uuid.Nil, err
	}

	log.Printf("Inserted %d records, response: %s\n", count, string(data))
	return bp.ID, nil
}

func (r *supabaseBookPointRepository) Edit(ctx context.Context, bp *domain.BookPoint) (uuid.UUID, error) {
	bp.UpdatedAt = time.Now().UTC()

	updateData := map[string]interface{}{
		"title":       bp.Title,
		"description": bp.Description,
		"lat":         bp.Lat,
		"lon":         bp.Lon,
		"approved":    bp.Approved,
		"updated_at":  time.Now().UTC(),
	}

	_, _, err := r.client.
		From("book_points").
		Update(updateData, "", "").
		Eq("id", bp.ID.String()).
		Execute()

	if err != nil {
		return uuid.Nil, fmt.Errorf("failed to update book point: %w", err)
	}

	return bp.ID, nil
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

func (r *supabaseBookPointRepository) GetAll(ctx context.Context, dataQuery *dto.DataQuery) (dto.DataResult[domain.BookPoint], error) {
	baseRepo := base.NewBaseRepository[domain.BookPoint](r.client, "book_points")
	return baseRepo.GetAll(ctx, dataQuery)
}

func (r *supabaseBookPointRepository) Approve(ctx context.Context, id uuid.UUID) error {
	updateData := map[string]interface{}{
		"approved":   true,
		"updated_at": time.Now().UTC(),
	}

	_, _, err := r.client.From("book_points").Update(updateData, "", "").Eq("id", id.String()).Execute()

	if err != nil {
		return fmt.Errorf("failed to approve book point: %w", err)
	}

	return nil
}

func (r *supabaseBookPointRepository) Remove(ctx context.Context, id uuid.UUID) error {
	_, _, err := r.client.
		From("book_points").
		Delete("", "").
		Eq("id", id.String()).
		Execute()

	if err != nil {
		return fmt.Errorf("failed to remove book point: %w", err)
	}

	return nil
}

func (r *supabaseBookPointRepository) ExistsByExternalKey(ctx context.Context, externalKey string) (bool, error) {
	data, _, err := r.client.From("book_points").
		Select("external_key", "exact", false).
		Eq("external_key", externalKey).
		Execute()

	if err != nil {
		return false, err
	}

	var results []domain.BookPoint
	if err := json.Unmarshal(data, &results); err != nil {
		return false, fmt.Errorf("unmarshal ExistsByExternalKey response: %w", err)
	}

	return len(results) > 0, nil
}

func (r *supabaseBookPointRepository) AssignFileName(ctx context.Context, id uuid.UUID, fileName string) (bool, error) {
	existing, err := r.GetByID(ctx, id)
	if err != nil {
		return false, fmt.Errorf("failed to fetch book point: %w", err)
	}
	if existing == nil {
		return false, fmt.Errorf("book point not found")
	}

	updatedImages := append(existing.Images, fileName)

	updateData := map[string]interface{}{
		"images":     updatedImages,
		"updated_at": time.Now().UTC(),
	}

	_, _, err = r.client.
		From("book_points").
		Update(updateData, "", "").
		Eq("id", id.String()).
		Execute()

	if err != nil {
		return false, fmt.Errorf("failed to update images field: %w", err)
	}

	return true, nil
}

func (r *supabaseBookPointRepository) AttachmentsCount(ctx context.Context, id uuid.UUID) (int, error) {
	data, _, err := r.client.From("book_points").
		Select("images", "exact", false).
		Eq("id", id.String()).
		Execute()

	if err != nil {
		return -1, err
	}

	var results []struct {
		Images []string `json:"images"`
	}

	if err := json.Unmarshal(data, &results); err != nil {
		return -1, fmt.Errorf("unmarshal images column: %w", err)
	}

	if len(results) == 0 {
		return 0, nil
	}

	return len(results[0].Images), nil
}
