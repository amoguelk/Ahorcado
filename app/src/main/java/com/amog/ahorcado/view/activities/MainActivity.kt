package com.amog.ahorcado.view.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        val word = getWord()

        // Setup keyboard
        var btnArray : Array<Button>
        with(binding) {
            btnArray = arrayOf(btnLetter01, btnLetter02, btnLetter03, btnLetter04, btnLetter05, btnLetter06, btnLetter07,
                btnLetter08, btnLetter09, btnLetter10, btnLetter11, btnLetter12, btnLetter13, btnLetter14,
                btnLetter15, btnLetter16, btnLetter17, btnLetter18, btnLetter19, btnLetter20, btnLetter21,
                btnLetter22, btnLetter23, btnLetter24, btnLetter25, btnLetter26)
        }
        for (i in 0 until 26) {
            val btn = btnArray[i]
            btn.text = resources.getStringArray(R.array.letters)[i]
            btn.setOnClickListener {
                Toast.makeText(this, resources.getStringArray(R.array.letters)[i], Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getWord(): Word? {
        var word : Word? = null
        CoroutineScope(Dispatchers.IO).launch {
            val callWord = Constants.getRetrofit().create(HangmanAPI::class.java).getWord()
            callWord.enqueue(object: Callback<Word> {
                override fun onResponse(call: Call<Word>, response: Response<Word>) {
                    Log.d(Constants.LOGTAG, "Palabra: ${response.body()?.word}, Categoría: ${response.body()?.category}")
                    binding.pbLoad.visibility = View.GONE
                    word = response.body()
                }

                override fun onFailure(call: Call<Word>, t: Throwable) {
                    Log.d(Constants.LOGTAG, "ERROR: ${t.message}")
                    Toast.makeText(this@MainActivity, getString(R.string.error_text, t.message), Toast.LENGTH_LONG).show()
                    binding.pbLoad.visibility = View.GONE
                    word = null
                }

            })
        }
        return word
    }

    fun restartGame(view: View) {
        getWord()
    }
}