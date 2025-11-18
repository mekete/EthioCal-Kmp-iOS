#!/bin/bash

# FCM Test 5: Seasonal Notification
# Tests: SEASONAL category with default priority

SERVER_KEY="YOUR_SERVER_KEY_HERE"

curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "/topics/general",
    "notification": {
      "title": "Spring Season Begins",
      "body": "የፀደይ ወቅት begins today according to the Ethiopian calendar!"
    },
    "data": {
      "category": "SEASONAL",
      "priority": "NORMAL"
    }
  }'

echo -e "\n✅ Seasonal notification sent to topic: general"
echo "Expected: Default priority notification in 'Seasonal Events' channel"
