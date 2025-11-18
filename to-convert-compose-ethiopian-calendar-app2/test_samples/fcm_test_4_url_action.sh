#!/bin/bash

# FCM Test 4: URL Action
# Tests: Opening external URLs in browser

SERVER_KEY="YOUR_SERVER_KEY_HERE"

curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "/topics/general",
    "notification": {
      "title": "Learn About Ethiopian Calendar",
      "body": "Discover the fascinating history of the Ethiopian calendar system"
    },
    "data": {
      "category": "DAILY_INSIGHT",
      "priority": "LOW",
      "actionType": "URL",
      "actionTarget": "https://en.wikipedia.org/wiki/Ethiopian_calendar",
      "actionLabel": "Read More"
    }
  }'

echo -e "\n✅ URL action notification sent to topic: general"
echo "Expected: Low-priority notification that opens Wikipedia when tapped"
