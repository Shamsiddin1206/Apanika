package shamsiddin.project.apanika.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import coil.load
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
    @SuppressLint("SetTextI18n")
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
                                            }, object : ProductAdapter.OnBosildi{
                                                override fun onBosildi(product: Product) {
                                                    parentFragmentManager.beginTransaction().replace(R.id.main, SingleProductFragment.newInstance(product)).commit()
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

                                            }, object : ProductAdapter.OnBosildi{
                                                override fun onBosildi(product: Product) {
                                                    parentFragmentManager.beginTransaction().replace(R.id.main, SingleProductFragment.newInstance(product)).commit()
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
                    @SuppressLint("SetTextI18n")
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
                                    binding.personProfileImg.load(users[0].image)
                                    binding.personName.text = users[0].firstName + " " + users[0].lastName
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

                    }, object : ProductAdapter.OnBosildi{
                        override fun onBosildi(product: Product) {
                            parentFragmentManager.beginTransaction().replace(R.id.main, SingleProductFragment.newInstance(product)).commit()
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

                                    }, object : ProductAdapter.OnBosildi{
                                        override fun onBosildi(product: Product) {
                                            parentFragmentManager.beginTransaction().replace(R.id.main, SingleProductFragment.newInstance(product)).commit()
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
                                        }, object : ProductAdapter.OnBosildi{
                                            override fun onBosildi(product: Product) {
                                                parentFragmentManager.beginTransaction().replace(R.id.main, SingleProductFragment.newInstance(product)).commit()
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
            if (mySharedPreferences.getLoginData().isNotEmpty()){
                parentFragmentManager.beginTransaction().replace(R.id.main, ChoosedFragment()).commit()
            }else{
                binding.LoginFragment.visibility = View.VISIBLE
            }
        }



        if (users.isNotEmpty()){
            binding.personProfileImg.load(users[0].image)
            binding.personName.text = users[0].firstName + " " + users[0].lastName
        }
        binding.homeScreenMenu.setOnClickListener {
            if (mySharedPreferences.getLoginData().isNotEmpty()){
                val animation_menu = AnimationUtils.loadAnimation(requireContext(), R.anim.menu_animation_start)
                binding.menutask.startAnimation(animation_menu)
                binding.menutask.visibility = View.VISIBLE
                binding.darkmode.visibility = View.VISIBLE
                binding.realHomeFragment.isClickable = false

            }else{
                binding.LoginFragment.visibility = View.VISIBLE
            }
        }
        binding.backtomain.setOnClickListener {
            binding.menutask.visibility = View.GONE
            val animation_finish = AnimationUtils.loadAnimation(requireContext(), R.anim.menu_animation_finish)
            binding.menutask.startAnimation(animation_finish)
            binding.darkmode.visibility = View.GONE
            binding.realHomeFragment.isContextClickable = true
        }
        binding.linerNotification.setOnClickListener {
            Toast.makeText(requireContext(), "You do not have any notifications", Toast.LENGTH_SHORT).show()
        }
        binding.help.setOnClickListener {
            Toast.makeText(requireContext(), "Email to takhirovshamsiddin@gmail.com", Toast.LENGTH_SHORT).show()
        }
        binding.logout.setOnClickListener {
            users.clear()
            mySharedPreferences.setLoginData(users)
            Toast.makeText(requireContext(), "You logged out", Toast.LENGTH_SHORT).show()
            binding.menutask.visibility = View.GONE
            val animation_finish = AnimationUtils.loadAnimation(requireContext(), R.anim.menu_animation_finish)
            binding.menutask.startAnimation(animation_finish)
            binding.personProfileImg.setImageResource(R.drawable.personprofile)
            binding.darkmode.visibility = View.GONE
            binding.realHomeFragment.isContextClickable = true
        }
        binding.linerMyorders.setOnClickListener {
            Toast.makeText(requireContext(), "You have not added your card yet", Toast.LENGTH_SHORT).show()
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