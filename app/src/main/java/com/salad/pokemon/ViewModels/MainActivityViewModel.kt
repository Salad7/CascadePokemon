package com.salad.pokemon.ViewModels

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salad.pokemon.MainActivity
import com.salad.pokemon.Retrofit.PokemonApi
import com.salad.pokemon.Retrofit.PokemonObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.text.DecimalFormat


class MainActivityViewModel(main :MainActivity) : ViewModel() {

    var mutableResults : MutableStateFlow<ArrayList<PokemonObject>> = MutableStateFlow(ArrayList<PokemonObject>())
    var immutableResults = mutableResults.asStateFlow()
    var mainActivity = main
    init {

    }

    suspend fun searchQuery(q :String){
        val logging = HttpLoggingInterceptor()
        val httpclient = OkHttpClient.Builder()
        httpclient.addInterceptor(logging)
        Log.d("MainVM Pokemon: ",q)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/pokemon/"+q+"/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(httpclient.build())
            .build()

        val results = retrofit.create<PokemonApi>().getPokemon("").replace("&quot;", "\\\"")
        Log.d("hmmm",results)

            if("base_experience" in results){
                var base_exp = results.toString().split("\"base_experience\":")[1].split(",")[0].toDouble()
                Log.d("base_experience",base_exp.toString())
                var obj = PokemonObject(base_exp*6*.01)
                var listOfObjects = ArrayList<PokemonObject>()
                listOfObjects.add(obj)
                mutableResults.value = listOfObjects
                mainActivity.binding.requestPb.visibility = View.INVISIBLE

            }
        else {
                Log.d("MAVW","Did not find pokemon.")
                Toast.makeText(mainActivity,"Did not find pokemon",
                    Toast.LENGTH_SHORT).show()
                mainActivity.binding.requestPb.visibility = View.INVISIBLE

            }
//        }

        viewModelScope.launch {

        }
    }

}