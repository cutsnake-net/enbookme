package enbookme

import (
  "appengine"
  "appengine/datastore"
  "net/http"
)

func CheckForUpdates(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	q := datastore.NewQuery("Publication").Order("-Created").Limit(10)
	books := make([]Publication, 0, 10)
	if _, err := q.GetAll(c, &books); err != nil {
		http.Error(w, "Could not look up book list", http.StatusInternalServerError)
		return
	}
  for _, book := range books {
      err := SendEmail(c, book.Key(c))
  		if err != nil {
  			c.Errorf("%s: %s", book.Name, err.Error())
  		}
  }
}