package com.example.healthcare.DependencyInjection

import com.example.healthcare.HospitalRetro.HospitalService
import com.example.healthcare.HospitalRetro.RetrofitInstance
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return RetrofitInstance.getRetrofitInstance()
    }

    @Provides
    @Singleton
    fun provideHospitalService(retrofit: Retrofit): HospitalService {
        return retrofit.create(HospitalService::class.java)
    }

}