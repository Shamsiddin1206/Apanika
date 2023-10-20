package shamsiddin.project.apanika.Adapters

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import shamsiddin.project.apanika.DataClasses.CategoryData
import shamsiddin.project.apanika.R

class CategoryAdapter(var list: MutableList<CategoryData>, val onPressed: OnPressed): RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView = view.findViewById<TextView>(R.id.cat_text)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: CategoryAdapter.MyViewHolder, position: Int) {
        val a = list[position]
        holder.textView.text = a.nomi

        if (a.status){
            holder.itemView.setBackgroundResource(R.drawable.imagecategoryedit)
            holder.textView.setTextColor(Color.parseColor("#79BDFE"))
        }else{
            holder.itemView.setBackgroundResource(R.drawable.notselectedborderforcategory)
            holder.textView.setTextColor(Color.parseColor("#000000"))
        }

        holder.itemView.setOnClickListener {
            if (!a.status){
                a.status=true
                holder.itemView.setBackgroundResource(R.drawable.imagecategoryedit)
                holder.textView.setTextColor(Color.parseColor("#79BDFE"))
                for (i in 0 until list.size){
                    if (i!=position){
                        list[i].status = false
                    }
                }
                notifyDataSetChanged()
            }
            onPressed.onPressed(a)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnPressed{
        fun onPressed(categoryData: CategoryData)
    }
}