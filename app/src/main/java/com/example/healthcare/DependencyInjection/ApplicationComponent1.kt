package com.example.healthcare.DependencyInjection

import com.example.healthcare.Fragments.HospitalFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class])
interface ApplicationComponent1 {
    fun inject(fragment: HospitalFragment)
}