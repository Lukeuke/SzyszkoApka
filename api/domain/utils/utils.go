package utils

import (
	"reflect"
	"strings"
)

func IsValidJSONField[T any](field string) bool {
	t := reflect.TypeOf((*T)(nil)).Elem()

	for i := 0; i < t.NumField(); i++ {
		f := t.Field(i)
		jsonTag := f.Tag.Get("json")
		if jsonTag == "" {
			continue
		}
		jsonKey := strings.Split(jsonTag, ",")[0]
		if jsonKey == field {
			return true
		}
	}
	return false
}
