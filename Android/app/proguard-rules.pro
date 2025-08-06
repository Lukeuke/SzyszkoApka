# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class
com.szyszkodar.szyszkomapka.data.remote.filter.BookpointsFilter { *; }
-keep class
com.szyszkodar.szyszkomapka.data.remote.query.GetBookpointsQuery { *; }
-keep class
com.szyszkodar.szyszkomapka.data.remote.body.** { *; }
-keep class
com.szyszkodar.szyszkomapka.data.mappers.BookpointsMapper { *; }
-keep class
com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenState { *; }
-keep class
com.szyszkodar.szyszkomapka.data.remote.response.** { *; }
-keep class
com.szyszkodar.szyszkomapka.domain.remote.ApiRequest { *; }
-keep class
com.szyszkodar.szyszkomapka.domain.remote.ApiRequest$* { *; }

-keep class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-keepattributes Signature
-keepattributes *Annotation*

-keep class org.maplibre.android.** { *; }
-dontwarn org.maplibre.android.**

-keep class org.maplibre.android.maps.MapView { *; }
-keep class org.maplibre.android.maps.Projection { *; }

-keep class org.maplibre.android.geometry.LatLng { *; }
-keep class android.graphics.PointF { *; }

-keep class com.szyszkodar.szyszkomapka.presentation.mapScreen.** { *; }

-keep class org.maplibre.android.plugins.** { *; }
-dontwarn org.maplibre.android.plugins.**