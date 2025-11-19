# ================================================================================================
# ProGuard Configuration for Ethiopian DatePicker Library
# ================================================================================================

# Keep Parcelable implementations
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep all public classes and methods in the library
-keep public class com.shalom.android.material.datepicker.** {
    public protected *;
}

# Keep Compose UI components
-keep @androidx.compose.runtime.Composable class * { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}

# Keep Jetpack Compose
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.foundation.** { *; }
-keep class androidx.compose.material3.** { *; }

# Keep ThreeTen Extra for EthiopicDate
-keep class org.threeten.** { *; }
-dontwarn org.threeten.**

# Keep kotlinx.serialization
-keepattributes *Annotation*, InnerClasses
-dontwarn kotlinx.serialization.**

# Keep Kotlin metadata
-keepattributes Signature
-keepattributes *Annotation*

# Keep source file names and line numbers for better stack traces
-keepattributes SourceFile,LineNumberTable

# ================================================================================================
# END OF PROGUARD CONFIGURATION
# ================================================================================================
