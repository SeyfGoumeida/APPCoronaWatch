package com.example.coronawatch.Activities

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.coronawatch.R
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.expressions.Expression.*
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.layers.TransitionOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlinx.android.synthetic.main.map.*
import java.net.URI
import java.net.URISyntaxException
import com.mapbox.mapboxsdk.style.expressions.Expression.exponential
import com.mapbox.mapboxsdk.style.expressions.Expression.get
import com.mapbox.mapboxsdk.style.expressions.Expression.interpolate
import com.mapbox.mapboxsdk.style.expressions.Expression.stop
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.circleRadius


class MapFragment : Fragment() {

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        // Mapbox access token is configured here. context!! needs to be called either in your application
        // object or in the same activity which contains the mapview.
        context?.let { Mapbox.getInstance(it, getString(R.string.access_token)) }

        var view = inflater!!.inflate(R.layout.map, container, false)

        mapView = view.findViewById( R.id.mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { map ->
            mapboxMap = map
            map.setStyle(Style.DARK) { style ->
                // Disable any type of fading transition when icons collide on the map. context!! enhances the visual
                // look of the data clustering together and breaking apart.
                style.transition = TransitionOptions(0, 0, false)
                mapboxMap!!.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(28.0339, 1.6596),
                        3.0
                    )
                )
                addClusteredGeoJsonSource(style)

            }
        }

        return view

    }


    private fun addClusteredGeoJsonSource(@NonNull loadedMapStyle: Style) {

        // Add a new source from the GeoJSON data and set the 'cluster' option to true.
        try {
            loadedMapStyle.addSource(
                //-----------------------------------------------
               // I want to link out Json file in here so i can use it
                //------------------------------------------------------
                GeoJsonSource("infected", URI("https://www.mapbox.com/mapbox-gl-js/assets/earthquakes.geojson"),
                    GeoJsonOptions()
                        .withCluster(true)
                        .withClusterMaxZoom(14)
                        .withClusterRadius(50)
                )
            )
        } catch (uriSyntaxException: URISyntaxException) {
            Toast.makeText(context,"Error while loading Json",Toast.LENGTH_SHORT).show()
        }

        // Use the earthquakes GeoJSON source to create three layers: One layer for each cluster category.
        // Each point range gets a different fill color.
        val layers = arrayOf(
            intArrayOf(150, ContextCompat.getColor(context!!, R.color.mapbox_pink)),
            intArrayOf(20, ContextCompat.getColor(context!!, R.color.mapbox_blue)),
            intArrayOf(0, ContextCompat.getColor(context!!, R.color.mapbox_green))
        )
        for (i in layers.indices) {
            val pointCount: Expression = toNumber(get("point_count"))
            Toast.makeText(context!!,pointCount.toString(),Toast.LENGTH_SHORT).show()

            //Add clusters' circles
            val circles = CircleLayer("cluster-$i", "infected")
            circles.setProperties(
                circleColor(layers[i][1]),
                circleRadius(18f)
                //circleRadius(pointCount) raduis depends on the count
            )


            // Add a filter to the cluster layer that hides the circles based on "point_count"
            circles.setFilter(
                if (i == 0) all(
                    has("point_count"),
                    gte(pointCount, literal(layers[i][0]))
                ) else all(
                    has("point_count"),
                    gte(pointCount, literal(layers[i][0])),
                    lt(pointCount, literal(layers[i - 1][0]))
                )
            )
            loadedMapStyle.addLayer(circles)
        }

        //Add the number of infected insie the circle
        val count = SymbolLayer("count", "infected")
        count.setProperties(
            textField(Expression.toString(get("point_count"))),
            textSize(12f),
            textColor(Color.WHITE),
            textIgnorePlacement(true),
            textAllowOverlap(true)
        )
        loadedMapStyle.addLayer(count)
    }

    public override fun onStart() {
        super.onStart()
        mapView!!.onStart()
    }

    public override fun onResume() {
        super.onResume()
        mapView!!.onResume()
    }

    public override fun onPause() {
        super.onPause()
        mapView!!.onPause()
    }

    public override fun onStop() {
        super.onStop()
        mapView!!.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView!!.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView!!.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView!!.onSaveInstanceState(outState)
    }
}

