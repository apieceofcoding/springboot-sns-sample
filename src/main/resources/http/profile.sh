#!/bin/bash

BASE_URL="http://localhost:8080"

# Get My Posts (including reposts)
echo "=== Get My Posts (including reposts) ==="
curl -X GET "${BASE_URL}/api/v1/profile/posts" \
  -b cookies.txt \
  -v

echo -e "\n\n"

# Get My Replies
echo "=== Get My Replies ==="
curl -X GET "${BASE_URL}/api/v1/profile/replies" \
  -b cookies.txt \
  -v

echo -e "\n\n"

# Get My Liked Posts
echo "=== Get My Liked Posts ==="
curl -X GET "${BASE_URL}/api/v1/profile/likes" \
  -b cookies.txt \
  -v

echo -e "\n"
