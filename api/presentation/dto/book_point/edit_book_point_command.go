package dto

type EditBookPointCommand struct {
	Lat         float64 `json:"lat" binding:"required"`
	Lon         float64 `json:"lon" binding:"required"`
	Approved    *bool   `json:"approved" binding:"required"`
	Title       string  `json:"title" binding:"required"`
	Description string  `json:"description" binding:"required"`
}
