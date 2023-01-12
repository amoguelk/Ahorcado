package com.amog.ahorcado.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.amog.ahorcado.R
import com.amog.ahorcado.databinding.ActivityMainBinding
import com.amog.ahorcado.model.HangmanAPI
import com.amog.ahorcado.model.Word
import com.amog.ahorcado.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getWord() {
        CoroutineScope(Dispatchers.IO).launch {
            val callWord = Constants.getRetrofit().create(HangmanAPI::class.java).getWord()
            var word : Word?
            callWord.enqueue(object: Callback<Word> {
                override fun onResponse(call: Call<Word>, response: Response<Word>) {
                    Log.d(Constants.LOGTAG, "Palabra: ${response.body()?.word}, Categoría: ${response.body()?.category}")
                    binding.pbLoad.visibility = View.GONE
                    word = response.body()
                }

                override fun onFailure(call: Call<Word>, t: Throwable) {
                    Log.d(Constants.LOGTAG, "ERROR: ${t.message}")
                    Toast.makeText(this@MainActivity, )
                    binding.pbLoad.visibility = View.GONE
                    word = null
                }

            })
        }
    }
}