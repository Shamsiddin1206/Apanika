package shamsiddin.project.apanika.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shamsiddin.project.apanika.Adapters.CategoryAdapter
import shamsiddin.project.apanika.Adapters.ProductAdapter
import shamsiddin.project.apanika.DataClasses.CategoryData
import shamsiddin.project.apanika.DataClasses.Login
import shamsiddin.project.apanika.DataClasses.Product
import shamsiddin.project.apanika.DataClasses.ProductData
import shamsiddin.project.apanika.DataClasses.User
import shamsiddin.project.apanika.Networking.APIClient
import shamsiddin.project.apanika.Networking.APIService
import shamsiddin.project.apanika.Networking.MySharedPreferences
import shamsiddin.project.apanika.R
import shamsiddin.project.apanika.databinding.FragmentHomeBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var users: MutableList<User>
    lateinit var selectedProducts: MutableList<Product>
    lateinit var mySharedPreferences: MySharedPreferences
    private val api = APIClient.getInstance().create(APIService::class.java)
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        var curentcategory = "All"
        mySharedPreferences = MySharedPreferences.newInstance(requireContext())
        users = mySharedPreferences.getLoginData()
        selectedProducts = mySharedPreferences.GetSelectedCarsList()


        //Category Recycler
        val listt = mutableListOf<CategoryData>()
        listt.add(CategoryData("All", true))
        api.getAllCategories().enqueue(object : retrofit2.Callback<List<String>>{
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful && !response.body().isNullOrEmpty()){
                    for (i in 0 until response.body()!!.size){
                        listt.add(CategoryData(nomi = response.body()!![i].toString()))
                    }
                    binding.categoryRecycler.adapter = CategoryAdapter(listt, object : CategoryAdapter.OnPressed{
                        override fun onPressed(categoryData: CategoryData) {
                            curentcategory = categoryData.nomi
                            if (categoryData.nomi=="All"){
                                api.getAllProducts().enqueue(object : Callback<ProductData>{
                                    override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                                        if (response.isSuccessful && !response.body()?.products.isNullOrEmpty()){
                                            binding.ProductsRecycler.adapter = ProductAdapter(response.body()!!.products.toMutableList(), object : ProductAdapter.OnSelected{
                                                override fun onSelected(product: Product) {
                                                    selectedProducts.add(product)
                                                    if (mySharedPreferences.getLoginData().isNotEmpty()){
                                                        mySharedPreferences.SetSelectedCarsList(selectedProducts)
                                                    }else{
                                                        binding.LoginFragment.visibility = View.VISIBLE
                                                    }
                                                }
                                            })
                                        }
                                    }

                                    override fun onFailure(call: Call<ProductData>, t: Throwable) {
                                        Log.d("TAG3", "onFailure: $t")
                                    }

                                })
                            }else{
                                api.getProductByCategory(categoryData.nomi).enqueue(object : Callback<ProductData>{
                                    override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                                        if (response.isSuccessful && !response.body()?.products.isNullOrEmpty()){
                                            binding.ProductsRecycler.adapter = ProductAdapter(response.body()!!.products.toMutableList(), object : ProductAdapter.OnSelected{
                                                override fun onSelected(product: Product) {
                                                    selectedProducts.add(product)
                                                    if (users.isNotEmpty()){
                                                        mySharedPreferences.SetSelectedCarsList(selectedProducts)
                                                    }else{
                                                        binding.LoginFragment.visibility = View.VISIBLE
                                                    }
                                                }

                                            })
                                        }
                                    }

                                    override fun onFailure(call: Call<ProductData>, t: Throwable) {
                                        Log.d("TAG2", "onFailure: $t")
                                    }

                                })
                            }
                        }
                    })
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("CategoryList", "onFailure: $t")
            }

        })


        binding.enterLogin.setOnClickListener {
            if (!binding.userTitle.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty()){
                api.login(Login(binding.userTitle.text.toString(), binding.password.text.toString())).enqueue(object : Callback<User>{
                    override fun onResponse(
                        call: Call<User>,
                        response: Response<User>
                    ) {
                        if (response.isSuccessful && response.body() != null){
                            if (response.body()!!.username == binding.userTitle.text.toString()){
                                if (binding.password.text.toString() == "0lelplR"){
                                    binding.LoginFragment.visibility = View.GONE
                                    mySharedPreferences.SetSelectedCarsList(selectedProducts)
                                    users.add(response.body()!!)
                                    mySharedPreferences.setLoginData(users)
                                }else{
                                    Toast.makeText(requireContext(), "Wrong password", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(requireContext(), "Wrong username", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<User>,
                        t: Throwable
                    ) {
                        Log.d("loginn", "onFailure: $t")
                    }

                })
            }
        }

        binding.notNow.setOnClickListener {
            binding.LoginFragment.visibility = View.GONE
            selectedProducts.removeAt(selectedProducts.size-1)
        }

        api.getAllProducts().enqueue(object : Callback<ProductData>{
            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                if (response.isSuccessful && !response.body()?.products.isNullOrEmpty()){
                    binding.ProductsRecycler.adapter = ProductAdapter(response.body()!!.products.toMutableList(), object : ProductAdapter.OnSelected{
                        override fun onSelected(product: Product) {
                            selectedProducts.add(product)
                            if (users.isNotEmpty()){
                                mySharedPreferences.SetSelectedCarsList(selectedProducts)
                            }else{
                                binding.LoginFragment.visibility = View.VISIBLE
                            }
                        }

                    })
                }
            }

            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                Log.d("TAG3", "onFailure: $t")
            }

        })

        binding.search.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText !=null) {
                    api.searchByName(newText).enqueue(object :Callback<ProductData>{
                        override fun onResponse(
                            call: Call<ProductData>,
                            response: Response<ProductData>
                        ) {
                            if (response.isSuccessful && !response.body()?.products.isNullOrEmpty()){
                                val a = mutableListOf<Product>()
                                if (curentcategory=="All"){
                                    binding.ProductsRecycler.adapter = ProductAdapter(response.body()!!.products.toMutableList(), object : ProductAdapter.OnSelected{
                                        override fun onSelected(product: Product) {
                                            selectedProducts.add(product)
                                            if (users.isNotEmpty()){
                                                mySharedPreferences.SetSelectedCarsList(selectedProducts)
                                            }else{
                                                binding.LoginFragment.visibility = View.VISIBLE
                                            }
                                        }

                                    })
                                    binding.ProductsRecycler.visibility = View.VISIBLE
                                    binding.cantFound.visibility = View.GONE
                                }else{
                                    for (i in 0 until response.body()!!.products.size){
                                        if (response.body()!!.products[i].category == curentcategory){
                                            a.add(response.body()!!.products[i])
                                        }
                                    }
                                    if (a.isNotEmpty()){
                                        binding.ProductsRecycler.adapter = ProductAdapter(a, object : ProductAdapter.OnSelected{
                                            override fun onSelected(product: Product) {
                                                selectedProducts.add(product)
                                                if (users.isNotEmpty()){
                                                    mySharedPreferences.SetSelectedCarsList(selectedProducts)
                                                }else{
                                                    binding.LoginFragment.visibility = View.VISIBLE
                                                }
                                            }
                                        })
                                        binding.ProductsRecycler.visibility = View.VISIBLE
                                        binding.cantFound.visibility = View.GONE
                                    }else{
                                        binding.ProductsRecycler.visibility = View.GONE
                                        binding.cantFound.visibility = View.VISIBLE
                                    }
                                }
                            }else{
                                binding.ProductsRecycler.visibility = View.GONE
                                binding.cantFound.visibility = View.VISIBLE
                            }
                        }

                        override fun onFailure(call: Call<ProductData>, t: Throwable) {
                            Log.d("TAG4", "onFailure: $t")
                        }

                    })
                    return true
                }
                binding.ProductsRecycler.visibility = View.VISIBLE
                binding.cantFound.visibility = View.GONE
                return false
            }

        })


        binding.homeScreenSelected.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main, ChoosedFragment()).commit()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}