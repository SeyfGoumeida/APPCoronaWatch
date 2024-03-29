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
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.map.*


class MapFragment : Fragment() , OnMapReadyCallback {
    private val compositeDisposable = CompositeDisposable()
    lateinit var jsonAPI: IAPI
    lateinit var deathButton : Button
    lateinit var recoveredButton : Button
    lateinit var confirmedButton : Button
    lateinit var suspectedButton : Button
    lateinit var infectedTextView : TextView
    lateinit var recovredTextView : TextView
    lateinit var deathTextView : TextView
    lateinit var suspectedTextView : TextView
    lateinit var countryName : TextView
    lateinit var selectCountry: Spinner
    lateinit var mMap : GoogleMap
    lateinit var statistics :View
    var countries: Countries = Countries()
    private var stat = Stats(0,0,0,0,0)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.map, null)
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync {
            mMap = it
            onMapReady(mMap)

        }
        selectCountry = view.findViewById(R.id.selectCountryspinner) as Spinner
        selectCountry.visibility=View.GONE
        statistics = view.findViewById<View>(R.id.popupLayout)
        countryName = view.findViewById(R.id.countryTextView)
        infectedTextView  = view.findViewById(R.id.infectedTextView)
        recovredTextView = view.findViewById(R.id.recovredTextView)
        deathTextView  = view.findViewById(R.id.deathTextView)
        suspectedTextView = view.findViewById(R.id.suspectedTextView)
        val fermer = view.findViewById<View>(R.id.fermerButton)
        fermer.setOnClickListener {
            statistics.visibility = View.GONE
        }
        //--------------------
        val retrofit = RetrofitClient.instance
        jsonAPI = retrofit.create(IAPI::class.java)
        deathButton = view.findViewById<Button>(R.id.deathButton)
        suspectedButton= view.findViewById<Button>(R.id.susprectedButton)
        confirmedButton= view.findViewById<Button>(R.id.confirmedButton)
        recoveredButton= view.findViewById<Button>(R.id.recoveredButton)
        confirmedButton.setOnClickListener{
            mMap.clear()
            onMapReady(mMap)
            fetchregions(56)
            fetchCountriesStats("Confirmed")
            fetchCountryStats("Confirmed",56,getLat(56),getLng(56))
            Toast.makeText(context, " ...جاري تحميل الخريطة", Toast.LENGTH_LONG).show()
            confirmedButton.visibility=View.INVISIBLE
            suspectedButton.visibility=View.VISIBLE
            deathButton.visibility=View.VISIBLE
            recoveredButton.visibility=View.VISIBLE
        }
        suspectedButton.setOnClickListener{
            mMap.clear()
            onMapReady(mMap)
            fetchregions(56)
            fetchCountriesStats("Suspected")
            fetchCountryStats("Suspected",56,getLat(56),getLng(56))
            Toast.makeText(context, "...جاري تحميل الخريطة", Toast.LENGTH_LONG).show()
            confirmedButton.visibility=View.VISIBLE
            suspectedButton.visibility=View.INVISIBLE
            deathButton.visibility=View.VISIBLE
            recoveredButton.visibility=View.VISIBLE
        }
        deathButton.setOnClickListener{
            mMap.clear()
            onMapReady(mMap)
            fetchregions(56)
            fetchCountriesStats("Death")
            fetchCountryStats("Death",56,getLat(56),getLng(56))
            Toast.makeText(context, "...جاري تحميل الخريطة", Toast.LENGTH_LONG).show()
            confirmedButton.visibility=View.VISIBLE
            suspectedButton.visibility=View.VISIBLE
            deathButton.visibility=View.INVISIBLE
            recoveredButton.visibility=View.VISIBLE
        }
        recoveredButton.setOnClickListener{
            mMap.clear()
            onMapReady(mMap)
            fetchregions(56)
            fetchCountriesStats("Recovered")
            fetchCountryStats("Recovered",56,getLat(56),getLng(56))
            Toast.makeText(context, "...جاري تحميل الخريطة", Toast.LENGTH_LONG).show()
            confirmedButton.visibility=View.VISIBLE
            suspectedButton.visibility=View.VISIBLE
            deathButton.visibility=View.VISIBLE
            recoveredButton.visibility=View.INVISIBLE

        }
        return view
    }
    override fun onMapReady(googlemap: GoogleMap?) {

        if (googlemap != null) {
            mMap = googlemap
        }
        try {
            val success: Boolean = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    activity, R.raw.maptheme
                )
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
        getCountries()
        mMap.setOnCircleClickListener{ mCircle ->
            val mId= getCountryId(mCircle.center.latitude,mCircle.center.longitude)
            getCountryStats(mId,mCircle)

        }
    }
    private fun fetchregions(idCountry:Int) {

      compositeDisposable.add( jsonAPI.getCountryRigions(idCountry)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe (
              { regions ->
                  circle("Recovered",mMap, regions)
              } ,
              { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
              }
            )
      )

   }
    private fun fetchCountriesStats(type:String) {
        compositeDisposable.add( jsonAPI.getcountries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { countries ->
                    for (country in countries) {
                        fetchCountryStats(type,country.id, country.latitude, country.longitude)
                    }
                } ,
                { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                }
            )
        )
    }
    private fun fetchCountryStats(type :String,idCountry:Int,latitude:Double,longitude:Double) {
                    compositeDisposable.add( jsonAPI.getCountryStatistics(idCountry)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe (
                            { stats ->
                                circle(type,mMap,stats,latitude,longitude)
                            } ,
                            { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                            }
                        )
                    )

    }
    private fun circle(type : String,googlemap: GoogleMap? , regions : Regions) {
        if (googlemap != null) {
            mMap = googlemap
        }
        for (regionItem in regions) {

            val location = LatLng(regionItem.region.latitude.toDouble(), regionItem.region.longitude.toDouble())
            var death = regionItem.nb_death.toDouble()
            death= circleSizeRegion(death)
            var recovred = regionItem.nb_recovered.toDouble()
            recovred= circleSizeRegion(recovred)
            var confirmed = regionItem.nb_confirme.toDouble()
            confirmed= circleSizeRegion(confirmed)
            var suspected = regionItem.nb_suspected.toDouble()
            suspected= circleSizeRegion(suspected)
            if(type=="Death"){
                mMap.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(death)
                        .strokeWidth(3f)
                        .strokeColor(Color.argb(100, 255, 0, 0))
                        .fillColor(Color.argb(50, 255, 0, 0))
                        .clickable(true)
                )
            }else if (type=="Recovered"){
                mMap.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(recovred)
                        .strokeWidth(3f)
                        .strokeColor(Color.argb(100, 0, 165, 99))
                        .fillColor(Color.argb(50, 0, 165, 99))
                        .clickable(true)
                )
            }else if (type=="Confirmed"){
                mMap.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(confirmed)
                        .strokeWidth(3f)
                        .strokeColor(Color.argb(100, 186, 3, 107))
                        .fillColor(Color.argb(50, 186, 3, 107))
                        .clickable(true)
                )
            }else if (type=="Suspected"){
                mMap.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(suspected)
                        .strokeWidth(3f)
                        .strokeColor(Color.argb(100, 255, 145, 0))
                        .fillColor(Color.argb(50, 255, 145, 0))
                        .clickable(true)
                )
            }
        }

    }
    private fun circle(type : String,googlemap: GoogleMap? , stats : Stats,latitude : Double ,longitude : Double) {
        if (googlemap != null) {
            mMap = googlemap
        }
        val location = LatLng(latitude, longitude)
        var death = stats.nb_death__sum.toDouble()
            death= circleSize(death)
        var recovered = stats.nb_recovered__sum.toDouble()
        recovered= circleSize(recovered)
        var confirmed = stats.nb_confirmed__sum.toDouble()
        confirmed= circleSize(confirmed)
        var suspected = stats.nb_suspected__sum.toDouble()
        suspected= circleSize(suspected)
        when (type) {
            "Death" -> {
                var circle=mMap.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(death)
                        .strokeWidth(3f)
                        .strokeColor(Color.argb(100, 255, 0, 0))
                        .fillColor(Color.argb(50, 255, 0, 0))
                        .clickable(true)
                )
                circle.tag=location
            }
            "Recovered" -> {
                val circle= mMap.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(recovered)
                        .strokeWidth(3f)
                        .strokeColor(Color.argb(100, 0, 165, 99))
                        .fillColor(Color.argb(50, 0, 165, 99))
                        .clickable(true)
                )
                circle.tag=location
            }
            "Confirmed" -> {
                val circle=mMap.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(confirmed)
                        .strokeWidth(3f)
                        .strokeColor(Color.argb(100, 186, 3, 107))
                        .fillColor(Color.argb(50, 186, 3, 107))
                        .clickable(true)
                )
                circle.tag=location
            }
            "Suspected" -> {
                val circle= mMap.addCircle(
                    CircleOptions()
                        .center(location)
                        .radius(suspected)
                        .strokeWidth(3f)
                        .strokeColor(Color.argb(100, 255, 145, 0))
                        .fillColor(Color.argb(50, 255, 145, 0))
                        .clickable(true)
                )
                circle.tag=location
            }
        }
    }
    private fun circleSize(number: Double): Double {
        var mNumber = number
        if(mNumber>0) {
            if (mNumber < 1000) {
                while (mNumber < 1000) {
                    mNumber *= 10
                }
                mNumber *= 5.0
            } else if (mNumber < 10000 && mNumber > 1000) {
                while (mNumber < 10000) {
                    mNumber *= 10
                }
            } else if (mNumber < 50000 && mNumber > 10000) {
                while (mNumber < 50000) {
                    mNumber *= 10
                }
            }
        }else{mNumber=20000.0}
        return mNumber
    }
    private fun circleSizeRegion(number: Double): Double {
        var mNumber = number
        if(mNumber>0) {
            if (mNumber < 1000) {
                while (mNumber < 1000) {
                    mNumber *= 10
                }
            } else if (mNumber < 10000 && mNumber > 1000) {
                while (mNumber < 10000) {
                    mNumber *= 10
                }
            } else if (mNumber < 50000 && mNumber > 10000) {
                while (mNumber < 50000) {
                    mNumber *= 10
                }
            }
        }
        return mNumber
    }
    private fun getLat(countryId : Int)  :Double{
        var lat : Double =0.0
                    for(country in countries ) if (country.id==countryId){lat=country.latitude}
        return lat
    }
    private fun getLng(countryId : Int) :Double{
        var lng : Double =0.0
        for (country in countries)if (country.id==countryId){lng=country.longitude}
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
    private fun getCountryId(latitude : Double, longitude : Double):Int{
        var mId =0
        val country = countries.find {it.latitude.equals(latitude) && it.longitude.equals(longitude)}
        if (country != null) {
            mId=country.id
        }
        return mId
    }
    private fun getCountryName(latitude : Double, longitude : Double):String{
        var mName =""
        val country = countries.find {it.latitude.equals(latitude) && it.longitude.equals(longitude)}
        if (country != null) {
            mName=country.name
        }
        return mName
    }
    private fun getCountryStats(idCountry:Int,mCircle:Circle){
        compositeDisposable.add( jsonAPI.getCountryStatistics(idCountry)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { statistics  ->
                    infectedTextView.text= statistics.nb_confirmed__sum.toString()
                    recovredTextView.text=statistics.nb_recovered__sum.toString()
                    deathTextView.text=statistics.nb_death__sum.toString()
                    suspectedTextView.text=statistics.nb_suspected__sum.toString()
                    countryName.text=getCountryName(mCircle.center.latitude,mCircle.center.longitude)
                    popupLayout.visibility=(View.VISIBLE)
                } ,
                { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                }
            )
        )
    }

}

