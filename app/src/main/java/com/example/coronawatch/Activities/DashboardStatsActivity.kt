package com.example.coronawatch.Activities

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.coronawatch.DataClases.Countries
import com.example.coronawatch.DataClases.Regions
import com.example.coronawatch.DataClases.Stats
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dashboard.*
import kotlinx.android.synthetic.main.map.*

class DashboardStatsFragment: Fragment() {
    private val compositeDisposable = CompositeDisposable()
    lateinit var confirmedTextView : TextView
    lateinit var recovredTextView : TextView
    lateinit var deathTextView : TextView
    lateinit var suspectedTextView: TextView
    lateinit var notyetsickTextView: TextView
    var countries = Countries()
    lateinit var jsonAPI: IAPI
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.dashboard, null)
        val retrofit = RetrofitClient.instance
        jsonAPI = retrofit.create(IAPI::class.java)
        getCountries()
        fetchWorldStats(view)
        return view
    }

    private fun getLat(countryName : String, countries: Countries)  :Double{
        var lat : Double =0.0
        for(country in countries ) if (country.name == countryName){lat=country.latitude}
        return lat
    }
    private fun getLng(countryName : String, countries: Countries) :Double{
        var lng : Double =0.0
        for (country in countries)if (country.name == countryName){lng=country.longitude}
        return lng
    }
    private fun getCountries() {
        compositeDisposable.add( jsonAPI.getcountries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { listcountries -> countries=listcountries
                } ,
                { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                }
            )
        )
    }
    private fun fetchWorldStats(view: View) {
        compositeDisposable.add( jsonAPI.getWorldStatistics()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { stats ->
                    deathTextView = view.findViewById(R.id.deathTextView)
                    confirmedTextView = view.findViewById(R.id.confirmedTextView)
                    recovredTextView= view.findViewById(R.id.recoveredTextView)
                    suspectedTextView = view.findViewById(R.id.suspectedTextView)
                    notyetsickTextView= view.findViewById(R.id.notyetsickTextView)
                    deathTextView.text=stats.nb_death__sum.toString()
                    confirmedTextView.text=stats.nb_confirmed__sum.toString()
                    recovredTextView.text=stats.nb_recovered__sum.toString()
                    notyetsickTextView.text=stats.nb_notyetsick__sum.toString()
                    suspectedTextView.text=stats.nb_suspected__sum.toString()
                } ,
                { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                }
            )
        )
    }

}

