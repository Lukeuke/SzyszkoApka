package repository

import (
	supabase "github.com/supabase-community/supabase-go"
)

type UnitOfWork struct {
	BookPointRepo BookPointRepository
}

func NewUnitOfWork(client *supabase.Client) *UnitOfWork {
	return &UnitOfWork{
		BookPointRepo: NewSupabaseBookPointRepository(client),
	}
}
