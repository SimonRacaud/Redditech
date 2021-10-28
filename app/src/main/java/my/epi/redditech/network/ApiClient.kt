package my.epi.redditech.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceOAuthInterceptor : Interceptor {

    private fun getCredential() : String {
        val clientId = "e14xOSvlU0BhmAuhho8z3Q"
        val clientSecret = ""

        val credentials = Credentials.basic(clientId, clientSecret)
        return credentials
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = request.newBuilder()
            .addHeader("Authorization", getCredential())
            .addHeader("User-Agent", "android:my.epi.redditech:v0.1.0 (by /u/TauteBZH)")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()
        return chain.proceed(request)
    }
}

//TODO: Improve that, move class or find other functional method (@Headers)
class ServiceInterceptor : Interceptor {

    var token : String = "";

    fun Token(token: String ) {
        this.token = token;
    }

    /*private getTokenFromSharedPreference(): String {
        val objectSharedPreferences = this.getSharedPreference()
    }*/

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if(request.header("No-Authentication")==null){
            //val token = getTokenFromSharedPreference();
            //or use Token Function
            if(!token.isNullOrEmpty())
            {
                val finalToken =  "Bearer "+token
                request = request.newBuilder()
                    .addHeader("Authorization",finalToken)
                    .addHeader("User-Agent", "android:my.epi.redditech:v0.1.0 (by /u/TauteBZH)")
                    .build()
            }
        }
        return chain.proceed(request)
    }
}


class ApiClient {
    companion object {
        private val BASE_URL: String = "https://oauth.reddit.com/"
        private val OAUTH_BASE_URL: String = "https://www.reddit.com/"
        private val serviceInterceptor: ServiceInterceptor = ServiceInterceptor()
        private val serviceOAuthInterceptor: ServiceOAuthInterceptor = ServiceOAuthInterceptor()

        var interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()

        var token: String = ""

        init {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        }

        private val gson: Gson by lazy {
            GsonBuilder().setLenient().create()
        }

        private val httpClient: OkHttpClient by lazy {
            serviceInterceptor.Token(token)
            OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(serviceInterceptor)
                .build()
        }

        private val oauthHttpClient: OkHttpClient by lazy {

            OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(serviceOAuthInterceptor)
                .build()
        }

        private val retrofit: Retrofit by lazy {

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        private val retrofitOauth: Retrofit by lazy {

            Retrofit.Builder()
                .baseUrl(OAUTH_BASE_URL)
                .client(oauthHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        val apiReddit: ApiReddit by lazy {
            retrofit.create(ApiReddit::class.java)
        }

        val oauthReddit: OAuthReddit by lazy {
            retrofitOauth.create(OAuthReddit::class.java)
        }
    }
}
