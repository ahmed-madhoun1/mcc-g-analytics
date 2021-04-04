package com.example.analyticsassignment

import android.app.Activity
import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.analyticsassignment.databinding.FragmentCategoryBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

const val ELECTRONIC: Int = Activity.RESULT_CANCELED
const val FURNITURE: Int = Activity.RESULT_CANCELED + 1
const val CLOTHES: Int = Activity.RESULT_CANCELED + 2

class CategoryFragment : Fragment(R.layout.fragment_category) {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(requireContext())
    }
    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private var START_TIME: Double = 0.0
    private var LEAVE_TIME: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackLog()
    }

    override fun onResume() {
        super.onResume()
        START_TIME = System.currentTimeMillis().toDouble()
        Log.e("TAG", "CategoryFragment START_TIME: $START_TIME")
    }

    override fun onStop() {
        super.onStop()
        LEAVE_TIME = System.currentTimeMillis().toDouble()
        Log.e("TAG", "CategoryFragment LEAVE_TIME: $LEAVE_TIME")
        secondSpendInScreen = ((LEAVE_TIME - START_TIME) / 1000)
        Log.e("TAG", "CategoryFragment secondSpendInScreen: $secondSpendInScreen")

        val map = ArrayMap<String, String>()
        map["screen"] = "Category"
        map["time"] = secondSpendInScreen.toString()
        map["userId"] = USERID

        firebaseFirestore.collection("Category Screen").document(USERID).set(map).addOnSuccessListener {
            Log.e("TAG", "CategoryFragment onStop: Success")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentCategoryBinding.bind(view)
        binding.apply {
            btnElectronic.setOnClickListener {
                val action = CategoryFragmentDirections.actionCategoryFragmentToProductListFragment(
                        ELECTRONIC
                )
                findNavController().navigate(action)
            }
            btnFurniture.setOnClickListener {
                val action =
                        CategoryFragmentDirections.actionCategoryFragmentToProductListFragment(FURNITURE)
                findNavController().navigate(action)
            }
            btnClothes.setOnClickListener {
                val action =
                        CategoryFragmentDirections.actionCategoryFragmentToProductListFragment(CLOTHES)
                findNavController().navigate(action)
            }
        }
    }

    // Here Track function
    private fun trackLog() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "CategoryScreen")
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "CategoryFragment")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }
    companion object {
        var secondSpendInScreen = 0.0
    }
}