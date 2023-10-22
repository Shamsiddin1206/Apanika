package shamsiddin.project.apanika.UI

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import shamsiddin.project.apanika.DataClasses.Product
import shamsiddin.project.apanika.R
import shamsiddin.project.apanika.databinding.FragmentSingleProductBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SingleProductFragment : Fragment() {
    private var param1: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getSerializable(ARG_PARAM1) as Product
        }
    }

    lateinit var binding: FragmentSingleProductBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleProductBinding.inflate(inflater, container, false)

        binding.singleProductImage.load(param1!!.thumbnail)
        binding.singleProductDescription.text = param1!!.description
        binding.singleProductName.text = param1!!.title
        binding.singleProductPrice.text = param1!!.price.toString() + ".00$"
        binding.singleProductRating.text = param1!!.rating.toString()
        binding.backFromSingleProduct.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.main, HomeFragment()).commit()
        }
        binding.singleProductMakeOrder.setOnClickListener {
            Toast.makeText(requireContext(), "You have not added your card yet", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Product) =
            SingleProductFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}