package base

import (
	"context"
	"encoding/json"
	"fmt"

	"szyszko-api/domain/utils"
	dto "szyszko-api/presentation/dto/common"

	"github.com/supabase-community/postgrest-go"
	"github.com/supabase-community/supabase-go"
)

type BaseRepository[T any] struct {
	client    *supabase.Client
	tableName string
	validOps  map[string]bool
}

func NewBaseRepository[T any](client *supabase.Client, tableName string) *BaseRepository[T] {
	return &BaseRepository[T]{
		client:    client,
		tableName: tableName,
		validOps: map[string]bool{
			"eq": true, "neq": true, "gt": true, "gte": true,
			"lt": true, "lte": true, "like": true, "ilike": true,
			"in": true, "is": true,
		},
	}
}

func (r *BaseRepository[T]) GetAll(ctx context.Context, query *dto.DataQuery) (dto.DataResult[T], error) {
	result := dto.DataResult[T]{
		Total: 0,
		Data:  []T{},
	}

	q := r.client.From(r.tableName).Select("*", "exact", false)

	for _, filter := range query.Filters {
		if !r.validOps[filter.Operator] {
			return result, fmt.Errorf("invalid filter operator: %s", filter.Operator)
		}
		q = q.Filter(filter.Field, filter.Operator, filter.Value)
	}

	if query.Sort != "" {
		field, asc := dto.ParseSortParam(query.Sort)
		if !utils.IsValidJSONField[T](field) {
			return result, fmt.Errorf("invalid sort field: %s", field)
		}
		q = q.Order(field, &postgrest.OrderOpts{Ascending: asc})
	}

	start := (query.Page - 1) * query.PageSize
	end := query.Page*query.PageSize - 1
	q = q.Range(start, end, "")

	data, _, err := q.Execute()
	if err != nil {
		return result, fmt.Errorf("query execution failed: %w", err)
	}

	_, total, err := r.client.From(r.tableName).Select("id", "exact", true).Range(0, 0, "").Execute()
	if err != nil {
		return result, err
	}

	var items []T
	if err := json.Unmarshal(data, &items); err != nil {
		return result, fmt.Errorf("unmarshal error: %w", err)
	}

	result.Data = items
	result.Total = total
	return result, nil
}
