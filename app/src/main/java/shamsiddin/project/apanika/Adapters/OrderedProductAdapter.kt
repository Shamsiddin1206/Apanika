package shamsiddin.project.apanika.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import shamsiddin.project.apanika.DataClasses.Product
import shamsiddin.project.apanika.DataClasses.ProductX
import shamsiddin.project.apanika.R

class OrderedProductAdapter(var mutableList: MutableList<ProductX>, var context: Context): RecyclerView.Adapter<OrderedProductAdapter.MyHolder>() {
    class MyHolder(itemview: View): RecyclerView.ViewHolder(itemview) {
        val title : TextView = itemView.findViewById(R.id.cart_item_title)
        val price : TextView = itemView.findViewById(R.id.cart_item_price)
        val quantity : TextView = itemView.findViewById(R.id.cart_item_quantity)
        val totalPrice : TextView = itemView.findViewById(R.id.cart_item_total_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val myholder = MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.buyedproduct_item, parent, false))
        return myholder
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val product = mutableList[position]
        holder.title.text = product.title
        holder.price.text = product.price.toString() + " $"
        holder.quantity.text = product.quantity.toString()
        holder.totalPrice.text = product.total.toString() + " $"
    }
}