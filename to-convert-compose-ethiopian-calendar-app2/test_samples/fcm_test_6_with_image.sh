#!/bin/bash

# FCM Test 6: Notification with Image
# Tests: Image URL support (Note: Image loading is TODO in current implementation)

SERVER_KEY="YOUR_SERVER_KEY_HERE"

curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "/topics/holiday-updates",
    "notification": {
      "title": "እንቁጣጣሽ - Ethiopian New Year",
      "body": "Happy Enkutatash! Celebrate the Ethiopian New Year with joy and blessings.",
      "image": "https://picsum.photos/400/300"
    },
    "data": {
      "category": "HOLIDAY",
      "priority": "HIGH",
      "imageUrl": "https://picsum.photos/400/300",
      "actionType": "IN_APP_HOLIDAY",
      "actionTarget": "enkutatash_2024",
      "actionLabel": "Celebrate"
    }
  }'

echo -e "\n✅ Image notification sent to topic: holiday-updates"
echo "NOTE: Image loading is currently TODO - will be added with Coil/Glide integration"
