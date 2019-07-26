package net.cocooncreations.countriesmvvm

import android.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import net.cocooncreations.countriesmvvm.model.CountriesService
import net.cocooncreations.countriesmvvm.model.Country
import net.cocooncreations.countriesmvvm.viewmodel.ListViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class ListViewModelTest {


    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()


    private var testSingle: Single<List<Country>>? = null

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesSuccess() {
       val country = Country("countryName","capital","url")
       val countriesList = arrayListOf(country)

       testSingle = Single.just(countriesList)

      `when`(countriesService.getCountries()).thenReturn(testSingle)

      listViewModel.refresh()

      Assert.assertEquals(1,listViewModel.countries.value?.size)

      Assert.assertEquals(false,listViewModel.countryLoadError.value)

      Assert.assertEquals(false,listViewModel.loading.value)

    }

    @Test
    fun getCountriesFail(){

        testSingle = Single.error(Throwable())

        `when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(true,listViewModel.countryLoadError.value)

        Assert.assertEquals(false,listViewModel.loading.value)

    }

    @Before
    fun setUpRxSchedulers(){
        val immidiate = object: Scheduler() {
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        RxJavaPlugins.setInitIoSchedulerHandler{scheduler: Callable<Scheduler>? -> immidiate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler: Callable<Scheduler>? -> immidiate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler: Callable<Scheduler>? ->  immidiate}
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler: Callable<Scheduler>? -> immidiate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler>? -> immidiate }

    }

}