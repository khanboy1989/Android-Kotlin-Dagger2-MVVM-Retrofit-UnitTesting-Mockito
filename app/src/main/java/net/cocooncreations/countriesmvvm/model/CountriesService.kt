package net.cocooncreations.countriesmvvm.model

import io.reactivex.Single
import net.cocooncreations.countriesmvvm.di.component.DaggerApiComponent
import javax.inject.Inject

class CountriesService {

    @Inject
    lateinit var api:CountriesApi

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getCountries():Single<List<Country>>{
        return api.getCountries()
    }
}