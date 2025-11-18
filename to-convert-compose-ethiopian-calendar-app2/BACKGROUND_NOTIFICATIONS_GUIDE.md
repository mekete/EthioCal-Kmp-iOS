# 📱 Background Notifications Guide

## Understanding FCM Foreground vs Background Behavior

Firebase Cloud Messaging (FCM) handles notifications differently based on:
1. **App state** (foreground vs background/killed)
2. **Message type** (notification payload vs data-only payload)

---

## 🎯 FCM Message Behavior Matrix

| Message Type | App State | What Happens | Your Code Runs? |
|-------------|-----------|--------------|-----------------|
| **Notification + Data** | Foreground | `onMessageReceived()` called | ✅ YES |
| **Notification + Data** | Background/Killed | System displays automatically | ❌ NO |
| **Data Only** | Foreground | `onMessageReceived()` called | ✅ YES |
| **Data Only** | Background/Killed | `onMessageReceived()` called | ✅ YES |

---

## 🚫 Problem: Why Your Background Notifications Don't Work

When you send from Firebase Console with:

```
Notification title: Test
Notification text: Hello
+ Custom data
```

Firebase sends **both** `notification` and `data` payloads:

```json
{
  "to": "device_token",
  "notification": {
    "title": "Test",
    "body": "Hello"
  },
  "data": {
    "category": "HOLIDAY",
    "priority": "HIGH"
  }
}
```

**Result when app is in background:**
- ❌ Android system displays a basic notification
- ❌ Your `onMessageReceived()` is NOT called
- ❌ Your custom categories, actions, channels are IGNORED
- ❌ Just shows "Test" / "Hello" with default settings

---

## ✅ Solution: Send Data-Only Messages

Send messages with **ONLY** data payload (no `notification` field):

```json
{
  "to": "device_token",
  "data": {
    "title": "Test",
    "body": "Hello",
    "category": "HOLIDAY",
    "priority": "HIGH",
    "actionType": "IN_APP_HOLIDAY",
    "actionTarget": "meskel_2024",
    "actionLabel": "View Details"
  }
}
```

**Result:**
- ✅ `onMessageReceived()` is ALWAYS called (foreground AND background)
- ✅ Your custom logic runs
- ✅ Categories, actions, channels work perfectly
- ✅ Same behavior whether app is open or closed

---

## 📝 Important Notes

### **Note 1: Title and Body Required**

With data-only messages, you MUST include `title` and `body` in the **data payload** (not notification payload):

```json
"data": {
  "title": "Your Title Here",    ← Required
  "body": "Your message here",   ← Required
  "category": "HOLIDAY"
}
```

### **Note 2: Firebase Console Limitation**

**Firebase Console UI does NOT support sending data-only messages.**

You must use one of these methods:
1. ✅ **Firebase REST API** (curl, Postman)
2. ✅ **Firebase Admin SDK** (Node.js, Python, Java)
3. ✅ **Cloud Functions**
4. ❌ Firebase Console UI (doesn't support data-only)

---

## 🧪 How to Test Background Notifications

### **Method 1: Using Curl (Recommended for Testing)**

#### **Step 1: Get Your Server Key**

1. Firebase Console → Project Settings → Cloud Messaging
2. Copy **Server key** under "Cloud Messaging API (Legacy)"

#### **Step 2: Run Test Script**

Update the server key in the script:

```bash
nano test_samples/test_background_notification.sh
```

Change:
```bash
SERVER_KEY="YOUR_SERVER_KEY_HERE"
```

Make executable and run:
```bash
chmod +x test_samples/test_background_notification.sh
./test_samples/test_background_notification.sh
```

#### **Step 3: Test Background Behavior**

1. **Start logcat:**
   ```bash
   adb logcat -s CheckNotification:E
   ```

2. **Put app in background** (press home button)

3. **Run the script** (from Step 2)

4. **Check logcat** - you should see:
   ```
   CheckNotification: ===== FCM MESSAGE RECEIVED =====
   CheckNotification: Title: Background Test
   CheckNotification: ✅ ✅ ✅ NOTIFICATION DISPLAYED SUCCESSFULLY ✅ ✅ ✅
   ```

5. **Check your device** - notification should appear!

---

### **Method 2: Using Postman**

#### **Step 1: Create Request**

1. Open Postman
2. Create new **POST** request
3. URL: `https://fcm.googleapis.com/fcm/send`

#### **Step 2: Set Headers**

```
Authorization: key=YOUR_SERVER_KEY_HERE
Content-Type: application/json
```

#### **Step 3: Set Body (raw JSON)**

```json
{
  "to": "dbCkkLtISQKPclenKVNYeJ:APA91bEO8aw6hUBqgGXwCjbFcyflCg_kao2VRxo7G7OO_keKmbmotG-ZMPW-5rjF6ZH52KgZaR0Q7Fz1_8Nli9O-B-RDLEu9qsHIuRtJrsZxOHT6KL11CVI",
  "data": {
    "title": "Background Test",
    "body": "This works in background!",
    "category": "HOLIDAY",
    "priority": "HIGH"
  }
}
```

#### **Step 4: Send**

Click **Send**, then check your device.

---

### **Method 3: Send to Topic**

Instead of targeting a specific device token, send to a topic:

```bash
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=YOUR_SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "/topics/general",
    "data": {
      "title": "Holiday Reminder",
      "body": "Meskel is coming!",
      "category": "HOLIDAY",
      "priority": "HIGH",
      "actionType": "IN_APP_HOLIDAY",
      "actionTarget": "meskel_2024",
      "actionLabel": "View Details"
    }
  }'
```

**Advantage:** All devices subscribed to `general` topic will receive it.

---

## 🎯 Production Setup

For production, you should use Firebase Admin SDK to send notifications from your server:

### **Node.js Example:**

```javascript
const admin = require('firebase-admin');

admin.initializeApp({
  credential: admin.credential.applicationDefault()
});

const message = {
  data: {
    title: 'Ethiopian New Year',
    body: 'Happy Enkutatash! 🎉',
    category: 'HOLIDAY',
    priority: 'HIGH',
    actionType: 'IN_APP_HOLIDAY',
    actionTarget: 'enkutatash_2024',
    actionLabel: 'Celebrate'
  },
  topic: 'holiday-updates'
};

admin.messaging().send(message)
  .then((response) => {
    console.log('Successfully sent message:', response);
  })
  .catch((error) => {
    console.log('Error sending message:', error);
  });
```

### **Python Example:**

```python
from firebase_admin import messaging, credentials, initialize_app

cred = credentials.Certificate('path/to/serviceAccountKey.json')
initialize_app(cred)

message = messaging.Message(
    data={
        'title': 'Ethiopian New Year',
        'body': 'Happy Enkutatash! 🎉',
        'category': 'HOLIDAY',
        'priority': 'HIGH',
        'actionType': 'IN_APP_HOLIDAY',
        'actionTarget': 'enkutatash_2024',
        'actionLabel': 'Celebrate'
    },
    topic='holiday-updates'
)

response = messaging.send(message)
print('Successfully sent message:', response)
```

---

## 📋 Data Payload Reference

### **Required Fields:**

```json
{
  "title": "Notification title",  // Required
  "body": "Notification message"  // Required
}
```

### **Optional Fields:**

```json
{
  "category": "HOLIDAY|SEASONAL|DAILY_INSIGHT|APP_UPDATE|GENERAL",
  "priority": "LOW|NORMAL|HIGH",
  "actionType": "URL|IN_APP_HOLIDAY|IN_APP_EVENT|IN_APP_CONVERTER|IN_APP_SETTINGS",
  "actionTarget": "target URL or screen identifier",
  "actionLabel": "Button text",
  "imageUrl": "https://example.com/image.jpg"
}
```

### **Complete Example:**

```json
{
  "to": "/topics/holiday-updates",
  "data": {
    "title": "መስቀል Tomorrow!",
    "body": "Meskel celebration begins tomorrow. Join the festivities!",
    "category": "HOLIDAY",
    "priority": "HIGH",
    "actionType": "IN_APP_HOLIDAY",
    "actionTarget": "meskel_2024",
    "actionLabel": "Learn More",
    "imageUrl": "https://example.com/meskel.jpg"
  }
}
```

---

## 🐛 Debugging Background Notifications

### **Check #1: App Can Be Completely Closed**

Kill the app completely:
```bash
adb shell am force-stop com.shalom.calendar
```

Send notification → It should still appear!

### **Check #2: Verify Data-Only Payload**

In logcat, you should see:
```
CheckNotification: No notification payload present
CheckNotification: ----- Data Payload -----
CheckNotification: Data[title] = Background Test
CheckNotification: Data[body] = This works in background!
```

**If you see "Notification Payload" instead, you're sending the wrong format!**

### **Check #3: Common Mistakes**

❌ **Wrong:** Using Firebase Console UI
```
Notification title: Test    ← This creates notification payload
Notification text: Hello
```

✅ **Correct:** Using REST API
```json
{
  "data": {
    "title": "Test",    ← In data payload
    "body": "Hello"
  }
}
```

---

## 📊 Comparison

| Feature | Console (Notification + Data) | API (Data Only) |
|---------|------------------------------|-----------------|
| Works in foreground | ✅ Yes | ✅ Yes |
| Works in background | ⚠️ Basic notification | ✅ Full custom logic |
| Custom categories | ❌ No (background) | ✅ Yes (always) |
| Action buttons | ❌ No (background) | ✅ Yes (always) |
| Rich media | ❌ No (background) | ✅ Yes (always) |
| Easy to send | ✅ Very easy | ⚠️ Requires API |
| Production ready | ❌ No | ✅ Yes |

---

## 🎯 Quick Test Commands

### **Test #1: Specific Device (Background)**

```bash
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=YOUR_SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "YOUR_DEVICE_TOKEN",
    "data": {
      "title": "Test",
      "body": "Background test"
    }
  }'
```

### **Test #2: Topic (Background)**

```bash
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=YOUR_SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "/topics/general",
    "data": {
      "title": "Test",
      "body": "Topic test"
    }
  }'
```

### **Test #3: With All Features**

```bash
curl -X POST https://fcm.googleapis.com/fcm/send \
  -H "Authorization: key=YOUR_SERVER_KEY" \
  -H "Content-Type: application/json" \
  -d '{
    "to": "/topics/holiday-updates",
    "data": {
      "title": "Holiday Alert",
      "body": "Meskel tomorrow!",
      "category": "HOLIDAY",
      "priority": "HIGH",
      "actionType": "IN_APP_HOLIDAY",
      "actionTarget": "meskel_2024",
      "actionLabel": "View Details"
    }
  }'
```

---

## ✅ Success Checklist

- [ ] App works in foreground (confirmed ✅)
- [ ] Get Firebase Server Key
- [ ] Update test script with server key
- [ ] Kill app completely
- [ ] Run data-only test script
- [ ] Check logcat shows "FCM MESSAGE RECEIVED"
- [ ] Notification appears on device
- [ ] Custom category/action works

---

## 🚀 Next Steps

1. **For Testing:** Use the provided curl scripts
2. **For Production:** Implement Firebase Admin SDK on your server
3. **For Scheduled Notifications:** Use Cloud Functions + Cloud Scheduler

---

**Happy testing! 🎉**
