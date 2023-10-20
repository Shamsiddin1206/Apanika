package shamsiddin.project.apanika.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import shamsiddin.project.apanika.DataClasses.Product
import shamsiddin.project.apanika.R

class ProductAdapter(var list: MutableList<Product>): RecyclerView.Adapter<ProductAdapter.MyViewHolder>() {
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var rasm = view.findViewById<ImageView>(R.id.product_image)
        var nomi = view.findViewById<TextView>(R.id.product_title)
        var narx = view.findViewById<TextView>(R.id.product_price)
        var raeing = view.findViewById<TextView>(R.id.product_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val a = list[position]
        holder.nomi.text = a.title
        holder.narx.text = a.price.toString()
        holder.rasm.load(a.thumbnail)
        holder.raeing.text = a.rating.toString()
    }
}