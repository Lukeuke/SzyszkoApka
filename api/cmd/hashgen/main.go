package main

import (
	"fmt"
	"os"
	"szyszko-api/application/helpers"
)

func main() {
	if len(os.Args) < 3 {
		fmt.Println("Usage: hashgen <username> <password>")
		os.Exit(1)
	}

	username := os.Args[1]
	password := os.Args[2]

	hashed, err := helpers.HashPassword(password)
	if err != nil {
		fmt.Println("Failed to hash password:", err)
		os.Exit(1)
	}

	fmt.Println("Username:", username)
	fmt.Println("Password Hash:", hashed)
}
