package enbookme

import (
	"appengine"
	"appengine/datastore"
	"net/http"
	"time"
)

func AddPublication(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	u := CurrentUser(c, w, r)
	g := Publication{
		Name:    r.FormValue("Name"),
		Url:     r.FormValue("URL"),
		Email:   r.FormValue("Email"),
		Owner:	 u.Email,
		Created: time.Now(),
	}
	_, err := datastore.Put(c, g.Key(c), &g)
	if err != nil {
    c.Errorf("Could not store book: %s", err.Error())
		http.Error(w, "Could not store book", http.StatusInternalServerError)
		return
	}
	http.Redirect(w, r, "/", http.StatusFound)
}

