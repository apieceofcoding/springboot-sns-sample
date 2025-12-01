#!/bin/bash

BASE_URL="http://localhost:8080"

# Get My Posts
echo "=== Get My Posts ==="
curl -X GET "${BASE_URL}/api/v1/profile/posts" \
  -b cookies.txt \
  -v

echo -e "\n\n"

# Get My Replies
echo "=== Get My Replies ==="
curl -X GET "${BASE_URL}/api/v1/profile/replies" \
  -b cookies.txt \
  -v

echo -e "\n"
