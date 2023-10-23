package shamsiddin.project.apanika.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shamsiddin.project.apanika.Adapters.OrderedProductAdapter
import shamsiddin.project.apanika.DataClasses.CartData
import shamsiddin.project.apanika.DataClasses.Product
import shamsiddin.project.apanika.DataClasses.ProductData
import shamsiddin.project.apanika.DataClasses.ProductX
import shamsiddin.project.apanika.Networking.APIClient
import shamsiddin.project.apanika.Networking.APIService
import shamsiddin.project.apanika.Networking.MySharedPreferences
import shamsiddin.project.apanika.R
import shamsiddin.project.apanika.databinding.BuyedproductItemBinding
import shamsiddin.project.apanika.databinding.FragmentCardBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class CardFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var mySharedPreferences: MySharedPreferences
    private var quantity: Int = 0
    var product: Product? = null
    lateinit var list: MutableList<Product>
    private val api = APIClient.getInstance().create(APIService::class.java)
    lateinit var binding: FragmentCardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        list = mutableListOf()
        binding = FragmentCardBinding.inflate(inflater, container, false)
        mySharedPreferences = MySharedPreferences.newInstance(requireContext())

        val id = mySharedPreferences.getLoginData().get(0).id
        Log.d("TAG20", "onCreateView: $id")

        api.getBuyedById(id).enqueue(object : retrofit2.Callback<CartData>{
            override fun onResponse(call: Call<CartData>, response: Response<CartData>) {
                if (response.isSuccessful){
                    val products = response.body()!!.carts[0].products.toMutableList()
                    if (product != null){
                        val productX = ProductX(0.0, 0, product!!.id, product!!.price, quantity, product!!.title, product!!.price * quantity)
                        products.add(0, productX)
                    }
                    Log.d("TAG15", "onResponse: $products")
                    binding.Sotvolinganrecycelr.adapter = OrderedProductAdapter(products, requireContext())
                }
            }

            override fun onFailure(call: Call<CartData>, t: Throwable) {
                Log.d("TAG", "$t")
            }
        })

        binding.addcard.setOnClickListener {
            Toast.makeText(requireContext(), "This function is not available yet", Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}