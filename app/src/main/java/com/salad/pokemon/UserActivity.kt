package com.salad.pokemon

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.salad.pokemon.Objects.User
import com.salad.pokemon.ViewModels.UserActivityViewModel
import com.salad.pokemon.databinding.ActivityUserBinding

class UserActivity : AppCompatActivity() {

    lateinit var binding : ActivityUserBinding
    lateinit var viewModel :UserActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var sharedPrefs = getSharedPreferences("pokemonPrefs", MODE_PRIVATE)
        binding = ActivityUserBinding.bind(layoutInflater.inflate(R.layout.activity_user,null,false))
        if(sharedPrefs.contains("first")) {
            //User already exists, go to main activity
            var intent = Intent(this@UserActivity,MainActivity::class.java)
            startActivity(intent)
        }
        binding.apply {
            createBtn.setOnClickListener {
                if(!areInputsEmpty() and isBalanceFloat()){
                    sharedPrefs = getSharedPreferences("pokemonPrefs", MODE_PRIVATE)
                    var edits = sharedPrefs.edit()
                    edits.putString("first",binding.firstnameEt.text.toString())
                    edits.putString("last",binding.lastnameEt.text.toString())
                    edits.putString("ban",binding.accountEt.text.toString())
                    var balanceToFloat = binding.balanceEt.text.toString().toFloatOrNull()!!
                    edits.putFloat("balance",balanceToFloat)
                    edits.putString("email",binding.firstnameEt.text.toString())
                    edits.apply()
                    var user = User(binding.firstnameEt.text.toString(),binding.lastnameEt.text.toString(),binding.accountEt.text.toString(),balanceToFloat,binding.firstnameEt.text.toString())
                    var intent = Intent(this@UserActivity,MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        setContentView(binding.root)
    }

    fun isBalanceFloat() : Boolean
    {
        val checkDouble = binding.balanceEt.text.toString().toFloatOrNull()
        if (checkDouble == null) {
            Log.d("UserActivity","Number is not float")
            Toast.makeText(this,"Balance is not a float.",Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    fun areInputsEmpty() : Boolean {
        if(binding.firstnameEt.text.toString().length == 0){
            Toast.makeText(this,"First name field is empty.",Toast.LENGTH_LONG).show()
        }
        else if(binding.lastnameEt.text.toString().length == 0){
            Toast.makeText(this,"Last name field is empty.",Toast.LENGTH_LONG).show()

        }
        else if(binding.accountEt.text.toString().length == 0){
            Toast.makeText(this,"Account Number field is empty.",Toast.LENGTH_LONG).show()

        }
        else if(binding.balanceEt.text.toString().length == 0){
            Toast.makeText(this,"Balance field is empty.",Toast.LENGTH_LONG).show()
        }
        else if(binding.emailEt.text.toString().length == 0){
            Toast.makeText(this,"Email field is empty.",Toast.LENGTH_LONG).show()
        }
        return false
    }
}