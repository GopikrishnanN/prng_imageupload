package com.prng.androidimageuploadcomponent.di

import android.annotation.SuppressLint
import android.content.Context
import com.prng.androidimageuploadcomponent.data.AuthApi
import com.prng.androidimageuploadcomponent.data.AuthRepositoryImpl
import com.prng.androidimageuploadcomponent.domain.AuthRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.create
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

interface AppModule {
    val authApi: AuthApi
    val authRepository: AuthRepository
}

class AppModuleImpl(private val appContext: Context) : AppModule {
    override val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.escuelajs.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient()!!.build())
            .build()
            .create()
    }
    override val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApi)
    }
}

private fun getUnsafeOkHttpClient(): OkHttpClient.Builder? {
    return try {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts: Array<TrustManager> = arrayOf(
            @SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient().newBuilder().connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.MINUTES)
            .build()

        // Install the all-trusting trust manager
        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        okHttpClient.sslSocketFactory
        val builder = okHttpClient.newBuilder()
        builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        builder.hostnameVerifier { _, _ -> true }
        builder
    } catch (e: Exception) {
        throw RuntimeException(e)
    }
}