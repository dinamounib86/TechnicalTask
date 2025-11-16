package com.example.aroundegypt.Utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission
import com.example.aroundegypt.data.api.AroundEgyptService
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private  val CACHE_SIZE = 10L * 1024 * 1024 // 10 MB

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache =
        Cache(File(context.cacheDir, "http_cache"), CACHE_SIZE)


    @Provides
    @Singleton
    fun loggingInterceptor(): LoggingInterceptor =
        LoggingInterceptor.Builder()
            .setLevel( Level.BODY)
            .build()

//    @Singleton
//    @Provides
//    fun getSharedPreferences(@ApplicationContext appContext: Context): SharedPreference = SharedPreference(appContext)

    @Provides
    @Singleton
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .build()

    @Provides
    @Singleton
    fun okHttpClient(
        cache: Cache,
        @ApplicationContext context: Context,
        loggingInterceptor: LoggingInterceptor,
    ): OkHttpClient {
        val onlineInterceptor = Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val maxAge = 60 // 1 min cache when online
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }

        val offlineInterceptor = Interceptor { chain ->
            var request = chain.request()
            if (!hasInternet(context)) {
                val maxStale = 60 * 60 * 24 * 7 // 7 days
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .build()
            }
            chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .cache(cache)
            .build()
    }



    @Singleton
    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://aroundegypt.34ml.com/")//BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())//GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }



    @Provides
    @Singleton
    fun getApiService( retrofit: Retrofit): AroundEgyptService {
        return retrofit.create(AroundEgyptService::class.java)
    }

    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun hasInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo?.isConnected == true
    }
}