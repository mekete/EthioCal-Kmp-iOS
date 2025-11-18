# 🧪 Firebase Cloud Messaging (FCM) Testing Guide

Complete guide to test the comprehensive FCM notification implementation.

---

## 📋 Prerequisites

Before testing, ensure you have:

1. ✅ **App installed** on a physical device or emulator
2. ✅ **Firebase project** configured (google-services.json in place)
3. ✅ **Internet connection** on test device
4. ✅ **Notification permissions** granted in app settings
5. ✅ **Firebase Server Key** from Firebase Console

---

## 🔑 Getting Your Firebase Server Key

1. Open [Firebase Console](https://console.firebase.google.com)
2. Select your project: **ComposeCalendarTwp**
3. Click **⚙️ Project Settings** (gear icon)
4. Go to **Cloud Messaging** tab
5. Under **Project credentials** → **Cloud Messaging API (Legacy)**
6. Copy the **Server key**

⚠️ **Important:** Keep this key secret! Don't commit it to Git.

---

## 📱 Method 1: Firebase Console (Quickest)

### Step-by-Step:

1. **Go to Firebase Console** → Your Project
2. Navigate to: **Engage** → **Messaging** (left sidebar)
3. Click **"Create your first campaign"** or **"New campaign"**
4. Select **"Firebase Notification messages"**

5. **Compose Notification:**
   ```
   Notification title: Test Holiday Reminder
   Notification text: Meskel is coming in 3 days!
   ```

6. **Select Target:**
   - Click **"Send test message"**
   - Option 1: Enter FCM registration token
   - Option 2: Select **"Topic"** → Enter `general`

7. **Click "Test"** or **"Publish"**

✅ **Expected:** Notification appears on your device within seconds.

---

## 🖥️ Method 2: Using Terminal/Curl (Advanced)

### Step 1: Update Script with Your Server Key

Open any test script (e.g., `fcm_test_1_basic.sh`):

```bash
nano fcm_test_1_basic.sh
```

Replace:
```bash
SERVER_KEY="YOUR_SERVER_KEY_HERE"
```

With:
```bash
SERVER_KEY="AAAA...(your actual key)"
```

### Step 2: Make Script Executable

```bash
chmod +x fcm_test_1_basic.sh
```

### Step 3: Run Test

```bash
./fcm_test_1_basic.sh
```

✅ **Expected Output:**
```
✅ Basic notification sent to topic: general
```

---

## 🧪 Available Test Samples

| Test | File | What It Tests | Topic |
|------|------|---------------|-------|
| **1. Basic** | `fcm_test_1_basic.sh` | Simple notification delivery | `general` |
| **2. Holiday Action** | `fcm_test_2_holiday.sh` | Category, priority, action button | `holiday-updates` |
| **3. Version-Specific** | `fcm_test_3_version_specific.sh` | Version-based targeting | `Version107` |
| **4. URL Action** | `fcm_test_4_url_action.sh` | External URL opening | `general` |
| **5. Seasonal** | `fcm_test_5_seasonal.sh` | SEASONAL category | `general` |
| **6. Image** | `fcm_test_6_with_image.sh` | Image support (TODO) | `holiday-updates` |
| **7. All Categories** | `fcm_test_7_all_categories.sh` | All 5 categories | `general` |

---

## 📝 Detailed Test Scenarios

### Test 1: Basic Notification ✅

**Purpose:** Verify FCM is working

```bash
./fcm_test_1_basic.sh
```

**Expected Behavior:**
- ✅ Notification appears in notification drawer
- ✅ Title: "Basic Test"
- ✅ Body: "This is a simple notification test"
- ✅ Tapping opens app
- ✅ Notification channel: "General Notifications"

**Debugging:**
- Check logcat: `adb logcat | grep FCMService`
- Verify topic subscription in Firebase Console

---

### Test 2: Holiday with Action ⭐

**Purpose:** Test category routing, high priority, action buttons

```bash
./fcm_test_2_holiday.sh
```

**Expected Behavior:**
- ✅ **High-priority** notification (heads-up display)
- ✅ Title: "መስቀል Tomorrow!"
- ✅ Body: Long text about Meskel
- ✅ Action button: "View Details"
- ✅ Notification channel: "Holiday Reminders"
- ✅ When tapped: Opens app with intent extras:
  - `navigate_to` = "holiday"
  - `holiday_id` = "meskel_2024"

**Debugging:**
- Check MainActivity receives intent extras
- Verify channel importance in Settings → Apps → Your App → Notifications

---

### Test 3: Version-Specific 🎯

**Purpose:** Test version-based targeting

**⚠️ IMPORTANT:** First, find your app version:

```bash
# Check app version in build.gradle
grep versionCode app/build.gradle
```

Update the script with your version code, then run:

```bash
./fcm_test_3_version_specific.sh
```

**Expected Behavior:**
- ✅ Only devices with matching version receive notification
- ✅ Opens date converter screen when tapped
- ✅ Action button: "Try It Now"

**Verify Version Subscription:**
```bash
# Check logcat when app starts
adb logcat | grep "Subscribed to FCM topic"
```

You should see:
```
Subscribed to FCM topic: Version107
```

---

### Test 4: URL Action 🌐

**Purpose:** Test external URL opening

```bash
./fcm_test_4_url_action.sh
```

**Expected Behavior:**
- ✅ Low-priority notification
- ✅ Action button: "Read More"
- ✅ Tapping opens Wikipedia in browser (not in-app)
- ✅ Notification channel: "Daily Insights"

---

### Test 5: Seasonal 🌸

**Purpose:** Test SEASONAL category

```bash
./fcm_test_5_seasonal.sh
```

**Expected Behavior:**
- ✅ Default priority notification
- ✅ Notification channel: "Seasonal Events"
- ✅ Ethiopian text displayed correctly

---

### Test 6: Image Notification 🖼️

**Purpose:** Test image URL support

```bash
./fcm_test_6_with_image.sh
```

**Current Behavior:**
- ✅ Notification appears with text
- ⚠️ Image NOT displayed (feature is TODO)

**Future Enhancement:**
After adding Coil/Glide, images will display in BigPictureStyle.

---

### Test 7: All Categories 🎨

**Purpose:** Verify all 5 notification channels

```bash
./fcm_test_7_all_categories.sh
```

**Expected Behavior:**
- ✅ 5 notifications appear in sequence
- ✅ Each routed to different channel
- ✅ Check Settings → Apps → Your App → Notifications
- ✅ Should see 6 channels total:
  1. Holiday Reminders
  2. Seasonal Events
  3. Daily Insights
  4. App Updates
  5. General Announcements
  6. Event Reminders (pre-existing)

---

## 🐛 Debugging Guide

### Issue: No Notifications Appear

**Check:**

1. **Notification Permission:**
   ```bash
   adb shell dumpsys package com.shalom.calendar | grep POST_NOTIFICATIONS
   ```

2. **Internet Connection:**
   ```bash
   adb shell ping -c 3 8.8.8.8
   ```

3. **Logcat for FCM:**
   ```bash
   adb logcat | grep -E "FCMService|FirebaseMessaging"
   ```

4. **Topic Subscription:**
   ```bash
   adb logcat | grep "Subscribed to FCM topic"
   ```

### Issue: Wrong Version Received Notification

**Check subscribed topics:**
```bash
# On app launch, check logs
adb logcat | grep "Version"
```

Should show:
```
Subscribed to FCM topic: Version107
Unsubscribed from FCM topic: Version103  (on upgrade)
```

### Issue: Action Button Not Working

**Check MainActivity intent handling:**

```kotlin
// In MainActivity.kt - Add this if not present
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    handleNotificationIntent(intent)
}

private fun handleNotificationIntent(intent: Intent?) {
    intent?.extras?.let { extras ->
        val navigateTo = extras.getString("navigate_to")
        val targetId = extras.getString("holiday_id")
                    ?: extras.getString("event_id")

        Log.d("MainActivity", "Navigate to: $navigateTo, ID: $targetId")
        // TODO: Implement actual navigation
    }
}
```

---

## 📊 Verification Checklist

After running tests, verify:

- [ ] Notifications appear on device
- [ ] Correct notification channels used
- [ ] Action buttons present when defined
- [ ] Tapping notification opens app
- [ ] High-priority notifications display as heads-up
- [ ] Version-specific notifications target correctly
- [ ] URL actions open browser
- [ ] In-app actions pass intent extras
- [ ] All 5 categories create separate channels
- [ ] Logcat shows FCM messages received

---

## 🚀 Quick Test Command

Test everything at once:

```bash
# Make all scripts executable
chmod +x fcm_test_*.sh

# Run basic test
./fcm_test_1_basic.sh

# Wait 5 seconds
sleep 5

# Run holiday test
./fcm_test_2_holiday.sh
```

---

## 📖 Example Logcat Output (Success)

```
D/FCMService: Message received from: /topics/holiday-updates
D/FCMService: Notification displayed: መስቀል Tomorrow!
D/FCMService: Created 5 notification channels
I/FirebaseMessaging: Subscribed to topic: general
I/FirebaseMessaging: Subscribed to topic: holiday-updates
I/FirebaseMessaging: Subscribed to topic: Version107
```

---

## 🎯 Real-World Testing Scenarios

### Scenario 1: New User (First Install)

1. Install app for first time
2. Grant notification permission
3. Send test to `general` topic
4. Verify notification appears

### Scenario 2: App Upgrade

1. Install version 107
2. Check logs: `Subscribed to Version107`
3. Upgrade to version 109
4. Check logs:
   - `Unsubscribed from Version103`
   - `Subscribed to Version109`
5. Send notification to `Version109` topic
6. Verify received

### Scenario 3: Multiple Notifications

1. Send 3 different notifications quickly
2. Verify all 3 appear
3. Check each has unique notification ID
4. All should coexist in drawer

---

## 📞 Support

If tests fail:

1. Check Firebase Console → Cloud Messaging → Send test message
2. Review logcat output
3. Verify google-services.json is correct
4. Ensure Firebase project matches package name
5. Check network connectivity

---

## 🎉 Success Criteria

All tests pass when:

✅ Notifications arrive within 1-10 seconds
✅ Categories route to correct channels
✅ Actions work as expected
✅ Version targeting works
✅ No crashes or errors in logcat
✅ Intent extras passed correctly

---

**Happy Testing! 🚀**
