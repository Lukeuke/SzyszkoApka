package dto

type DataResult[T any] struct {
	Data  []T    `json:"data"`
	Total int64  `json:"total"`
	Error string `json:"error,omitempty"`
}

func ErrorResult[T any](err any) DataResult[T] {
	switch e := err.(type) {
	case error:
		return DataResult[T]{Error: e.Error()}
	case string:
		return DataResult[T]{Error: e}
	default:
		return DataResult[T]{Error: "unknown error"}
	}
}
