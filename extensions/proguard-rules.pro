-dontobfuscate
-dontoptimize
-keepattributes *
# Extension entry points are called from patched TikTok bytecode by name.
-keep class app.unscroll.** {
  *;
}
-dontwarn com.ss.android.**
