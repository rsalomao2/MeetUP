package com.example.meetup.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.meetup.R
import com.example.meetup.databinding.FragmentUserDetailBinding
import java.util.*

class UserDetailFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentUserDetailBinding
    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_user_detail, container, false
        )
        setupUi()
        setupListener()
        setupToolbar()
        return binding.root
    }


    private fun setupUi() {
        val addressObject = args.userObject.address
        val userObject = args.userObject

        binding.apply {
            userDetailZipCodeTextView.text = getString(R.string.zipCodeLabel, addressObject.zipcode)
            userDetailSuiteTextView.text = getString(R.string.suiteLabel, addressObject.suite)
            userDetailCityTextView.text = getString(R.string.cityLabel, addressObject.city)
            userDetailStreetTextView.text = getString(R.string.streetLabel, addressObject.street)

            userDetailUserNameTextView.text = userObject.username
            userDetailEmailTextView.text = userObject.email
            userDetailIdTextView.text = userObject.id
            userDetailPhoneNumberTextView.text = userObject.phone
            userDetailWebsiteTextView.text = userObject.website
            userDetailWebsiteTextView.text = userObject.website
        }
        val userUrl = userObject.imageUrl
        Glide.with(requireContext())
            .load(userUrl)
            .into(binding.expandedImage)
    }

    private fun setupListener() {
        //setupMap()
        setupDateTimePickerBtn()
    }

    private fun setupDateTimePickerBtn() {
        binding.datePickerBtn.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val month = cal.get(Calendar.MONTH)
            val year = cal.get(Calendar.YEAR)
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }
    }

    // private fun setupMap() {
    // val geoObject = args.userObject.address.geo
    // val action =
    //    UsersDetailFragmentDirections.actionUsersDetailFragmentToMapsFragment(geoObject)
    // binding.mapBtn.setOnClickListener {
    //     findNavController().navigate(action)
    // }
    // }

    private fun setupToolbar() {
        binding.collapsingToolbarLayout.title = args.userObject.name
        binding.userDetailToolbar.apply {
            setupWithNavController(findNavController())
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val cal: Calendar = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR)
        val minute = cal.get(Calendar.MINUTE)
        val savedDay = dayOfMonth.toString()
        val savedMonth = month.toString()
        val savedYear = year.toString()
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
        binding.datePickerText.text =
            getString(R.string.dateMessage, savedDay, savedMonth, savedYear)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val savedHour = hourOfDay.toString()
        val savedMinute = minute.toString()
        binding.timePickerText.text =
            getString(R.string.timeMessage, savedHour, savedMinute)
    }
}
