package enbookme

import (
	"appengine"
	"appengine/datastore"
	"appengine/user"
	"http"
	"template"
	"time"
)

type Book struct {
	Name        string
	Owner       string
	URL         string
	Created     datastore.Time
	LastChecked datastore.Time
	LastChanged datastore.Time
}

func init() {
	http.HandleFunc("/", root)
	http.HandleFunc("/add", sign)
}

func root(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	user := currentUser(c, w, r)
	if user == nil {
		return
	}
	q := datastore.NewQuery("Book").Order("-Created").Limit(10)
	books := make([]Book, 0, 10)
	if _, err := q.GetAll(c, &books); err != nil {
		http.Error(w, err.String(), http.StatusInternalServerError)
		return
	}
	if err := booksTemplate.Execute(w, books); err != nil {
		http.Error(w, err.String(), http.StatusInternalServerError)
	}
}

func currentUser(c appengine.Context, w http.ResponseWriter, r *http.Request) *user.User {
	u := user.Current(c)
	if u == nil {
		url, err := user.LoginURL(c, r.URL.String())
		if err != nil {
			http.Error(w, err.String(), http.StatusInternalServerError)
			return nil
		}
		w.Header().Set("Location", url)
		w.WriteHeader(http.StatusFound)
		return nil
	}
	return u
}

var booksTemplate = template.MustParse(booksTemplateHTML, nil)

const booksTemplateHTML = `
<!DOCTYPE html>
<html>
  <head>
    <link rel="stylesheet" type="text/css" href="css/enbookme.css" />
  </head>
  <body>
    <div class="mainPanel">
    <table class="bookList">
      <thead>
        <tr>
          <th>Name</th>
          <th>URL</th>
          <th>Created</th>
          <th>Last Checked</th>
          <th>Last Changed</th>
        </tr>
      </thead>
      <tbody>
        {.repeated section @}
        <tr class="bookEntry">
          <th>{Name|html}</th>
          <td>{URL|html}</td>
          <td>{Created|html}</td>
          <td>{LastChecked|html}</td>
          <td>{LastChanged|html}</td>
        </tr>
        {.end}
      </tbody>
    </table>
    <form action="/add" method="post" class="addBook">
      <div><input type="text" name="name"></input></div>
      <div><input type="text" name="URL"></input></div>
      <div><input type="submit" value="Enbook it"></div>
    </form>
    </div>
  </body>
</html>
`

func sign(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	/*
		startTime, err := time.Parse("%Y%m%d-%H:%M:%S", r.FormValue("start"))
		if err != nil {
			http.Error(w, err.String(), http.StatusInternalServerError)
			return
		}
		endTime, err := time.Parse("%Y%m%d-%H:%M:%S", r.FormValue("end"))
		if err != nil {
			http.Error(w, err.String(), http.StatusInternalServerError)
			streturn
		}
	*/
	g := Book{
		Name:    r.FormValue("name"),
		URL:     r.FormValue("URL"),
		Owner:   r.FormValue("creator"),
		Created: datastore.SecondsToTime(time.Seconds()),
	}
	_, err := datastore.Put(c, datastore.NewIncompleteKey("Book"), &g)
	if err != nil {
		http.Error(w, err.String(), http.StatusInternalServerError)
		return
	}
	http.Redirect(w, r, "/", http.StatusFound)
}

