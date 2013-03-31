package enbookme

import (
	"appengine"
	"appengine/datastore"
	"appengine/user"
	"html/template"
	"net/http"
)

func ListPublications(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	user := CurrentUser(c, w, r)
	if user == nil {
		return
	}
	q := datastore.NewQuery("Publication").Order("-Created").Limit(10)
	books := make([]Publication, 0, 10)
	if _, err := q.GetAll(c, &books); err != nil {
		http.Error(w, "Could not look up book list", http.StatusInternalServerError)
		return
	}
	if len(books) > 0 {
		c.Infof("Sending email")
		err := SendEmail(c, books[0].Key(c))
		if err != nil {
			c.Errorf(err.Error())
		}
	} else {
		c.Infof("No books found")
	}
	if err := booksTemplate.Execute(w, books); err != nil {
		http.Error(w, "Could not run template", http.StatusInternalServerError)
	}
}

func CurrentUser(c appengine.Context, w http.ResponseWriter, r *http.Request) *user.User {
	u := user.Current(c)
	if u == nil {
		url, err := user.LoginURL(c, r.URL.String())
		if err != nil {
			http.Error(w, "Could not get login URL", http.StatusInternalServerError)
			return nil
		}
		w.Header().Set("Location", url)
		w.WriteHeader(http.StatusFound)
		return nil
	}
	return u
}

var booksTemplate = template.Must(template.ParseFiles("templates/home.html"))
