package dto

import "strings"

type DataQuery struct {
	// sort?=<field>-<dir>
	Sort string `form:"sort"`

	// Start: 1
	Page int `form:"page"`

	// Max: 1000
	PageSize int `form:"pageSize"`

	Filters []Filter
}

type Filter struct {
	Field    string `json:"field"`
	Operator string `json:"operator"`
	Value    string `json:"value"`
}

func ParseSortParam(sort string) (field string, ascending bool) {
	field = "created_at"
	direction := "asc"
	ascending = true

	parts := strings.Split(sort, "-")
	if len(parts) == 2 {
		field = parts[0]
		direction = parts[1]
	}

	if direction != "asc" && direction != "desc" {
		ascending = true
	} else {
		ascending = false
	}

	return field, ascending
}
