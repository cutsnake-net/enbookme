package enbookme

import (
"fmt"
	"time"

	"appengine"
	"appengine/datastore"
	"appengine/urlfetch"
	"appengine/mail"
)

type UrlFetchError struct {
	PubId  int64
	Url    string
	Status string
}


func (e *UrlFetchError) Error() string {
    return fmt.Sprintf("URL Fetch Error (%s) for %s, requested by Publication %d", e.Status, e.Url, e.PubId)
}

func SendEmail(c appengine.Context, id int64) error {
	// Lookup the publication
	key := datastore.NewKey(c, "publication", "", id, nil)
	var pub Publication
	if err := datastore.Get(c, key, &pub); err != nil {
		return err
	}

	// Update the mod times
	pub.LastChanged = time.Now()

	// Go and get the content
	client := urlfetch.Client(c)
	resp, err := client.Get(pub.Url)
	if err != nil {
		return err
	}
	if resp.StatusCode != 200 {
		return &UrlFetchError{id, pub.Url, resp.Status}
	}

	buf := make([]byte, resp.ContentLength)
	_, err = resp.Body.Read(buf)
	if err != nil {
		return err
	}

	doc := &mail.Attachment{
		Name: "Update from Enbook.me", 
		Data: buf,
	}
	message := &mail.Message{
		Sender:      "",
		To:          []string{""},
		Subject:     "",
		Body:        "Update from Enbook.me",
		Attachments: []mail.Attachment{*doc},
	}
	if err := mail.Send(c, message); err != nil {
		return err
	}
	pub.LastChanged = time.Now()

	if _, err = datastore.Put(c, key, pub); err != nil {
		return err
	}
return nil;
}
