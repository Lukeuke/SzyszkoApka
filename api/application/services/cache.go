package cache

// THIS ACTS MORE LIKE A RATE-LIMIT BUT ITS ON PURPOSE

import (
	"sync"
	"time"
)

type CacheItem struct {
	Data      interface{}
	Timestamp time.Time
}

type Cache struct {
	data  map[string]CacheItem
	mutex sync.RWMutex
	ttl   time.Duration
}

func NewCache(ttl time.Duration) *Cache {
	return &Cache{
		data: make(map[string]CacheItem),
		ttl:  ttl,
	}
}

func (c *Cache) IsValid(key string) bool {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	item, exists := c.data[key]
	if !exists {
		return false
	}

	return time.Since(item.Timestamp) <= c.ttl
}

func (c *Cache) Get(key string) (interface{}, bool) {
	c.mutex.RLock()
	defer c.mutex.RUnlock()

	item, exists := c.data[key]
	if !exists || time.Since(item.Timestamp) > c.ttl {
		return nil, false
	}

	return item.Data, true
}

func (c *Cache) Set(key string, data interface{}) {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	c.data[key] = CacheItem{
		Data:      data,
		Timestamp: time.Now(),
	}
}

func (c *Cache) Cleanup() {
	c.mutex.Lock()
	defer c.mutex.Unlock()

	for key, item := range c.data {
		if time.Since(item.Timestamp) > c.ttl {
			delete(c.data, key)
		}
	}
}
