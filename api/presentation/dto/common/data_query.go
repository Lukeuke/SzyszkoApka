package dto

import "strings"

type DataQuery struct {
	// sort?=<field>-<dir>
	Sort string `form:"sort"`

	// Start: 1
	Page int `form:"page"`

	// Max: 1000
	PageSize int `form:"page_size"`

	Filters []Filter
}

type Filter struct {
	Field string `json:"field"`

	// Common operators:
	//   - "eq" (equal): Matches exactly
	//   - "neq" (not equal): Does not match
	//   - "gt" (greater than): Field value is greater than the value
	//   - "gte" (greater than or equal): Field value is greater than or equal to the value
	//   - "lt" (less than): Field value is less than the value
	//   - "lte" (less than or equal): Field value is less than or equal to the value
	//   - "like" (SQL LIKE): Field value matches the pattern
	//   - "ilike" (case-insensitive LIKE): Case-insensitive matching
	//   - "in" (within): Checks if the field value is within a list of values
	//   - "is" (IS NULL or IS NOT NULL): Checks for NULL values
	//   - "match" (full-text search): Checks for full-text search matches
	//   - "overlap": Checks if two arrays overlap	Operator string `json:"operator"`
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
