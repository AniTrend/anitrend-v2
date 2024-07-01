# Keep everything
-keep class * { *; }
-dontshrink
-dontobfuscate
-dontoptimize

# Optionally, keep annotations if needed
-keepattributes *Annotation*

-dontwarn java.lang.invoke.StringConcatFactory
