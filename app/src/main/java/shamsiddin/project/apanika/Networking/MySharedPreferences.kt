package shamsiddin.project.apanika.Networking

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import shamsiddin.project.apanika.DataClasses.Product
import shamsiddin.project.apanika.DataClasses.ProductData
import shamsiddin.project.apanika.DataClasses.User

class MySharedPreferences private constructor(contexT: Context){
    val sharedPreferences = contexT.getSharedPreferences("data", 0)
    val edit = sharedPreferences.edit()
    val gson = Gson()

    companion object{
        private var instance:MySharedPreferences? = null
        fun newInstance(contexT: Context): MySharedPreferences {
            if (instance == null){
                instance = MySharedPreferences(contexT)
            }
            return instance!!
        }
    }

    fun setLoginData(mutableList: MutableList<User>){
        edit.putString("Login", gson.toJson(mutableList)).apply()
    }
    fun getLoginData(): MutableList<User>{
        val data: String = sharedPreferences.getString("Login", "")!!
        if (data == ""){
            return mutableListOf()
        }
        val typeToken = object : TypeToken<MutableList<User>>(){}.type
        return gson.fromJson(data, typeToken)
    }

    fun GetSelectedCarsList(): MutableList<Product>{
        val data: String = sharedPreferences.getString("Selected", "")!!
        if (data == ""){
            return mutableListOf()
        }
        val typeToken = object : TypeToken<MutableList<Product>>(){}.type
        return gson.fromJson(data, typeToken)
    }
    fun SetSelectedCarsList(mutableList: MutableList<Product>){
        edit.putString("Selected", gson.toJson(mutableList)).apply()
    }
}