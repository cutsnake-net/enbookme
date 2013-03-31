package enbookme

import (
	"appengine"
	"appengine/datastore"
	"fmt"
	"time"
)

type Publication struct {
	Checksum       string
	Created        time.Time
	Email          string
	LastChanged    time.Time
	LastChecked    time.Time
	LastUpdateSent time.Time
	Length         int64
	Name           string
	Owner          string
	Url            string
}

func (r *Publication) Key(c appengine.Context) *datastore.Key {
	return datastore.NewKey(c, "Publication", fmt.Sprintf("%s:%s:%s", r.Owner, r.Url, r.Email), 0, nil)
}
