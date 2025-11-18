#!/bin/bash

# Background-compatible FCM notification test using TOPICS
# Sends DATA-ONLY payload that works even when app is in background

SERVER_KEY="AAAAetmmk3Y:APA91bHq-2-9UU7LTm21J7d4RmlI3LcC5eqtoHMyTChE5Ivxu3gyzIaLBveV3fOFGcpi-vQV5qNtVEQz8pKnXQJvyD8NrYfphCNhTWjwRe-uWVZqxwXq73IV7rdGkj0G8emLBg61Pnno"

echo "🚀 Sending DATA-ONLY notification to topic: general"
echo "This works in background because it has NO notification payload"
echo ""

curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "/topics/general",
    "data": {
      "title": "Holiday Reminder",
      "body": "መስቀል (Meskel) celebration is coming soon!",
      "category": "HOLIDAY",
      "priority": "HIGH",
      "actionType": "IN_APP_HOLIDAY",
      "actionTarget": "meskel_2024",
      "actionLabel": "View Details"
    }
  }'

echo ""
echo "✅ Notification sent to topic: general"
echo "📱 Check your device (app can be closed/background)"
echo "👀 Check logcat: adb logcat -s CheckNotification:E"
