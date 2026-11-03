-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.jarvis.ai.**$$serializer { *; }
-keepclassmembers class com.jarvis.ai.** {
    *** Companion;
}
-keepclasseswithmembers class com.jarvis.ai.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class com.jarvis.ai.data.remote.dto.** { *; }
-keep class okhttp3.** { *; }
-keep class retrofit2.** { *; }
-dontwarn okhttp3.**
-dontwarn retrofit2.**