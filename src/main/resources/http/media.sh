#!/bin/bash

BASE_URL="http://localhost:8080"

# Initialize Media Upload (IMAGE)
echo "=== Initialize Media Upload (IMAGE) ==="
RESPONSE=$(curl -X POST "${BASE_URL}/api/v1/media/init" \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "mediaType": "IMAGE"
  }' \
  -s)

echo "$RESPONSE"
PRESIGNED_URL=$(echo "$RESPONSE" | grep -o '"presignedUrl":"[^"]*"' | cut -d'"' -f4)
echo "Presigned URL: $PRESIGNED_URL"

# Upload image to presigned URL (example with test image)
# Replace /path/to/image.jpg with your actual image path
echo -e "\n=== Upload Image to Presigned URL ==="
echo "To upload an image, use:"
echo "curl -X PUT \"\$PRESIGNED_URL\" -H \"Content-Type: image/jpeg\" --data-binary @/path/to/image.jpg"
echo ""
echo "Example:"
echo "# Create a test image file"
echo "# echo 'test' > test.jpg"
echo "# curl -X PUT \"\$PRESIGNED_URL\" -H \"Content-Type: image/jpeg\" --data-binary @test.jpg"

echo -e "\n\n"

# Initialize Media Upload (VIDEO)
echo "=== Initialize Media Upload (VIDEO) ==="
curl -X POST "${BASE_URL}/api/v1/media/init" \
  -H "Content-Type: application/json" \
  -b cookies.txt \
  -d '{
    "mediaType": "VIDEO"
  }' \
  -v

echo -e "\n\n"

# Get Media by ID
echo "=== Get Media by ID (ID=1) ==="
curl -X GET "${BASE_URL}/api/v1/media/1" \
  -v

echo -e "\n\n"

# Get Media by User ID
echo "=== Get Media by User ID (User ID=1) ==="
curl -X GET "${BASE_URL}/api/v1/users/1/media" \
  -v

echo -e "\n\n"

# Delete Media
echo "=== Delete Media (ID=1) ==="
curl -X DELETE "${BASE_URL}/api/v1/media/1" \
  -b cookies.txt \
  -v

echo -e "\n"
