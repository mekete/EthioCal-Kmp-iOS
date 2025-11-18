#!/bin/bash

# FCM Test 3: Version-Specific Notification
# Tests: Version-based topic targeting
# NOTE: Replace VERSION_CODE with your actual app version (e.g., 107, 109, etc.)

SERVER_KEY="YOUR_SERVER_KEY_HERE"
VERSION_CODE="107"  # Change this to your app's version code

curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=$SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d "{
    \"to\": \"/topics/Version$VERSION_CODE\",
    \"notification\": {
      \"title\": \"New Feature Available!\",
      \"body\": \"You're running version $VERSION_CODE. Check out the new date converter feature!\"
    },
    \"data\": {
      \"category\": \"APP_UPDATE\",
      \"priority\": \"NORMAL\",
      \"actionType\": \"IN_APP_CONVERTER\",
      \"actionLabel\": \"Try It Now\"
    }
  }"

echo -e "\n✅ Version-specific notification sent to topic: Version$VERSION_CODE"
echo "Expected: Only devices with version $VERSION_CODE will receive this"
