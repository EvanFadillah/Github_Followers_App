package com.ervan.github.ui.model

import androidx.lifecycle.ViewModel
import com.ervan.github.repository.FavouriteRepository

class FavouriteViewModel(private val favouriteRepository: FavouriteRepository): ViewModel() {
    fun getFavourite() = favouriteRepository.getAll()

}