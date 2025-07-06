package dto

type DataResult[T any] struct {
	Data  []T   `json:"data"`
	Total int64 `json:"total"`
}
