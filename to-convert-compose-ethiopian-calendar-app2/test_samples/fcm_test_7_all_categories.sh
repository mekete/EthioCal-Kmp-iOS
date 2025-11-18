#!/bin/bash

# FCM Test 7: All Categories Test
# Tests: All notification categories to verify channel creation

SERVER_KEY="YOUR_SERVER_KEY_HERE"

echo "Sending notifications to all 5 categories..."

# Category 1: HOLIDAY
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{"to": "/topics/general", "notification": {"title": "HOLIDAY Test", "body": "Testing holiday category"}, "data": {"category": "HOLIDAY"}}'
echo -e "\n✅ 1/5 HOLIDAY sent"
sleep 2

# Category 2: SEASONAL
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{"to": "/topics/general", "notification": {"title": "SEASONAL Test", "body": "Testing seasonal category"}, "data": {"category": "SEASONAL"}}'
echo -e "\n✅ 2/5 SEASONAL sent"
sleep 2

# Category 3: DAILY_INSIGHT
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{"to": "/topics/general", "notification": {"title": "DAILY_INSIGHT Test", "body": "Testing daily insight category"}, "data": {"category": "DAILY_INSIGHT"}}'
echo -e "\n✅ 3/5 DAILY_INSIGHT sent"
sleep 2

# Category 4: APP_UPDATE
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{"to": "/topics/general", "notification": {"title": "APP_UPDATE Test", "body": "Testing app update category"}, "data": {"category": "APP_UPDATE"}}'
echo -e "\n✅ 4/5 APP_UPDATE sent"
sleep 2

# Category 5: GENERAL
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{"to": "/topics/general", "notification": {"title": "GENERAL Test", "body": "Testing general category"}, "data": {"category": "GENERAL"}}'
echo -e "\n✅ 5/5 GENERAL sent"

echo -e "\n🎉 All category tests complete!"
echo "Check your device for 5 notifications in different channels"
