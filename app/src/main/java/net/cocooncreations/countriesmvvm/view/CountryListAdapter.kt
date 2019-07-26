package net.cocooncreations.countriesmvvm.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_country.view.*
import net.cocooncreations.countriesmvvm.R
import net.cocooncreations.countriesmvvm.model.Country
import net.cocooncreations.countriesmvvm.util.getProgressDrawable
import net.cocooncreations.countriesmvvm.util.loadImage

class CountryListAdapter(var countries:ArrayList<Country>):RecyclerView.Adapter<CountryListAdapter.CountryViewHolder>() {


    fun updateCountries(newCountries:List<Country>){
        countries.clear()
        countries.addAll(newCountries)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_country,parent,false))

    override fun getItemCount() = countries.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }


    class CountryViewHolder(view: View):RecyclerView.ViewHolder(view){

        private val countryName = view.name
        private val imageView = view.imageView
        private val countryCapital = view.capital
        private val progressDrawable  = getProgressDrawable(view.context)

        fun bind(country:Country){
            countryName.text = country.countryName
            countryCapital.text = country.capital
            imageView.loadImage(country.flag,progressDrawable)

        }
    }

}