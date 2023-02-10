package com.example.camera_new_way

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.camera_new_way.databinding.FragmentShowBinding

class ShowFragment : Fragment() {

    lateinit var adapter: RvAdapter
    lateinit var list: ArrayList<Product>
    lateinit var database: ProductDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show, container, false)
        val bind = FragmentShowBinding.bind(view)
        database = ProductDatabase.getInstance(requireContext())
        list = database.productDao().getAllProduct() as ArrayList<Product>
        adapter = RvAdapter(list)
        bind.rv.adapter = adapter

        return view
    }
}