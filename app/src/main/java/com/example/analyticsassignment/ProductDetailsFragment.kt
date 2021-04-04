package com.example.analyticsassignment

import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.analyticsassignment.databinding.FragmentProductDetailsBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore

class ProductDetailsFragment : Fragment(R.layout.fragment_product_list) {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(requireContext())
    }
    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private var START_TIME: Double = 0.0
    private var LEAVE_TIME: Double = 0.0
    private val argument = navArgs<ProductDetailsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentProductDetailsBinding.inflate(inflater)
        binding.apply {
            textViewTitle.text = argument.value.product.name
            textViewDetails.text = argument.value.product.details
            Glide.with(requireActivity()).load(argument.value.product.image).into(imageView)
        }
        return binding.root
    }

    // Here Track function
    private fun trackLog(){
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "ProductDetailsScreen")
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ProductDetailsFragment")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW,  bundle)
    }
    override fun onResume() {
        super.onResume()
        START_TIME = System.currentTimeMillis().toDouble()
        Log.e("TAG", "ProductDetailsFragment START_TIME: $START_TIME")
    }

    override fun onStop() {
        super.onStop()
        LEAVE_TIME = System.currentTimeMillis().toDouble()
        Log.e("TAG", "ProductDetailsFragment LEAVE_TIME: $LEAVE_TIME")
        secondSpendInScreen = ((LEAVE_TIME - START_TIME) / 1000)
        Log.e("TAG", "ProductDetailsFragment secondSpendInScreen: ${secondSpendInScreen}")

        val map = ArrayMap<String, String>()
        map["screen"] = "Category"
        map["time"] = secondSpendInScreen.toString()
        map["userId"] = USERID

        firebaseFirestore.collection("Product Details Screen").document(USERID).set(map).addOnSuccessListener {
            Log.e("TAG", "ProductDetailsFragment onStop: Success")
        }

    }

    companion object {
        var secondSpendInScreen = 0.0
    }
}