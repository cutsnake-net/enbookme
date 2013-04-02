package enbookme

import (
	"net/http"
)

func init() {
	http.HandleFunc("/", ListPublications)
	http.HandleFunc("/add", AddPublication)
	http.HandleFunc("/help", ShowHelp)
}
