#!/bin/bash

# Background-compatible FCM notification test
# Sends DATA-ONLY payload that triggers onMessageReceived() even when app is in background

SERVER_KEY="AAAAetmmk3Y:APA91bHq-2-9UU7LTm21J7d4RmlI3LcC5eqtoHMyTChE5Ivxu3gyzIaLBveV3fOFGcpi-vQV5qNtVEQz8pKnXQJvyD8NrYfphCNhTWjwRe-uWVZqxwXq73IV7rdGkj0G8emLBg61Pnno"
FCM_TOKEN="dbCkkLtISQKPclenKVNYeJ:APA91bEO8aw6hUBqgGXwCjbFcyflCg_kao2VRxo7G7OO_keKmbmotG-ZMPW-5rjF6ZH52KgZaR0Q7Fz1_8Nli9O-B-RDLEu9qsHIuRtJrsZxOHT6KL11CVI"

echo "🚀 Sending DATA-ONLY notification (works in background)..."
echo "This will trigger your custom onMessageReceived() logic"
echo ""

curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d "{
    \"to\": \"$FCM_TOKEN\",
    \"data\": {
      \"title\": \"Background Test\",
      \"body\": \"This notification works even when app is in background!\",
      \"category\": \"HOLIDAY\",
      \"priority\": \"HIGH\",
      \"actionType\": \"IN_APP_HOLIDAY\",
      \"actionTarget\": \"meskel_2024\",
      \"actionLabel\": \"View Details\"
    }
  }"

echo ""
echo "✅ Notification sent!"
echo "📱 Check your device (even if app is closed)"
echo "👀 Check logcat: adb logcat -s CheckNotification:E"
