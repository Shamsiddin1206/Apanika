package shamsiddin.project.apanika.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import shamsiddin.project.apanika.Adapters.CategoryAdapter
import shamsiddin.project.apanika.DataClasses.CategoryData
import shamsiddin.project.apanika.DataClasses.ProductData
import shamsiddin.project.apanika.Networking.APIClient
import shamsiddin.project.apanika.Networking.APIService
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

    private val api = APIClient.getInstance().create(APIService::class.java)
    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

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
                            Log.d("TAG", "onPressed: $listt")
                        }
                    })
                }
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                Log.d("CategoryList", "onFailure: $t")
            }

        })

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