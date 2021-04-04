package com.example.analyticsassignment

import android.os.Bundle
import android.util.ArrayMap
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.analyticsassignment.databinding.FragmentProductListBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.CoroutineContext

class ProductListFragment : Fragment(R.layout.fragment_product_list),
        ProductAdapter.OnItemClickListener {

    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(requireContext())
    }
    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    private var START_TIME: Double = 0.0
    private var LEAVE_TIME: Double = 0.0
    private val argument = navArgs<ProductListFragmentArgs>()

    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onResume() {
        super.onResume()
        START_TIME = System.currentTimeMillis().toDouble()
        Log.e("TAG", "ProductListFragment START_TIME: $START_TIME")
    }

    override fun onStop() {
        super.onStop()
        LEAVE_TIME = System.currentTimeMillis().toDouble()
        Log.e("TAG", "ProductListFragment LEAVE_TIME: $LEAVE_TIME")
        secondSpendInScreen += ((LEAVE_TIME - START_TIME) / 1000)
        Log.e("TAG", "ProductListFragment secondSpendInScreen: $secondSpendInScreen")

        val map = ArrayMap<String, String>()
        map["screen"] = "Category"
        map["time"] = secondSpendInScreen.toString()
        map["userId"] = USERID

        firebaseFirestore.collection(" Product List Screen").document(USERID).set(map).addOnSuccessListener {
            Log.e("TAG", "ProductListFragment onStop: Success")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val binding = FragmentProductListBinding.bind(view)

        val productAdapter = ProductAdapter(requireContext(), this)

        binding.apply {
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
            binding.recyclerView.adapter = productAdapter
            when (argument.value.categoryType) {
                ELECTRONIC -> {
                    title.text = "Electronic"
                    CoroutineScope(IO).launch {
                        val productList = ArrayList<Product>()
                        val data = firestoreInstance.collection("Electronic").get().await()
                        for (document in data) {
                            productList.add(
                                    Product(
                                            id = document.getString("id")!!,
                                            image = document.getString("image")!!,
                                            name = document.getString("name")!!,
                                            details = document.getString("details")!!,
                                    )
                            )
                        }
                        productAdapter.submitList(productList)
                    }

                }
                FURNITURE -> {
                    title.text = "Furniture"
                    CoroutineScope(IO).launch {
                        val productList = ArrayList<Product>()
                        val data = firestoreInstance.collection("Furniture").get().await()
                        for (document in data) {
                            productList.add(
                                    Product(
                                            id = document.getString("id")!!,
                                            image = document.getString("image")!!,
                                            name = document.getString("name")!!,
                                            details = document.getString("details")!!,
                                    )
                            )
                        }
                        productAdapter.submitList(productList)
                    }
                }
                CLOTHES -> {
                    title.text = "Clothes"
                    CoroutineScope(IO).launch {
                        val productList = ArrayList<Product>()
                        val data = firestoreInstance.collection("Clothes").get().await()
                        for (document in data) {
                            productList.add(
                                    Product(
                                            id = document.getString("id")!!,
                                            image = document.getString("image")!!,
                                            name = document.getString("name")!!,
                                            details = document.getString("details")!!,
                                    )
                            )
                        }
                        productAdapter.submitList(productList)
                    }
                }
            }
        }


    }

    override fun onItemClick(product: Product) {
        val action =
                ProductListFragmentDirections.actionProductListFragmentToProductDetailsFragment(product)
        findNavController().navigate(action)
    }

    // Here Track function
    private fun trackLog() {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "ProductListScreen")
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "ProductListFragment")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    companion object {
        var secondSpendInScreen = 0.0
    }

}