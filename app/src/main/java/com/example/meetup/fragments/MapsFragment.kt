package com.example.meetup.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.meetup.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    private val args: MapsFragmentArgs by navArgs()
    private val callback = OnMapReadyCallback { googleMap ->
        val geo = args.userObject.address.geo
        val lat = geo.lat
        val lng = geo.lng
        val placeToMark = LatLng(lat , lng)
        googleMap.addMarker(MarkerOptions().position(placeToMark).title(args.userObject.name))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(placeToMark))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}