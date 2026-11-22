
-keepattributes *Annotation*, InnerClasses
-keep @kotlinx.serialization.Serializable class * {
    <fields>;
    <init>();
}


-keep @androidx.room.Entity class * {
    <fields>;
    <init>();
}

# ── Retrofit + Gson ────────────────────────────────────────────────────────────
# 保留 Retrofit API 接口（方法通过动态代理调用）
-keep interface com.example.classschedule.retrofit.DashScopeApi { *; }
-keep interface com.example.classschedule.retrofit.GitHubApi { *; }

# 保留 Gson 反序列化的网络模型
-keep class com.example.classschedule.retrofit.model.** { <fields>; }

# 保留泛型签名信息，Retrofit/Gson 反序列化需要
-keepattributes Signature

# ── 通用抑制警告 ──────────────────────────────────────────────────────────────
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn com.gemalto.jp2.JP2Decoder
