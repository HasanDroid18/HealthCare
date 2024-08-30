package com.example.healthcare

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthcare.Adapters.BarcodeAdapter
import com.example.healthcare.Models.BarcodeData
import com.example.healthcare.Util.cameraPermissionRequest
import com.example.healthcare.Util.isPermissionGranted
import com.example.healthcare.Util.openPermissionSetting
import com.example.healthcare.databinding.ActivityScanMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.mlkit.vision.barcode.common.Barcode

class ScanMain : AppCompatActivity() {

    private lateinit var binding: ActivityScanMainBinding
    private val cameraPermission = android.Manifest.permission.CAMERA
    private var requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startScanner()
            }
        }

    private lateinit var barcodeAdapter: BarcodeAdapter
    private val barcodeItems = ArrayList<BarcodeData>()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Scan")

        // Load data from Firebase
        loadDataFromFirebase()

        binding.scanBtn.setOnClickListener {
            requestCameraAndStartScanner()
        }
    }

    private fun setupRecyclerView() {
        barcodeAdapter = BarcodeAdapter(this, barcodeItems)
        binding.barcodeRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.barcodeRecyclerview.adapter = barcodeAdapter
    }

    private fun requestCameraPermission() = when {
        shouldShowRequestPermissionRationale(cameraPermission) -> {
            cameraPermissionRequest {
                openPermissionSetting()
            }
        }
        else -> {
            requestPermissionLauncher.launch(cameraPermission)
        }
    }

    private fun startScanner() {
        ScannerActivity.startScanner(this) { barcodes ->
            barcodes.forEach { barcode ->
                val barcodeItem = BarcodeData(
                    type = when (barcode.valueType) {
                        Barcode.TYPE_URL -> "URL"
                        Barcode.TYPE_CONTACT_INFO -> "CONTACT"
                        else -> "OTHER"
                    },
                    content = barcode.rawValue ?: "Unknown"
                )
                barcodeItems.add(barcodeItem)
                barcodeAdapter.notifyDataSetChanged()
                saveToFirebase(barcodeItem)
            }
        }
    }

    private fun requestCameraAndStartScanner() {
        if (isPermissionGranted(cameraPermission)) {
            startScanner()
        } else {
            requestCameraPermission()
        }
    }

    private fun saveToFirebase(barcodeItem: BarcodeData) {
        databaseReference.push().setValue(barcodeItem)
    }

    private fun loadDataFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                barcodeItems.clear() // Clear the list to avoid duplication
                for (dataSnapshot in snapshot.children) {
                    val barcodeData = dataSnapshot.getValue(BarcodeData::class.java)
                    barcodeData?.let { barcodeItems.add(it) }
                }
                barcodeAdapter.notifyDataSetChanged() // Notify adapter to refresh the RecyclerView
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
            }
        })
    }
}
