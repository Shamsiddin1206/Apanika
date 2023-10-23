package shamsiddin.project.apanika.Adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import shamsiddin.project.apanika.R

class ViewPagerAdapter(val images:List<String>): RecyclerView.Adapter<ViewPagerAdapter.MyHolder>() {
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.product_image_item_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false))
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.image.load(images[position])
    }
}