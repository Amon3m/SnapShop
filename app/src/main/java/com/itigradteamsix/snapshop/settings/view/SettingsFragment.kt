package com.itigradteamsix.snapshop.settings.view

import ApiDataSource
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.FragmentSettingsBinding
import com.itigradteamsix.snapshop.settings.currency.CurrencyApiClient
import com.itigradteamsix.snapshop.settings.currency.CurrencyEndPoints
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils.getAllCountries
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils.getCountryCode
import com.itigradteamsix.snapshop.settings.currency.CurrencyUtils.getCurrencyCode
import com.itigradteamsix.snapshop.settings.currency.MainRepo
import com.itigradteamsix.snapshop.settings.currency.Utility
import com.itigradteamsix.snapshop.settings.currency.Utility.withColor
import com.itigradteamsix.snapshop.settings.viewmodel.SettingsViewModel
import com.itigradteamsix.snapshop.settings.viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewmodel: SettingsViewModel
    private var selectedCurrency: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferenceWrapper = (activity?.application as MyApplication).settingsStore



        viewmodel = ViewModelProvider(
            requireActivity(),
            SettingsViewModelFactory(preferenceWrapper, MainRepo(ApiDataSource(CurrencyApiClient.getApiService())))
        )[SettingsViewModel::class.java]



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewmodel.currentLanguage.collectLatest {
                binding.languageButton.text = it
            }
        }

        binding.languageButton.setOnClickListener {
            showLanguagePopup(it, com.itigradteamsix.snapshop.R.menu.lang_popup_menu)
        }

//        binding.currencyButton.setOnClickListener {
//            viewmodel.changeCurrency("EUR")
//        }

        lifecycleScope.launch {
            viewmodel.userPreferencesFlow.collectLatest {
                //log the value of the flow
                Log.d("SettingsFragment", "onViewCreated USERPREFS: $it")
            }
        }

        observeCurrency()
//        initCurrencySpinner()


    }

    private fun observeCurrency() {
        lifecycleScope.launch {
            viewmodel.currencyPreferencesFlow.collectLatest {currencyPrefs ->

                //set the currency code in the spinner
//                binding.currencyButton.text = it.currencyCode
                binding.currencySpinner.text = currencyPrefs.currencyCode

                val flagUrl = "https://www.flagsapi.com/${currencyPrefs.countryCode}/flat/64.png"
                Glide.with(requireActivity()).load(flagUrl).into(binding.flagImageView)

                binding.currencySpinner.setOnClickListener {
                    initCurrencySpinner(currencyPrefs.countryCode)
                }

            }
        }

    }


    private fun initCurrencySpinner(countryyCode: String) {


        //get first spinner country reference in view
        val spinner1 = binding.currencySpinner


        //set items in the spinner i.e a list of all countries

        val allCountries = getAllCountries()
//        val index = allCountries.indexOf(getCountryCode(currencyCode))
        spinner1.setItems(allCountries)
        //select the country with the country code passed in
        val index = allCountries.first { getCountryCode(it)?.lowercase() == countryyCode.lowercase() }
        spinner1.selectedIndex = allCountries.indexOf(index)






        //Handle selected item, by getting the item and storing the value in a  variable - selectedCurrency
        spinner1.setOnItemSelectedListener { view, position, id, item ->
            //Set the currency code for each country as hint
            val countryCode = getCountryCode(item.toString())
            val currencySymbol = getCurrencyCode(countryCode)
            selectedCurrency = currencySymbol

            //check if internet is available
            if (!Utility.isNetworkAvailable(requireActivity())) {
                Snackbar.make(
                    binding.root,
                    "You are not connected to the internet",
                    Snackbar.LENGTH_LONG
                )
                    .withColor(ContextCompat.getColor(MyApplication.appContext, R.color.md_theme_dark_errorContainer))
                    .setTextColor(ContextCompat.getColor(MyApplication.appContext, R.color.white))
                    .show()
            }

            //carry on and convert the value
            else {
                doConversion(countryCode)
            }



        }

        spinner1.setOnNothingSelectedListener {
            spinner1.text = getCurrencyCode(countryyCode)


        }




    }


    private fun doConversion(countryCode: String?){

        //Get the data inputed
        val apiKey = CurrencyEndPoints.API_KEY
        val from = "USD"
        val to = selectedCurrency.toString()
        val amount = 1.0


        //do the conversion
        viewmodel.getConvertedData(apiKey, from, to, amount,countryCode!!)


    }



    private fun showLanguagePopup(view: View, menuRes: Int) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                com.itigradteamsix.snapshop.R.id.english -> {
                    viewmodel.changeLanguage(getString(com.itigradteamsix.snapshop.R.string.english))
                    true
                }

                com.itigradteamsix.snapshop.R.id.arabic -> {
                    viewmodel.changeLanguage(getString(R.string.arabic))
                    true
                }

                else -> false
            }
        }
        popup.show()
    }


}