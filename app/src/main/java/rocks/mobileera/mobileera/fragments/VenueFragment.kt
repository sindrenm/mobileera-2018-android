package rocks.mobileera.mobileera.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_venue.view.*

import rocks.mobileera.mobileera.R

class VenueFragment : BaseFragment() {

    private val venueCoordinate = LatLng(59.910142, 10.725090)
    private val partyVenueCoordinate = LatLng(59.9105716, 10.7262615)

    var venueGoogleMap: GoogleMap? = null
    var partyVenueGoogleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_venue, container, false)

        view.venueMapView.onCreate(savedInstanceState)
        view.venueMapView.onResume()

        view.partyVenueMapView.onCreate(savedInstanceState)
        view.partyVenueMapView.onResume()

        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            print(e)
        }

        view.venueMapView.getMapAsync {venueMap ->
            venueGoogleMap = venueMap
            venueGoogleMap?.addMarker(MarkerOptions().position(venueCoordinate).title("Felix Konferansesenter"))
            venueGoogleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition.builder().target(venueCoordinate).zoom(17f).build()))
        }

        view.partyVenueMapView.getMapAsync {partyVenueMap ->
            partyVenueGoogleMap = partyVenueMap
            partyVenueGoogleMap?.addMarker(MarkerOptions().position(partyVenueCoordinate).title("Beer Palace"))
            partyVenueGoogleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(
                    CameraPosition.builder().target(partyVenueCoordinate).zoom(17f).build()))
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_venue, menu)
    }
}
