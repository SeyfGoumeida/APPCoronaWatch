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
import androidx.core.view.isVisible
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
import kotlinx.android.synthetic.main.map.*


lateinit var mMap : GoogleMap

class MapCountryFragment : Fragment() , OnMapReadyCallback {
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
    var countries: Countries = Countries()
    private lateinit var statistics :View
    var mRegions: Regions = Regions()
    private var stat = Stats(0,0,0,0,0)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.map, null)
        val mapFragment: SupportMapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync {
            mMap = it
            onMapReady(mMap)

        }
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
        confirmedButton=view.findViewById<Button>(R.id.confirmedButton)
        recoveredButton=view.findViewById<Button>(R.id.recoveredButton)
        selectCountry = view.findViewById(R.id.selectCountryspinner) as Spinner
        var countriesnames = getCountriesNames()
        for (countrie in countries)
        {
            countriesnames.add(countrie.name)
            Toast.makeText(context, "not empty", Toast.LENGTH_LONG).show()
        }
        selectCountry.adapter= ArrayAdapter<String>(context!!,android.R.layout.simple_list_item_1,countriesnames)
        selectCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(context,"اختر دولة", Toast.LENGTH_LONG).show()
            }
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                Toast.makeText(context, countriesnames[position], Toast.LENGTH_LONG).show()
            }
        }
        confirmedButton.setOnClickListener{
            mMap.clear()
            onMapReady(mMap)
            fetchregions("Confirmed",56)
            Toast.makeText(context, " ...جاري تحميل الخريطة", Toast.LENGTH_LONG).show()
            confirmedButton.visibility=View.INVISIBLE
            suspectedButton.visibility=View.VISIBLE
            deathButton.visibility=View.VISIBLE
            recoveredButton.visibility=View.VISIBLE
        }
        suspectedButton.setOnClickListener{
            mMap.clear()
            onMapReady(mMap)
            fetchregions("Suspected",56)
            Toast.makeText(context, "...جاري تحميل الخريطة", Toast.LENGTH_LONG).show()
            confirmedButton.visibility=View.VISIBLE
            suspectedButton.visibility=View.INVISIBLE
            deathButton.visibility=View.VISIBLE
            recoveredButton.visibility=View.VISIBLE
        }
        deathButton.setOnClickListener{
            mMap.clear()
            onMapReady(mMap)
            fetchregions("Death",56)
            Toast.makeText(context, "...جاري تحميل الخريطة", Toast.LENGTH_LONG).show()
            confirmedButton.visibility=View.VISIBLE
            suspectedButton.visibility=View.VISIBLE
            deathButton.visibility=View.INVISIBLE
            recoveredButton.visibility=View.VISIBLE
        }
        recoveredButton.setOnClickListener{
            mMap.clear()
            onMapReady(mMap)
            fetchregions("Recovered",56)
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
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(28.0339, 1.6596),5.0f))
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
        getCountries()
        mMap.setOnCircleClickListener{ mCircle ->
            val mId = getRegionId(mCircle.center.latitude,mCircle.center.longitude)
            getRegionStats(mId)
            infectedTextView.text= stat.nb_confirmed__sum.toString()
            recovredTextView.text=stat.nb_recovered__sum.toString()
            deathTextView.text=stat.nb_death__sum.toString()
            suspectedTextView.text=stat.nb_suspected__sum.toString()
            countryName.text=getRegionName(mId)
            popupLayout.visibility=(View.VISIBLE)
        }
    }
    private fun getCountries() {
        compositeDisposable.add( jsonAPI.getcountries()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { listcountries ->
                    countries=listcountries
                } ,
                { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                }
            )
        )

    }

    private fun getCountriesNames() :ArrayList<String>{
        var listCountriesNames : ArrayList<String> = ArrayList()
        for (countrie in countries)
             {
                 listCountriesNames.add(countrie.name)
             }
        return listCountriesNames
    }
    private fun fetchregions(type:String,idCountry:Int) {

        compositeDisposable.add( jsonAPI.getCountryRigions(idCountry)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe (
                { regions ->
                    circle(type,mMap, regions)
                } ,
                { error -> Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                }
            )
        )

    }
    private fun circle(type : String,googlemap: GoogleMap? , regions : Regions) {
        mRegions =regions
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
    private fun circleSizeRegion(number: Double): Double {
        var mNumber = number
        if(mNumber>0) {
            if (mNumber < 1000) {
                while (mNumber < 10000) {
                    mNumber *= 10
                }
            } else if (mNumber < 10000 && mNumber > 1000) {
                while (mNumber < 100000) {
                    mNumber *= 10
                }
            } else if (mNumber < 50000 && mNumber > 10000) {
                while (mNumber < 500000) {
                    mNumber *= 10
                }
            }
        }
        return mNumber
    }
    private fun getRegionId(latitude : Double, longitude : Double):Int{
        var mId =0
        val region = mRegions.find {it.region.latitude==latitude && it.region.longitude.equals(longitude)}
        if (region != null) {
            mId=region.id
        }
        return mId
    }
    private fun getRegionName(regionId:Int):String{
        var mName =""
        val region = mRegions.find {it.id==regionId}
        if (region != null) {
            mName=region.region.region_name
        }
        return mName
    }
    private fun getRegionStats(idRegion:Int) {
       for(region in mRegions){
           if (region.id==idRegion){
               val statistics = Stats(region.nb_confirme,region.nb_recovered,region.nb_notyetsick,region.nb_suspected,region.nb_confirme)
               affectStats(statistics)
           }
       }
     }
    private fun affectStats(stats: Stats){
        stat=stats
    }

}

