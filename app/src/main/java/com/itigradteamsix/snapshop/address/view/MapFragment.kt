package com.itigradteamsix.snapshop.address.view

import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavArgs
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.address.viewmodel.AddressViewModel
import com.itigradteamsix.snapshop.address.viewmodel.AddressViewModelFactory
import com.itigradteamsix.snapshop.authentication.FirebaseRepo
import com.itigradteamsix.snapshop.authentication.login.model.ApiDraftLoginState
import com.itigradteamsix.snapshop.data.models.Address
import com.itigradteamsix.snapshop.data.models.AddressBody
import com.itigradteamsix.snapshop.database.ConcreteLocalSource
import com.itigradteamsix.snapshop.databinding.FragmentAddressBinding
import com.itigradteamsix.snapshop.databinding.FragmentOrdersBinding
import com.itigradteamsix.snapshop.favorite.model.LineItems
import com.itigradteamsix.snapshop.favorite.view.FavoriteAdapter
import com.itigradteamsix.snapshop.favorite.viewmodel.FavoriteViewModel
import com.itigradteamsix.snapshop.favorite.viewmodel.FavoriteViewModelFactory
import com.itigradteamsix.snapshop.model.Repository
import com.itigradteamsix.snapshop.network.ApiClient
import com.itigradteamsix.snapshop.network.ApiState
import kotlinx.coroutines.launch
import java.util.Locale


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private var currentMarker: Marker? = null
    private var floatingActionButton : FloatingActionButton? = null
    var longitude : Double =0.0
    var latitude : Double=0.0
    private lateinit var binding: FragmentOrdersBinding
    private lateinit var viewModel: AddressViewModel
    private  var customerID :Long = 0



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.mapView)
        floatingActionButton = view.findViewById(R.id.floatingActionButton)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        val  addressViewModelFactory= AddressViewModelFactory(
            Repository.getInstance(ApiClient, ConcreteLocalSource(requireContext()))

        )
        viewModel = ViewModelProvider(this,addressViewModelFactory)[AddressViewModel::class.java]


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args : MapFragmentArgs by navArgs()
        customerID = args.customerId
        floatingActionButton?.setOnClickListener(View.OnClickListener {
//            goToHome(longitude,latitude)
            var address = getAddressFromLatLng(latitude,longitude)
            viewModel.addNewAddress(customerID.toString(),requireContext(), AddressBody(address))
        })
        viewLifecycleOwner.lifecycleScope.launch {

            viewModel.newAddressFlow.collect { result ->
                when (result) {

                    is ApiState.Loading -> {
                        Log.d("FlowCollect", "Loading")
                    }

                    is ApiState.Success<*> -> {
                        //navigate

                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_mapFragment_to_addressFragment)

                    }

                    is ApiState.Failure -> {
                        Log.d("FlowCollect", result.msg.toString())
                    }
                }

            }
        }
    }



    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap?.setOnMapClickListener(this)
    }

    override fun onMapClick(latLng: LatLng) {
        if(floatingActionButton?.isVisible == false)
            floatingActionButton?.visibility = View.VISIBLE
        latitude = latLng.latitude
        longitude = latLng.longitude
        Log.d("lat",longitude.toString())
        Log.d("lat",latitude.toString())
        currentMarker?.remove()
        currentMarker = googleMap?.addMarker(MarkerOptions().position(latLng))


    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
//        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    fun goToAddressFragment(long:String,lat:String) {


    }
    fun getAddressFromLatLng(latitude: Double, longitude: Double,language: String = "en"): Address {
        var geocoder = Geocoder(requireContext(), Locale.getDefault())
        if(language.equals("ar")) {
            geocoder = Geocoder(requireContext(), Locale("ar")) // Specify Arabic locale
        }
        var country: String? = null
        var city: String? = ""
        var city2: String? = ""
        var city3: String? = ""

        try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    Log.d("full adressList" , addresses.toString())
                    val address = addresses[0]
                    country = address.countryName

                    city = if(address.adminArea.isBlank()){
                        ""
                    } else{
                        address.adminArea.plus("  ")

                    }
                    city2 = if(address.locality.isBlank()){
                        ""
                    } else{
                        address.locality.plus("  ")

                    }


                    city3 =address.subLocality
                    Log.d("adress",country+"-"+city+"-"+city2+"-"+city3+"-")

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Address(country = country, city = city, address1 = city2 , customer_id = customerID)

    }


}
