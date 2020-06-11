package com.example.coronawatch.Activities

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coronawatch.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager


lateinit var mMap : GoogleMap
class MapFragment : Fragment() ,OnMapReadyCallback{

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View = inflater?.inflate(R.layout.map, null)
        var mapFragment : SupportMapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync {
            mMap = it
            onMapReady(mMap)
            circle(mMap)
        }

        return view
    }

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

