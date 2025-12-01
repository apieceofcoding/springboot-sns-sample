#!/bin/bash

BASE_URL="http://localhost:8080"

# Create Quote
echo "=== Create Quote (Quote Post ID=1) ==="
curl -X POST "${BASE_URL}/api/v1/posts/1/quotes" \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "content": "I want to quote this post!"
  }' \
  -v

echo -e "\n"
