package enbookme

import (
	"appengine"
	"appengine/datastore"
	"net/http"
	"time"
)

func AddPublication(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	g := Publication{
		Name:    r.FormValue("Name"),
		Url:     r.FormValue("URL"),
		Email:   r.FormValue("Email"),
		Created: time.Now(),
	}
	_, err := datastore.Put(c, g.Key(c), &g)
	if err != nil {
		http.Error(w, "Could not store book", http.StatusInternalServerError)
		return
	}
	http.Redirect(w, r, "/", http.StatusFound)
}

