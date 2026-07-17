package com.example.data.api

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

// --- moshi data models ---

@JsonClass(generateAdapter = true)
data class Part(
    val text: String? = null,
    val inlineData: InlineData? = null
)

@JsonClass(generateAdapter = true)
data class InlineData(
    val mimeType: String,
    val data: String // base64
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>,
    val role: String? = null
)

@JsonClass(generateAdapter = true)
data class ThinkingConfig(
    val thinkingLevel: String // "low", "medium", "high"
)

@JsonClass(generateAdapter = true)
data class ImageConfig(
    val aspectRatio: String? = null,
    val imageSize: String? = null // "1K", "2K", "4K"
)

@JsonClass(generateAdapter = true)
data class SpeechConfig(
    val voiceConfig: VoiceConfig? = null
)

@JsonClass(generateAdapter = true)
data class VoiceConfig(
    val prebuiltVoiceConfig: PrebuiltVoiceConfig? = null
)

@JsonClass(generateAdapter = true)
data class PrebuiltVoiceConfig(
    val voiceName: String // e.g. "Puck", "Charon", "Kore", "Fenrir"
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    val temperature: Float? = null,
    val topP: Float? = null,
    val topK: Int? = null,
    val maxOutputTokens: Int? = null,
    val responseMimeType: String? = null,
    val responseModalities: List<String>? = null, // e.g. ["TEXT", "IMAGE"], ["AUDIO"]
    val thinkingConfig: ThinkingConfig? = null,
    val imageConfig: ImageConfig? = null,
    val speechConfig: SpeechConfig? = null
)

@JsonClass(generateAdapter = true)
data class SearchGroundingSpec(
    val dummy: String? = null // Retrofit Moshi compatibility helper
)

@JsonClass(generateAdapter = true)
data class GoogleSearchTool(
    val googleSearch: Map<String, String>? = null
)

@JsonClass(generateAdapter = true)
data class GoogleMapsTool(
    val googleMaps: Map<String, String>? = null
)

@JsonClass(generateAdapter = true)
data class Tool(
    val googleSearch: SearchGroundingSpec? = null,
    val googleMaps: SearchGroundingSpec? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null,
    val systemInstruction: Content? = null,
    val tools: List<Tool>? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content,
    val finishReason: String? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    val candidates: List<Candidate>? = null
)

// For Veo Video Generation
@JsonClass(generateAdapter = true)
data class GenerateVideosRequest(
    val prompt: String,
    val config: VeoConfig? = null
)

@JsonClass(generateAdapter = true)
data class VeoConfig(
    val numberOfVideos: Int = 1,
    val resolution: String = "720p",
    val aspectRatio: String = "16:9"
)

@JsonClass(generateAdapter = true)
data class VeoResponse(
    val name: String? = null,
    val metadata: Map<String, String>? = null
)

// --- retrofit client ---

interface GeminiApiService {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse

    @POST("v1beta/models/{model}:generateVideos")
    suspend fun generateVideos(
        @Path("model") model: String,
        @Query("key") apiKey: String,
        @Body request: GenerateVideosRequest
    ): VeoResponse
}

object GeminiApiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    // Configure 60 seconds timeouts as strictly mandated in SKILL.md
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApiService::class.java)
    }

    /**
     * Checks if API key is empty or placeholder to avoid hard crashes
     */
    fun getApiKey(): String {
        val key = BuildConfig.GEMINI_API_KEY
        return if (key.isEmpty() || key == "MY_GEMINI_API_KEY" || key.contains("PLACEHOLDER")) "" else key
    }

    /**
     * Helper to verify if key is set
     */
    fun isKeyConfigured(): Boolean {
        return getApiKey().isNotEmpty()
    }
}
