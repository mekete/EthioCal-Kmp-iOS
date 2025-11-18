#!/bin/bash

# FCM Test 1: Basic Notification
# Tests: Simple notification delivery

SERVER_KEY="YOUR_SERVER_KEY_HERE"

curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "/topics/general",
    "notification": {
      "title": "Basic Test",
      "body": "This is a simple notification test"
    }
  }'

echo -e "\n✅ Basic notification sent to topic: general"
