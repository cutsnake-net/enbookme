package enbookme

import (
	"fmt"
	"time"

	"appengine"
	"appengine/datastore"
	"appengine/mail"
	"appengine/urlfetch"
)

type UrlFetchError struct {
	PubId  int64
	Url    string
	Status string
}

func (e *UrlFetchError) Error() string {
	return fmt.Sprintf("URL Fetch Error (%s) for %s, requested by Publication %d", e.Status, e.Url, e.PubId)
}

func SendEmail(c appengine.Context, key *datastore.Key) error {
	// Lookup the publication
	var pub Publication
	c.Infof("Using key %s", key.StringID())
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
		return &UrlFetchError{key.IntID(), pub.Url, resp.Status}
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
		Sender:      "Enbook.me <jamieburrell@gmail.com>",
		To:          []string{pub.Email},
		Subject:     "",
		Body:        "Update from Enbook.me",
		Attachments: []mail.Attachment{*doc},
	}
	if err := mail.Send(c, message); err != nil {
		return err
	}
	pub.LastChanged = time.Now()

	// Save the changes
	if _, err = datastore.Put(c, key, pub); err != nil {
		return err
	}
	return nil
}
