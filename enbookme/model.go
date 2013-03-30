package enbookme

import (
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
	Owner          int64
	Url            string
}
