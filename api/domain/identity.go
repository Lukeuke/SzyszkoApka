package domain

import (
	"time"

	"github.com/google/uuid"
)

type Identity struct {
	ID           uuid.UUID `json:"id"  db:"id"`
	CreatedAt    time.Time `json:"created_at" db:"created_at"`
	Username     string    `json:"username" db:"username"`
	PasswordHash string    `json:"password_hash" db:"password_hash"`
	Enabled      bool      `json:"enabled" db:"enabled"`
}
