package repository

import (
	"context"
	"encoding/json"
	"fmt"
	"szyszko-api/domain"
	base "szyszko-api/infrastructure/repositories/base"
	dto "szyszko-api/presentation/dto/common"

	supabase "github.com/supabase-community/supabase-go"
)

type IdentityRepository interface {
	GetAll(ctx context.Context, dataQuery *dto.DataQuery) (dto.DataResult[domain.Identity], error)
	GetByUsername(ctx context.Context, username string) (*domain.Identity, error)
	UpdatePassword(ctx context.Context, username string, newHash string) error
}

type supabaseIdentityRepository struct {
	client *supabase.Client
}

func (r *supabaseIdentityRepository) GetAll(ctx context.Context, dataQuery *dto.DataQuery) (dto.DataResult[domain.Identity], error) {
	baseRepo := base.NewBaseRepository[domain.Identity](r.client, "identity")
	return baseRepo.GetAll(ctx, dataQuery)
}

func NewIdentityRepository(client *supabase.Client) IdentityRepository {
	return &supabaseIdentityRepository{
		client: client,
	}
}

func (r *supabaseIdentityRepository) GetByUsername(ctx context.Context, username string) (*domain.Identity, error) {
	data, _, err := r.client.From("identity").
		Select("*", "exact", false).
		Eq("username", username).
		Execute()

	if err != nil {
		return nil, err
	}

	var results []domain.Identity
	if err := json.Unmarshal(data, &results); err != nil {
		return nil, fmt.Errorf("unmarshal GetByUsername response: %w", err)
	}

	if len(results) == 0 {
		return nil, nil
	}

	return &results[0], nil
}

func (r *supabaseIdentityRepository) UpdatePassword(ctx context.Context, username string, newHash string) error {
	update := map[string]interface{}{
		"password_hash": newHash,
	}

	_, _, err := r.client.
		From("identity").
		Update(update, "minimal", "").
		Eq("username", username).
		Execute()

	if err != nil {
		return fmt.Errorf("update password failed: %w", err)
	}

	return nil
}
