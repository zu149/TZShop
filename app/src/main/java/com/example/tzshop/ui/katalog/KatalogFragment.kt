package com.example.tzshop.ui.katalog

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tzshop.FavouritesActivity
import com.example.tzshop.ItemsAPI
import com.example.tzshop.ProductPageActivity
import com.example.tzshop.adapters.AdapterKatalog

import com.example.tzshop.databinding.FragmentKatalogBinding
import com.example.tzshop.models.Item
import com.example.tzshop.models.Root
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Serializable

class KatalogFragment : Fragment() {

    private var _binding: FragmentKatalogBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentKatalogBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()


    }




    private fun getData () { // создается и происходит парсинг ссылки
        val retrofit = Retrofit.Builder()
        .baseUrl("https://run.mocky.io/v3/")
            .addConverterFactory(GsonConverterFactory.create()).build() // вызов классa в качестве gson
        val retrofitAPI = retrofit.create(ItemsAPI::class.java)
        val call: Call<Root?>? = retrofitAPI.getItems() // экзэмпляр для выполнеия запроса получающий путь к окончательной ссылке
        call!!.enqueue(object : Callback<Root?> {
            override fun onResponse(
                call: Call<Root?>?,
                response: Response<Root?>
            ) {
                if (response.isSuccessful) {

                val list: List<Item> = response.body()!!.items

                    if(list.isNotEmpty()) {
                        binding.spisok.layoutManager = GridLayoutManager(requireActivity(), 2) //GridLayoutManager - сетка
                        binding.spisok.setHasFixedSize(true) // не будет искажаться, когда будет изменяться количество элементов
                        val x = AdapterKatalog(requireActivity(), list) // x содержить значиения адапетра
                        binding.spisok.adapter = x

                        x.setOnClick(object : AdapterKatalog.OnClickListener{
                            override fun onClick(position: Int, item: Item) {
                                super.onClick(position, item)
                                val intent = Intent(requireActivity(), ProductPageActivity::class.java)
                                intent.putExtra("item", item) //дает возможность отправлять целые объекты
                                startActivity(intent)
                            }
                        })
                    }


                }
            }

            override fun onFailure(call: Call<Root?>, t: Throwable) {

            }
        })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

