# ── kotlinx.serialization ─────────────────────────────────────────────────────
# 保留所有 @Serializable 类（包括导航路由和 Room 实体中的序列化字段）
-keepattributes *Annotation*, InnerClasses
-keep @kotlinx.serialization.Serializable class * {
    <fields>;
    <init>();
}

# ── Room ──────────────────────────────────────────────────────────────────────
# 保留 Room 实体类，防止字段被混淆导致 Room 生成的代码出错
-keep @androidx.room.Entity class * {
    <fields>;
    <init>();
}

# ── Retrofit + Gson ────────────────────────────────────────────────────────────
# 保留 Retrofit API 接口（方法通过动态代理调用）
-keep interface com.example.classschedule.retrofit.ClassScheduleApi { *; }

# 保留 Gson 反序列化的网络模型
-keep class com.example.classschedule.retrofit.model.** { <fields>; }

# 保留泛型签名信息，Retrofit/Gson 反序列化需要
-keepattributes Signature

# ── 通用抑制警告 ──────────────────────────────────────────────────────────────
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn com.gemalto.jp2.JP2Decoder
