package com.example.analyticsassignment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String,
    val image: String,
    val name: String,
    val details: String
) : Parcelable
