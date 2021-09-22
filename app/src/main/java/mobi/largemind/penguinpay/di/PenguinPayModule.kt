package mobi.largemind.penguinpay.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mobi.largemind.penguinpay.repository.ExchangesRepository
import mobi.largemind.penguinpay.repository.ExchangesService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class PenguinPayModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://openexchangerates.org")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun provideExchangeRatesService(retrofit: Retrofit): ExchangesService {
        return retrofit.create(ExchangesService::class.java)
    }

    @Provides
    fun provideExchangesRepository(service: ExchangesService): ExchangesRepository {
        return ExchangesRepository(service)
    }
}