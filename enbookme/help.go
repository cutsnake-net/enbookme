package enbookme

import (
	"appengine"
	"html/template"
	"net/http"
)

func ShowHelp(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	user := CurrentUser(c, w, r)
	if user == nil {
		return
	}
	if err := helpTemplate.Execute(w, user); err != nil {
		http.Error(w, "Could not run template", http.StatusInternalServerError)
	}
}

var helpTemplate = template.Must(template.ParseFiles("templates/help.html"))
