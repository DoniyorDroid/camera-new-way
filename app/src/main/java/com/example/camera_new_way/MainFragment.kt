package com.example.camera_new_way

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.camera_new_way.databinding.FragmentMainBinding
import java.io.File
import java.io.FileOutputStream


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    lateinit var database: ProductDatabase
    var imgUri: String? = null

    private val galleryPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                getImageContent.launch("image/*")
            }
        }

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri ?: return@registerForActivityResult
            binding.iv.setImageURI(uri)

            val stream = requireContext().contentResolver.openInputStream(uri!!)
            val file = File(requireContext().filesDir, "image.jpg")
            val outputStream = FileOutputStream(file)
            stream?.copyTo(outputStream)
            stream?.close()
            imgUri = file.absolutePath

            binding.iv.visibility = View.VISIBLE
        }

    // gallery
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        binding = FragmentMainBinding.bind(view)

        database = ProductDatabase.getInstance(requireContext())

        binding.btnGallery.setOnClickListener {
            galleryPermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        binding.btnShow.setOnClickListener {
            findNavController().navigate(R.id.showFragment)
        }
        binding.save.setOnClickListener {
            val name = binding.etName.text.toString()
            val price = binding.etPrice.text.toString().toInt()

            val product = Product(name, price, imgUri!!)
            database.productDao().addProduct(product)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("key")
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                binding.iv.visibility = View.VISIBLE
                binding.iv.setImageURI(Uri.parse(result))
                imgUri = result
            }

        binding.btn.setOnClickListener {
            if (allPermissionsGranted()) {
                savedInstance()
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
                )
            }
        }


        // Set up the listeners for take photo and video capture buttons


        return view
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "CameraXApp"

        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                android.Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                savedInstance()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun savedInstance() {
        findNavController().navigate(R.id.cameraFragment)
    }
}