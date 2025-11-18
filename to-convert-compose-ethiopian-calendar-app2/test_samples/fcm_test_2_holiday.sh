#!/bin/bash

# FCM Test 2: Holiday Notification with In-App Action
# Tests: Category routing, priority, action buttons, in-app navigation

SERVER_KEY="YOUR_SERVER_KEY_HERE"

curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "/topics/holiday-updates",
    "notification": {
      "title": "መስቀል Tomorrow!",
      "body": "Meskel - The Finding of the True Cross celebration begins tomorrow. Join the festivities!"
    },
    "data": {
      "category": "HOLIDAY",
      "priority": "HIGH",
      "actionType": "IN_APP_HOLIDAY",
      "actionTarget": "meskel_2024",
      "actionLabel": "View Details"
    }
  }'

echo -e "\n✅ Holiday notification sent to topic: holiday-updates"
echo "Expected: High-priority notification with 'View Details' button"
