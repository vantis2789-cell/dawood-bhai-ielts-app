package com.example.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

/**
 * Utility singleton managing the Supabase client connection, authentication headers,
 * and project credentials for Dawood Bhai IELTS Studio.
 *
 * Prepares the application for PostgreSQL / PostgREST CRUD operations against
 * the `profiles`, `test_sets`, `writing_submissions`, and `speaking_submissions` tables.
 */
object SupabaseClient {

    private const val TAG = "SupabaseClient"

    // Default project credentials (can be overridden via initialize())
    private var _supabaseUrl: String = "https://jwqaebwqscylw3phnkoe.supabase.co"
    private var _supabaseAnonKey: String = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imp3cWFlYndxc2N5bHczcGhua29lIiwicm9sZSI6ImFub24iLCJpYXQiOjE3OTExNzAwMDAsImV4cCI6MjEwNjc0NjAwMH0.sample_anon_token_ielts_studio"

    // Session token when authenticated via Supabase Auth
    private var _userAccessToken: String? = null
    private var _currentUserId: String? = null

    val supabaseUrl: String
        get() = _supabaseUrl

    val supabaseAnonKey: String
        get() = _supabaseAnonKey

    val isUserAuthenticated: Boolean
        get() = !_userAccessToken.isNullOrBlank()

    val currentUserId: String?
        get() = _currentUserId

    /**
     * Initializes the Supabase client with project keys.
     * Can be invoked during Application or Repository startup.
     */
    fun initialize(
        url: String,
        anonKey: String
    ) {
        _supabaseUrl = url.trimEnd('/')
        _supabaseAnonKey = anonKey
    }

    /**
     * Sets the active authenticated user session token obtained from Supabase Auth.
     */
    fun setSession(accessToken: String?, userId: String?) {
        _userAccessToken = accessToken
        _currentUserId = userId
    }

    /**
     * Clears user session on logout.
     */
    fun clearSession() {
        _userAccessToken = null
        _currentUserId = null
    }

    /**
     * Builds standard headers required by Supabase PostgREST endpoints.
     * Enforces Row-Level Security (RLS) by attaching Bearer token when logged in.
     */
    fun getHeaders(): Map<String, String> {
        val bearerToken = _userAccessToken ?: _supabaseAnonKey
        return mapOf(
            "apikey" to _supabaseAnonKey,
            "Authorization" to "Bearer $bearerToken",
            "Content-Type" to "application/json",
            "Prefer" to "return=representation"
        )
    }

    /**
     * Interceptor automatically injecting Supabase apikey & Authorization headers.
     */
    class SupabaseAuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original: Request = chain.request()
            val requestBuilder = original.newBuilder()

            // Inject headers
            getHeaders().forEach { (name, value) ->
                requestBuilder.header(name, value)
            }

            return chain.proceed(requestBuilder.build())
        }
    }

    /**
     * Shared OkHttpClient configured for Supabase REST and Auth endpoints.
     */
    val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(SupabaseAuthInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    // ==========================================
    // Endpoint URL Builders for CRUD Operations
    // ==========================================

    /**
     * Generates REST URL for `profiles` table.
     * e.g. GET https://.../rest/v1/profiles?id=eq.<user_id>
     */
    fun getProfilesUrl(userId: String? = null): String {
        val base = "$_supabaseUrl/rest/v1/profiles"
        return if (userId != null) "$base?id=eq.$userId&select=*" else "$base?select=*"
    }

    /**
     * Generates REST URL for `test_sets` table.
     * e.g. GET https://.../rest/v1/test_sets?is_active=eq.true
     */
    fun getTestSetsUrl(testSetId: String? = null): String {
        val base = "$_supabaseUrl/rest/v1/test_sets"
        return if (testSetId != null) "$base?id=eq.$testSetId&select=*" else "$base?select=*"
    }

    /**
     * Generates Supabase Auth endpoint URLs (signup, token, logout).
     */
    fun getAuthUrl(endpoint: String): String {
        val cleanEndpoint = endpoint.trimStart('/')
        return "$_supabaseUrl/auth/v1/$cleanEndpoint"
    }
}
