package com.salad.pokemon

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.salad.pokemon.ViewModels.MainActivityViewModel
import com.salad.pokemon.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    lateinit var binding :ActivityMainBinding
    lateinit var viewModel :MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var v = LayoutInflater.from(this@MainActivity).inflate(R.layout.activity_main,null,false)
        binding = ActivityMainBinding.bind(v)
        viewModel = MainActivityViewModel(this@MainActivity)
        var sharedPref = getSharedPreferences("pokemonPrefs", MODE_PRIVATE)
        binding.userFirstNameTv.setText("Welcome back, "+sharedPref.getString("first","null")+"!")

        binding.userBalanceTv.setText("Your balance: "+sharedPref.getFloat("balance",0.0f).toString())
        binding.apply {
                searchButton.setOnClickListener {
                    lifecycleScope.launch {
                    var searchQuery = pokemonSearchEt.text.toString()
                    if(searchQuery.length == 0){
                        Toast.makeText(this@MainActivity,"Search query cannot be empty",Toast.LENGTH_SHORT).show()
                        Log.d("MainActivity",searchQuery)
                    }
                    else {
                        //Time to make search query
                        try {
                            binding.requestPb.visibility = View.VISIBLE
                            viewModel.searchQuery(searchQuery)
                            viewModel.immutableResults.collect() {
                                binding.requestPb.visibility = View.INVISIBLE
                                var value = it.get(0).base_experience
                                var balance = sharedPref.getFloat("balance",0.0f)
                                if(balance > value){
                                    //User can buy
                                    // build alert dialog
                                    val dialogBuilder = AlertDialog.Builder(this@MainActivity)

                                    // set message of alert dialog
                                    dialogBuilder.setMessage("Congrats you can buy "+searchQuery+" with a balance of: "+balance.toString()+". This will cost you: "+currencyFormat(value.toString()))
                                        // if the dialog is cancelable
                                        .setCancelable(true)
                                        // positive button text and action
                                        .setPositiveButton("Purchase", DialogInterface.OnClickListener {
                                                dialog, id -> dialog.cancel()

                                        })
                                        // negative button text and action
                                        .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                                                dialog, id -> dialog.cancel()
                                        })

                                    // create dialog box
                                    val alert = dialogBuilder.create()
                                    // set title for alert dialog box
                                    alert.setTitle("Payment processing flow")
                                    // show alert dialog
                                    alert.show()
                                }
                                else {
                                    //User cannot buy, send toast
                                    Toast.makeText(this@MainActivity,"Your balance of "+balance.toString()+" is not enough",Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        catch (ex :Exception){
                            Toast.makeText(this@MainActivity,"Error running search",Toast.LENGTH_SHORT).show()
                            Toast.makeText(this@MainActivity,ex.printStackTrace().toString(),Toast.LENGTH_SHORT).show()
                            ex.printStackTrace()
                        }
                    }
                }

            }

            createNewUserBtn.setOnClickListener {
                val sharedPrefs = getSharedPreferences("pokemonPrefs", MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.clear()
                editor.apply()
                var intent = Intent(this@MainActivity,UserActivity::class.java)
                startActivity(intent)
            }
        }

        setContentView(binding.root)


    }



    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }
}