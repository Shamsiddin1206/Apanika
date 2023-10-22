package shamsiddin.project.apanika.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import okhttp3.internal.notify
import shamsiddin.project.apanika.DataClasses.Product
import shamsiddin.project.apanika.Networking.MySharedPreferences
import shamsiddin.project.apanika.R

class ChoosedAdapter(var list: MutableList<Product>, var onBuy: OnBuy, var context: Context, var onPressed: OnPressed) : RecyclerView.Adapter<ChoosedAdapter.MyViewHolder>(){
    val mySharedPreferences = MySharedPreferences.newInstance(context)
    var b = mySharedPreferences.GetSelectedCarsList()
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var rasm = view.findViewById<ImageView>(R.id.choosed_image)
        var title = view.findViewById<TextView>(R.id.choosed_title)
        var reyting = view.findViewById<TextView>(R.id.choosed_rating)
        var buy = view.findViewById<ImageView>(R.id.buy_choosed)
        var korzina = view.findViewById<ImageView>(R.id.choosed_basket)
        var narx = view.findViewById<TextView>(R.id.choosed_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.choosed_product, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val a = list[position]
        holder.narx.text = "$ " + a.price.toString() + ".00"
        holder.title.text = a.title
        holder.reyting.text = a.rating.toString()
        holder.rasm.load(a.thumbnail)
        holder.korzina.setOnClickListener {
            Log.d("Wishlist", "onBindViewHolder: $b")
            b.remove(a)
            mySharedPreferences.SetSelectedCarsList(b)
            notifyItemRemoved(position)
        }
        holder.buy.setOnClickListener {
            onBuy.onBuy(a)
//            mySharedPreferences.GetSelectedCarsList().remove(a)
//            notifyItemRemoved(position)
        }

        holder.itemView.setOnClickListener {
            onPressed.onPressed(a)
        }
    }

    interface OnBuy{
        fun onBuy(product: Product)
    }

    interface OnPressed{
        fun onPressed(product: Product)
    }
}