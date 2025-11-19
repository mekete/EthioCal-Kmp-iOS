# 🔧 FCM Notification Troubleshooting Guide

## Problem: Not Receiving Firebase Notifications

If you're not receiving FCM notifications, follow this step-by-step debugging guide.

---

## Step 1: Check Logcat for "CheckNotification" Tag

Run this command to see all FCM-related logs:

```bash
adb logcat -s CheckNotification:E
```

**What to look for:**

### ✅ Success Pattern (What You SHOULD See):

```
CheckNotification: ===== FCM Service onCreate() called =====
CheckNotification: Service created successfully, creating notification channels...
CheckNotification: Creating channel: Holiday Reminders (ID: holiday_notifications)
CheckNotification: Creating channel: Seasonal Events (ID: seasonal_notifications)
CheckNotification: Creating channel: Daily Insights (ID: daily_insights)
CheckNotification: Creating channel: App Updates (ID: app_updates)
CheckNotification: Creating channel: General Announcements (ID: general_notifications)
CheckNotification: ✅ Created 5 notification channels
CheckNotification: ===== SUBSCRIBING TO FCM TOPICS =====
CheckNotification: Topics to subscribe: [general, holiday-updates, Version107]
CheckNotification: Subscribing to topic: general...
CheckNotification: ✅ Successfully subscribed to topic: general
CheckNotification: Subscribing to topic: holiday-updates...
CheckNotification: ✅ Successfully subscribed to topic: holiday-updates
CheckNotification: Subscribing to topic: Version107...
CheckNotification: ✅ Successfully subscribed to topic: Version107
CheckNotification: ✅ ✅ ALL TOPICS SUBSCRIBED SUCCESSFULLY ✅ ✅
```

### When You Send a Notification, You Should See:

```
CheckNotification: ========================================
CheckNotification: ===== FCM MESSAGE RECEIVED =====
CheckNotification: ========================================
CheckNotification: Message ID: 0:1234567890
CheckNotification: From: /topics/general
CheckNotification: ----- Notification Payload -----
CheckNotification: Title: Test Notification
CheckNotification: Body: This is a test
CheckNotification: ----- Permission Check -----
CheckNotification: Can show notifications: true
CheckNotification: ✅ Permission granted, proceeding with notification
CheckNotification: ----- Converting to AppNotification -----
CheckNotification: Notification ID: ...
CheckNotification: Title: Test Notification
CheckNotification: ✅ ✅ ✅ NOTIFICATION DISPLAYED SUCCESSFULLY ✅ ✅ ✅
```

---

## Step 2: Common Issues and Solutions

### Issue #1: No Logs at All

**Symptom:** `adb logcat -s CheckNotification:E` shows nothing

**Possible Causes:**
1. ❌ App not running or not installed
2. ❌ FCM service not registered in AndroidManifest
3. ❌ Firebase not initialized

**Solutions:**

```bash
# 1. Check if app is installed
adb shell pm list packages | grep com.shalom.calendar

# 2. Check if app is running
adb shell ps | grep com.shalom.calendar

# 3. Reinstall the app
./gradlew installDebug

# 4. Open the app manually on your device
```

---

### Issue #2: Service Created But No Topic Subscription Logs

**Symptom:** You see "FCM Service onCreate()" but no subscription logs

**Possible Causes:**
1. ❌ App initialization not completing
2. ❌ Network connectivity issues

**Solutions:**

```bash
# Check app launch logs
adb logcat -s CheckNotification:E AppInitializationManager:*

# Check network connectivity
adb shell ping -c 3 fcm.googleapis.com

# Force close and restart app
adb shell am force-stop com.shalom.calendar
adb shell am start -n com.shalom.calendar/.MainActivity
```

---

### Issue #3: Subscribed But Never Receiving Messages

**Symptom:** You see "✅ ALL TOPICS SUBSCRIBED SUCCESSFULLY" but no messages arrive

**Possible Causes:**
1. ❌ Wrong topic name in Firebase Console
2. ❌ Notification sent to wrong topic
3. ❌ Firebase project mismatch
4. ❌ google-services.json incorrect

**Solutions:**

**A. Verify Topics Match:**

Your app is subscribed to:
- `general`
- `holiday-updates`
- `Version{YOUR_VERSION}` (e.g., `Version107`)

When sending from Firebase Console, make sure topic name **EXACTLY** matches (case-sensitive!).

**B. Check Firebase Project:**

```bash
# Check which Firebase project your app is connected to
cat app/google-services.json | grep project_id
```

Make sure you're sending from the SAME Firebase project.

**C. Verify App Package Name:**

```bash
# Check package name in google-services.json
cat app/google-services.json | grep package_name

# Should match your app's package name
# com.shalom.calendar
```

---

### Issue #4: Message Received But Permission Denied

**Symptom:** Logs show:
```
CheckNotification: ❌ NOTIFICATION PERMISSION NOT GRANTED!
```

**Solution:**

Grant notification permission:

1. **Settings → Apps → Ethiopian Calendar → Notifications → Allow**
2. Or programmatically in the app

Check permission status:
```bash
adb shell dumpsys package com.shalom.calendar | grep POST_NOTIFICATIONS
```

---

### Issue #5: Message Received But Not Displayed

**Symptom:** Logs show message received and permission granted, but no notification appears

**Possible Causes:**
1. ❌ Notification channel blocked by user
2. ❌ Do Not Disturb mode enabled
3. ❌ Notification icon missing

**Solutions:**

**A. Check Notification Channels:**
```bash
adb shell dumpsys notification | grep "com.shalom.calendar"
```

**B. Manually check on device:**
- Settings → Apps → Ethiopian Calendar → Notifications
- Ensure ALL notification categories are enabled

**C. Check Do Not Disturb:**
- Settings → Sound → Do Not Disturb
- Temporarily disable

**D. Verify notification icon exists:**
Check that `app/src/main/res/drawable/ic_notification_calendar.xml` exists.

---

## Step 3: Get Your FCM Token

To send test messages to your specific device:

```bash
# Run this logcat filter when app starts
adb logcat -s CheckNotification:E | grep "FCM Token"
```

You should see:
```
CheckNotification: ===== NEW FCM TOKEN GENERATED =====
CheckNotification: FCM Token: <long_token_string>
```

Use this token in Firebase Console → Cloud Messaging → "Send test message" → Add an FCM registration token.

---

## Step 4: Test with Firebase Console (Easiest)

1. Open [Firebase Console](https://console.firebase.google.com)
2. Select your project
3. **Cloud Messaging** → **Send your first message**
4. Fill in:
   - **Notification title:** Test
   - **Notification text:** Hello
5. Click **Send test message**
6. Paste your FCM token (from Step 3)
7. Click **Test**

**Expected:** Notification appears within 5-10 seconds.

---

## Step 5: Common Mistakes

### ❌ Using In-App Messaging Instead of Cloud Messaging

**In-App Messaging** ≠ **Cloud Messaging (FCM)**

- ✅ Use: **Cloud Messaging** (FCM)
- ❌ Don't use: In-App Messaging

### ❌ Wrong Topic Format

- ✅ Correct: `general`
- ❌ Wrong: `/topics/general` (don't include `/topics/` prefix in Console)

### ❌ Sending While App is in Background (First Time)

For debugging, keep the app **in foreground** when testing.

### ❌ Not Waiting Long Enough

FCM can take 5-60 seconds. Wait at least 1 minute after sending.

---

## Step 6: Full Debug Checklist

Run through this checklist:

```bash
# 1. Clear logcat
adb logcat -c

# 2. Start logcat filter
adb logcat -s CheckNotification:E

# 3. Force close app
adb shell am force-stop com.shalom.calendar

# 4. Start app
adb shell am start -n com.shalom.calendar/.MainActivity

# 5. Wait 10 seconds for initialization

# 6. Check logs for:
```

- [ ] "FCM Service onCreate() called"
- [ ] "Created 5 notification channels"
- [ ] "✅ Successfully subscribed to topic: general"
- [ ] "✅ Successfully subscribed to topic: holiday-updates"
- [ ] "✅ Successfully subscribed to topic: Version..."

**If all checkmarks pass, your app is ready to receive notifications!**

---

## Step 7: Send Test Notification

### Using Firebase Console:

1. Cloud Messaging → New campaign
2. Target: **Topic** → `general`
3. Title: **Test**
4. Text: **Hello World**
5. Click **Review** → **Publish**

### Watch Logcat:

Within 60 seconds, you should see:

```
CheckNotification: ===== FCM MESSAGE RECEIVED =====
CheckNotification: Title: Test
CheckNotification: Body: Hello World
CheckNotification: ✅ ✅ ✅ NOTIFICATION DISPLAYED SUCCESSFULLY ✅ ✅ ✅
```

---

## Step 8: Still Not Working?

### Get Full Diagnostic Info:

```bash
# Run comprehensive diagnostic
adb logcat -v time *:E | grep -E "Firebase|FCM|CheckNotification"
```

### Check Firebase Status:

https://status.firebase.google.com/

### Verify Internet Connection:

```bash
adb shell ping -c 5 fcm.googleapis.com
```

### Check App Version:

```bash
adb shell dumpsys package com.shalom.calendar | grep versionCode
```

Your version should match the topic you're sending to (e.g., `Version107`).

---

## Common Error Messages and Solutions

| Error Message | Cause | Solution |
|---------------|-------|----------|
| `PERMISSION_DENIED` | Notification permission not granted | Grant permission in app settings |
| `INVALID_RECIPIENT` | Wrong FCM token | Re-copy token from logs |
| `Topic subscription failed` | Network issue | Check internet connection |
| `Service Intent must be explicit` | AndroidManifest issue | Check service declaration |
| `Channel does not exist` | Channels not created | Reinstall app |

---

## Quick Test Script

Save this as `test_notification_debug.sh`:

```bash
#!/bin/bash

echo "🔍 FCM Notification Diagnostic"
echo "================================"

echo "1. Clearing logcat..."
adb logcat -c

echo "2. Restarting app..."
adb shell am force-stop com.shalom.calendar
sleep 2
adb shell am start -n com.shalom.calendar/.MainActivity

echo "3. Waiting for initialization (10 seconds)..."
sleep 10

echo "4. Checking FCM setup..."
adb logcat -d -s CheckNotification:E | grep -E "onCreate|SUBSCRIBED|FCM Token"

echo ""
echo "✅ If you see 'ALL TOPICS SUBSCRIBED SUCCESSFULLY', your app is ready!"
echo "📱 Now send a test notification from Firebase Console"
echo "👀 Watch logs with: adb logcat -s CheckNotification:E"
```

Run it:
```bash
chmod +x test_notification_debug.sh
./test_notification_debug.sh
```

---

## Expected Timeline

| Time | Event |
|------|-------|
| 0s | App launches |
| 1-2s | FCM Service created |
| 2-5s | Notification channels created |
| 5-10s | Topics subscribed |
| --- | Send notification from Firebase Console |
| 5-60s | Notification arrives on device |

If nothing happens after 2 minutes, something is wrong.

---

**Need more help? Share your logcat output:**

```bash
adb logcat -s CheckNotification:E > fcm_logs.txt
```

Then review `fcm_logs.txt` for errors.
