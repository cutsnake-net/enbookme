package enbookme

import (
	"appengine"
	"appengine/datastore"
	"appengine/user"
	"net/http"
	"html/template"
)

func ListPublications(w http.ResponseWriter, r *http.Request) {
	c := appengine.NewContext(r)
	user := currentUser(c, w, r)
	if user == nil {
		return
	}
	q := datastore.NewQuery("Publication").Order("-Created").Limit(10)
	books := make([]Publication, 0, 10)
	if _, err := q.GetAll(c, &books); err != nil {
		http.Error(w, "Could not look up book list", http.StatusInternalServerError)
		return
	}
	if err := booksTemplate.Execute(w, books); err != nil {
		http.Error(w, "Could not run template", http.StatusInternalServerError)
	}
}

func currentUser(c appengine.Context, w http.ResponseWriter, r *http.Request) *user.User {
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

var booksTemplate = template.Must(template.New("Main").Parse(booksTemplateHTML))

const booksTemplateHTML = `
<!DOCTYPE html>
<html>
  <head>
    <title>enbook.me</title>
    <meta name="keywords" content="ebook, kindle, doc">
    <meta name="description" content="Tracks updates to URLs and forwards them to your Ebook reader">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="css/enbookme.css" />
  </head>
  <body>
    <div class="mainPanel">
    <form action="/add" method="post" class="addPublication">
      <div><label for="Name">Name</label><input type="text" name="Name"></input></div>
      <div><label for="URL">URL</label><input type="text" name="URL"></input></div>
      <div><label for="Email">Email</label><input type="text" name="Email"></input></div>
      <div><input type="submit" value="Enbook it"></div>
    </form>
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
        {{range .}}
        <tr class="bookEntry">
          <th>{{.Name}}</th>
          <td>{{.Url}}</td>
          <td>{{.Created}}</td>
          <td>{{.LastChecked}}</td>
          <td>{{.LastChanged}}</td>
        </tr>
        {{end}}
      </tbody>
    </table>
    </div>
  </body>
</html>
`
