package repository

import (
	supabase "github.com/supabase-community/supabase-go"
)

type UnitOfWork struct {
	BookPointRepo BookPointRepository
	IdentityRepo  IdentityRepository
}

func NewUnitOfWork(client *supabase.Client) *UnitOfWork {
	return &UnitOfWork{
		BookPointRepo: NewSupabaseBookPointRepository(client),
		IdentityRepo:  NewIdentityRepository(client),
	}
}
