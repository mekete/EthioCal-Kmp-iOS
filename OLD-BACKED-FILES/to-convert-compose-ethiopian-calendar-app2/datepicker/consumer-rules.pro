# ================================================================================================
# Consumer ProGuard rules for the Ethiopian DatePicker Library
# ================================================================================================
# These rules are automatically applied to apps that use this library

# Keep all public API classes
-keep public class com.shalom.android.material.datepicker.** {
    public protected *;
}

# Keep Parcelable implementations
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep Compose functions (if library exposes Composables)
-keep @androidx.compose.runtime.Composable class com.shalom.android.material.datepicker.** { *; }
-keepclassmembers class com.shalom.android.material.datepicker.** {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep ThreeTen classes used by the library
-keep class org.threeten.extra.** { *; }

# Keep kotlinx.serialization if used
-keepattributes *Annotation*, InnerClasses

# ================================================================================================
# END OF CONSUMER PROGUARD RULES
# ================================================================================================
