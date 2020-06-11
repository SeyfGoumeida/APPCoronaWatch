package com.example.coronawatch.Activities

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coronawatch.R
import com.example.coronawatch.Retrofit.IAPI
import com.example.coronawatch.Retrofit.RetrofitClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*


lateinit var mMap : GoogleMap

class MapFragment : Fragment() ,OnMapReadyCallback{
    private val compositeDisposable = CompositeDisposable()
    lateinit var jsonAPI: IAPI
    override  fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.map, null)
        val mapFragment : SupportMapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync {
            mMap = it
            onMapReady(mMap)
            circle(mMap)
        }
        val statistics = view.findViewById<View>(R.id.popupLayout)
        val fermer = view.findViewById<View>(R.id.fermerButton)
            fermer.setOnClickListener{
            statistics.visibility = View.GONE
            }
        //--------------------
        val retrofit = RetrofitClient.instance
        jsonAPI = retrofit.create(IAPI::class.java)
        //fetchregions()
        return view
    }

//    private fun fetchregions() {
//        compositeDisposable.add( jsonAPI.getAlgeriastatistics(56)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe{ region ->
//                var confirmed: TextView? = findViewById(R.id.article_text)
//                if (confirmed != null) {
//                    confirmed.text = region.nb_confirme.toString()
//               }
//
//            }
//        )
//
//    }
    override fun onMapReady(googlemap: GoogleMap?) {
        if (googlemap != null) {
            mMap = googlemap
        }
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
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
    }
    private fun circle(googlemap: GoogleMap?) {
        if (googlemap != null) {
            mMap = googlemap
        }

        val locationALG = LatLng(28.0339,1.6596)
        mMap.addMarker(
            MarkerOptions()
                .position(locationALG)
                .title("Hello world")
                //.visible(false)
                .flat(false)

        )
        mMap.addCircle(
            CircleOptions()
                .center(locationALG)
                .radius(732*500.0)
                .strokeWidth(3f)
                .strokeColor(Color.argb(100,255,0,0))
                .fillColor(Color.argb(50,255,0,0))
                .clickable(true)
        )
    }
}

