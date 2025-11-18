#!/bin/bash

echo "🔍 Notification Diagnostic for Ethiopian Calendar"
echo "=================================================="
echo ""

echo "1. Checking app notification permissions..."
adb shell dumpsys notification | grep -A 5 "com.shalom.calendar" | head -n 10

echo ""
echo "2. Checking Do Not Disturb status..."
ZEN_MODE=$(adb shell settings get global zen_mode)
if [ "$ZEN_MODE" = "0" ]; then
    echo "   ✅ Do Not Disturb is OFF"
else
    echo "   ❌ Do Not Disturb is ON (value: $ZEN_MODE)"
    echo "   Turn it off: Settings → Sound → Do Not Disturb"
fi

echo ""
echo "3. Checking notification channels..."
adb shell cmd notification list_channels com.shalom.calendar 2>/dev/null || echo "   ⚠️ Could not list channels"

echo ""
echo "4. Checking battery optimization..."
BATTERY=$(adb shell dumpsys deviceidle whitelist | grep com.shalom.calendar)
if [ -z "$BATTERY" ]; then
    echo "   ⚠️ App is battery optimized (may affect background notifications)"
    echo "   Fix: Settings → Apps → Ethiopian Calendar → Battery → Unrestricted"
else
    echo "   ✅ App is whitelisted from battery optimization"
fi

echo ""
echo "5. Testing system notification (bypass FCM)..."
adb shell cmd notification post -S bigtext -t 'Diagnostic Test' 'TestTag' 'If you see this, device notifications work!' com.shalom.calendar
echo "   📱 Check your device for a test notification"

echo ""
echo "6. Checking if app is running..."
RUNNING=$(adb shell ps | grep com.shalom.calendar)
if [ -z "$RUNNING" ]; then
    echo "   ⚠️ App is not running"
else
    echo "   ✅ App is running"
fi

echo ""
echo "=================================================="
echo "✅ Diagnostic complete!"
echo ""
echo "Next steps:"
echo "1. Check your device for the test notification"
echo "2. If test notification appeared, issue is with FCM message format"
echo "3. If test notification didn't appear, check device settings above"
