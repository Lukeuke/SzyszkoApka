package dto

type CreateBookPointCommand struct {
	Lat         float64 `json:"lat" binding:"required"`
	Lon         float64 `json:"lon" binding:"required"`
	Title       string  `json:"title" binding:"required"`
	Description string  `json:"description"`
}
