package net.cocooncreations.countriesmvvm.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import net.cocooncreations.countriesmvvm.R
import net.cocooncreations.countriesmvvm.viewmodel.ListViewModel

class MainActivity : AppCompatActivity(),SwipeRefreshLayout.OnRefreshListener {


    lateinit var viewModel:ListViewModel
    private var countriesAdapter:CountryListAdapter = CountryListAdapter(arrayListOf())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        instantiateTheViewModel()
    }

    /**
     * Instantiates the view model class
     * Triggers the fetch data from remote method
     */
    private fun instantiateTheViewModel(){
        viewModel  = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()

        countriesList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = countriesAdapter
        }

        //set on refresh listener
        swipeRefreshLayout.setOnRefreshListener(this)

        //observe the view model if there are changes
        observeViewModel()
    }

    /**
    * ObserveModel method listens for changes on fetched data
     *  if there is an error
     *  @property countryLoadError is called to show error message
     *  @property countries is called when data is succesfully observed and loaded from remote or local
     *  @property loading defines whether to show loading spinner or hide it
    * */
    fun observeViewModel(){
        viewModel.countries.observe(this, Observer {countries->
            countries?.let{list->
                countriesList.visibility = View.VISIBLE
                countriesAdapter.updateCountries(list)
            }
        })

        viewModel.countryLoadError.observe(this, Observer {isError->
            isError?.let { list_error.visibility = if(it) View.VISIBLE else View.GONE }
        })

        viewModel.loading.observe(this, Observer { isLoading->
            isLoading?.let {
                loading_view.visibility = if(it) View.VISIBLE else View.GONE
                if(it){
                    list_error.visibility = View.GONE
                    countriesList.visibility = View.GONE
                }
            }
        })
    }

    /**
     * handles the swipe and refresh function
     */

    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = false
        viewModel.refresh()
    }
}
