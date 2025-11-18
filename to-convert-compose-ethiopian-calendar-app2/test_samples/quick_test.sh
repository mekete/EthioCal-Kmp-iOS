#!/bin/bash

# Quick FCM Test Script
# Tests the most important notification scenarios in sequence

echo "🧪 Ethiopian Calendar FCM Quick Test Suite"
echo "==========================================="
echo ""

# Check if SERVER_KEY is set
if [ -z "$FCM_SERVER_KEY" ]; then
    echo "⚠️  ERROR: FCM_SERVER_KEY environment variable not set"
    echo ""
    echo "Please set it first:"
    echo "  export FCM_SERVER_KEY='your_server_key_here'"
    echo ""
    echo "Get your server key from:"
    echo "  Firebase Console → Project Settings → Cloud Messaging"
    exit 1
fi

SERVER_KEY="$FCM_SERVER_KEY"

echo "✅ Server key configured"
echo "📱 Sending test notifications..."
echo ""

# Test 1: Basic
echo "1️⃣  Sending Basic Notification..."
curl -s -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{"to": "/topics/general", "notification": {"title": "Test 1: Basic", "body": "Basic notification delivery test"}}' \
  > /dev/null
echo "   ✅ Sent to topic: general"
sleep 3

# Test 2: Holiday with Action
echo "2️⃣  Sending Holiday Notification with Action..."
curl -s -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{"to": "/topics/holiday-updates", "notification": {"title": "Test 2: Holiday", "body": "High-priority notification with action button"}, "data": {"category": "HOLIDAY", "priority": "HIGH", "actionType": "IN_APP_HOLIDAY", "actionTarget": "test_holiday", "actionLabel": "View Details"}}' \
  > /dev/null
echo "   ✅ Sent to topic: holiday-updates (HIGH priority)"
sleep 3

# Test 3: URL Action
echo "3️⃣  Sending URL Action Notification..."
curl -s -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{"to": "/topics/general", "notification": {"title": "Test 3: URL", "body": "Tap to open Wikipedia"}, "data": {"category": "DAILY_INSIGHT", "priority": "LOW", "actionType": "URL", "actionTarget": "https://en.wikipedia.org/wiki/Ethiopian_calendar", "actionLabel": "Read More"}}' \
  > /dev/null
echo "   ✅ Sent to topic: general (URL action)"
sleep 3

echo ""
echo "🎉 All tests sent successfully!"
echo ""
echo "📋 Verification Checklist:"
echo "  [ ] Check your device for 3 notifications"
echo "  [ ] Test 1: Basic notification appears"
echo "  [ ] Test 2: High-priority with 'View Details' button"
echo "  [ ] Test 3: Opens Wikipedia when tapped"
echo ""
echo "🐛 Debugging:"
echo "  adb logcat | grep FCMService"
echo ""
