package shamsiddin.project.apanika.Networking

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import shamsiddin.project.apanika.DataClasses.ProductData

interface APIService {

    @GET("/products")
    fun getAllProducts(): Call<ProductData>

    @GET("/products/categories")
    fun getAllCategories(): Call<List<String>>

//    @GET("/products/{id}")
//    fun getProduct(@Path("id") id: Int): Call<Product>
//
//    @GET("products/search")
//    fun searchByName(@Query("q") name: String): Call<ProductData>
//
//    @POST("/auth/login")
//    fun login(@Body login: Login): Call<User>


}