package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"log"
	"time"

	"szyszko-api/domain"
	"szyszko-api/domain/utils"
	dto "szyszko-api/presentation/dto/common"

	"github.com/google/uuid"
	"github.com/supabase-community/postgrest-go"
	supabase "github.com/supabase-community/supabase-go"
)

type BookPointRepository interface {
	Insert(ctx context.Context, bp *domain.BookPoint) (uuid.UUID, error)
	Remove(ctx context.Context, id uuid.UUID) error
	Approve(ctx context.Context, id uuid.UUID) error
	GetByID(ctx context.Context, id uuid.UUID) (*domain.BookPoint, error)
	GetAll(ctx context.Context, dataQuery *dto.DataQuery) (dto.DataResult[domain.BookPoint], error)
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

	data, count, err := r.client.From("book_points").Insert(bp, false, "", "representation", "").Execute()
	if err != nil {
		return uuid.Nil, err
	}

	log.Printf("Inserted %d records, response: %s\n", count, string(data))
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
	result := dto.DataResult[domain.BookPoint]{
		Total: 0,
		Data:  []domain.BookPoint{},
	}

	validOperators := map[string]bool{
		"eq": true, "neq": true, "gt": true, "gte": true,
		"lt": true, "lte": true, "like": true, "ilike": true,
		"in": true, "is": true,
	}

	query := r.client.From("book_points").Select("*", "exact", false)

	for _, filter := range dataQuery.Filters {
		if !validOperators[filter.Operator] {
			return result, fmt.Errorf("invalid filter operator: %s", filter.Operator)
		}

		query = query.Filter(filter.Field, filter.Operator, filter.Value)
	}

	if dataQuery.Sort != "" {
		field, ascending := dto.ParseSortParam(dataQuery.Sort)

		if !utils.IsValidJSONField[domain.BookPoint](field) {
			return result, fmt.Errorf("invalid sort field: %s", field)
		}

		query = query.Order(field, &postgrest.OrderOpts{Ascending: ascending})
	}

	start := (dataQuery.Page - 1) * dataQuery.PageSize
	end := dataQuery.Page*dataQuery.PageSize - 1
	query = query.Range(start, end, "")

	data, _, err := query.Execute()

	if err != nil {
		return result, fmt.Errorf("query execution failed: %w", err)
	}

	_, total, err := r.client.From("book_points").
		Select("id", "exact", true).
		Range(0, 0, "").
		Execute()

	if err != nil {
		return result, err
	}

	var bookPoints []domain.BookPoint
	if err := json.Unmarshal(data, &bookPoints); err != nil {
		return result, fmt.Errorf("unmarshal GetAll response: %w", err)
	}

	result.Data = bookPoints
	result.Total = total

	return result, nil
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
