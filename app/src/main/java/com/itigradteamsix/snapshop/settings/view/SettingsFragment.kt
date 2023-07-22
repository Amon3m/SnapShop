package com.itigradteamsix.snapshop.settings.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.itigradteamsix.snapshop.MyApplication
import com.itigradteamsix.snapshop.R
import com.itigradteamsix.snapshop.databinding.FragmentSettingsBinding
import com.itigradteamsix.snapshop.settings.viewmodel.SettingsViewModel
import com.itigradteamsix.snapshop.settings.viewmodel.SettingsViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewmodel: SettingsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferenceWrapper = (activity?.application as MyApplication).settingsStore
        viewmodel = ViewModelProvider(
            requireActivity(),
            SettingsViewModelFactory(preferenceWrapper)
        )[SettingsViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSettingsBinding.inflate(inflater,container,false)
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
            showLanguagePopup(it, R.menu.lang_popup_menu)
        }

        binding.currencyButton.setOnClickListener {
            viewmodel.changeCurrency("EUR")
        }

        lifecycleScope.launch {
            viewmodel.userPreferencesFlow.collectLatest {
                //log the value of the flow
                Log.d("SettingsFragment", "onViewCreated USERPREFS: $it")
            }
        }


    }

    private fun showLanguagePopup(view: View, menuRes: Int) {
        val popup = PopupMenu(requireContext(), view)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.english -> {
                    viewmodel.changeLanguage(getString(R.string.english))
                    true
                }
                R.id.arabic -> {
                    viewmodel.changeLanguage(getString(R.string.arabic))
                    true
                }
                else -> false
            }
        }
        popup.show()
    }


}