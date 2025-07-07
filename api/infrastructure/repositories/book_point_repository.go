package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"log"

	"szyszko-api/domain"
	dto "szyszko-api/presentation/dto/common"

	"github.com/google/uuid"
	"github.com/supabase-community/postgrest-go"
	supabase "github.com/supabase-community/supabase-go"
)

type BookPointRepository interface {
	Insert(ctx context.Context, bp *domain.BookPoint) error
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

func (r *supabaseBookPointRepository) Insert(ctx context.Context, bp *domain.BookPoint) error {
	bp.ID = uuid.New()
	bp.CreatedAt = bp.CreatedAt.UTC()
	bp.UpdatedAt = bp.UpdatedAt.UTC()

	data, count, err := r.client.From("book_points").Insert(bp, false, "", "representation", "").Execute()
	if err != nil {
		return err
	}

	log.Printf("Inserted %d records, response: %s\n", count, string(data))
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

func (r *supabaseBookPointRepository) GetAll(ctx context.Context, dataQuery *dto.DataQuery) (dto.DataResult[domain.BookPoint], error) {

	query := r.client.From("book_points").Select("*", "exact", false)

	for _, filter := range dataQuery.Filters {
		query = query.Filter(filter.Field, filter.Operator, filter.Value)
	}

	if dataQuery.Sort != "" {
		field, ascending := dto.ParseSortParam(dataQuery.Sort)
		query = query.Order(field, &postgrest.OrderOpts{Ascending: ascending})
	}

	start := (dataQuery.Page - 1) * dataQuery.PageSize
	end := dataQuery.Page*dataQuery.PageSize - 1
	query = query.Range(start, end, "")

	data, _, err := query.Execute()

	result := dto.DataResult[domain.BookPoint]{
		Total: 0,
		Data:  []domain.BookPoint{},
	}

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
