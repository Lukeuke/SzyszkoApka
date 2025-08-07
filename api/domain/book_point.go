package domain

import (
	"time"

	"github.com/google/uuid"
)

type BookPoint struct {
	ID          uuid.UUID `json:"id"  db:"id"`
	Lat         float64   `json:"lat" db:"lat"`
	Lon         float64   `json:"lon" db:"lon"`
	CreatedAt   time.Time `json:"created_at" db:"created_at"`
	UpdatedAt   time.Time `json:"updated_at" db:"updated_at"`
	Title       string    `json:"title" db:"title"`
	Description string    `json:"description" db:"description"`
	Approved    bool      `json:"approved" db:"approved"`
}
