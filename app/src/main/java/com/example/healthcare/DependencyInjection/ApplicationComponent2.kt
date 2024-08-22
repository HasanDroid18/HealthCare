package com.example.healthcare.DependencyInjection

import com.example.healthcare.Fragments.HospitalFragment
import com.example.healthcare.Fragments.MapFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent2 {
    fun inject(fragment: MapFragment)
}