package net.cocooncreations.countriesmvvm.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import net.cocooncreations.countriesmvvm.di.component.DaggerApiComponent
import net.cocooncreations.countriesmvvm.model.CountriesService
import net.cocooncreations.countriesmvvm.model.Country
import javax.inject.Inject

/**
 * ListvViewModel, class is reponsible to get commands from MainActivity and perform the screen behaviour
 * after it loads the data to screen
 */

class ListViewModel : ViewModel() {

    @Inject
    lateinit var countriesService:CountriesService
    private val disposable = CompositeDisposable()

    //MutableLiveData is provided by google, it helps to notify the subscribers of the variable when the variable values has been changed
    val countries = MutableLiveData<List<Country>>()
    val countryLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    //initializing the necessary components and classes
    init {
        DaggerApiComponent.create().inject(this)
    }
    //public refresh function it calls the fetchCountries method to perform the task
    fun refresh(){
        fetchCountries()
    }

    /**
     * Fetches the data from remote
     */
    private fun fetchCountries(){
       loading.value = true
        disposable.add(
            countriesService.getCountries().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object:DisposableSingleObserver<List<Country>> (){
                    override fun onSuccess(value: List<Country>?) {
                       countries.value = value
                       countryLoadError.value = false
                       loading.value = false
                    }

                    override fun onError(e: Throwable?) {
                        countryLoadError.value = true
                        loading.value = false
                    }

                })
        )
    }

    /**
     * build-in method of ViewModel class which is provided by Google and it clears disposable a of the cache of the device
     * prevent the memory leaks and app crashes
     */

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}