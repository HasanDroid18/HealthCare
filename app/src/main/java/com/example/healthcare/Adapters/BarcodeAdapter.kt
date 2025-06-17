package com.example.healthcare.Adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.healthcare.Models.BarcodeData
import com.example.healthcare.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BarcodeAdapter(private val context: Context, private val barcodeItems: MutableList<BarcodeData>) :
    RecyclerView.Adapter<BarcodeAdapter.ViewHolder>() {

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("Scan")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewNumber: TextView = itemView.findViewById(R.id.counter)
        val textViewType: TextView = itemView.findViewById(R.id.text_view_qr_type)
        val textViewContent: TextView = itemView.findViewById(R.id.text_view_qr_content)
        val copyButton: ImageButton = itemView.findViewById(R.id.copy_btn)
        val removeButton: ImageButton = itemView.findViewById(R.id.remove_btn)

        init {
            copyButton.setOnClickListener {
                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("QR Content", textViewContent.text)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(context, "QR content copied to clipboard", Toast.LENGTH_SHORT).show()
            }
            removeButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val itemToRemove = barcodeItems[position]

                    itemToRemove.id?.let { id ->
                        databaseReference.child(id).removeValue()
                            .addOnSuccessListener {
                                // Only modify the local list if the delete was successful
                                if (barcodeItems.size > position) {
                                    barcodeItems.removeAt(position)
                                    notifyItemRemoved(position)
                                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show()
                            }
                    } ?: Toast.makeText(context, "Error: Item ID is missing.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.barcode_data_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = barcodeItems[position]
        holder.textViewNumber.text = (position + 1).toString() // Numbering starts from 1
        holder.textViewType.text = item.type
        holder.textViewContent.text = item.content
    }

    override fun getItemCount(): Int = barcodeItems.size
}
