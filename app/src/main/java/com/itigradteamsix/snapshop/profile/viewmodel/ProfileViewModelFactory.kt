package com.itigradteamsix.snapshop.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.itigradteamsix.snapshop.model.RepoInterface

class ProfileViewModelFactory(private val _repo: RepoInterface): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass::class.java.isInstance(ProfileViewModel::class.java)) {
                ProfileViewModel(_repo) as T
            } else {
                throw IllegalArgumentException("View Model class not found")
            }
        }

}