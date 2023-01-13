package com.amog.ahorcado.view.activities

import android.app.AlertDialog
import android.content.DialogInterface
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
    private var word : Word? = null
    private var mistakes : Int = 0
    private var correct : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getWord()
        binding.ivHangman.setImageDrawable(getDrawable(getHangmanImage()))
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
            val letter = resources.getStringArray(R.array.letters)[i]
            btn.text = letter
            btn.setOnClickListener {
                letterSelected(btn)
            }
        }
    }

    private fun getWord() {
        CoroutineScope(Dispatchers.IO).launch {
            val callWord = Constants.getRetrofit().create(HangmanAPI::class.java).getWord()
            callWord.enqueue(object: Callback<Word> {
                override fun onResponse(call: Call<Word>, response: Response<Word>) {
                    word = response.body()
                    Log.d(Constants.LOGTAG, "Palabra: ${word?.word}, Categoría: ${word?.category}")
                    binding.tvCategory.text = getString(R.string.category_text, word?.category)
                    var blankWord = ""
                    val wordSize = word?.word!!.length
                    for (i in 0 until wordSize) {
                        blankWord += "_"
                        if (i < wordSize - 1) blankWord += " "
                    }
                   binding.tvWord.text = blankWord

                    with (binding) {
                        pbLoad.visibility = View.GONE
                        keyboardLayout.visibility = View.VISIBLE
                        ivHangman.visibility = View.VISIBLE
                        tvCategory.visibility = View.VISIBLE
                        tvWord.visibility = View.VISIBLE
                        ivError.visibility = View.GONE
                        tvError.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<Word>, t: Throwable) {
                    Log.d(Constants.LOGTAG, "ERROR: ${t.message}")
                    Toast.makeText(this@MainActivity, getString(R.string.error_text_toast, t.message), Toast.LENGTH_LONG).show()
                    with (binding) {
                        pbLoad.visibility = View.GONE
                        keyboardLayout.visibility = View.GONE
                        ivHangman.visibility = View.GONE
                        tvCategory.visibility = View.GONE
                        tvWord.visibility = View.GONE
                        ivError.visibility = View.VISIBLE
                        tvError.visibility = View.VISIBLE
                    }
                    word = null
                }

            })
        }
    }

    private fun letterSelected(letterBtn : Button) {
        val answer = word?.word!!.uppercase()
        val letter = letterBtn.text.toString()
        if (answer.contains(letter)) {
            var currentText = binding.tvWord.text
            var pointer = 0
            var index = answer.indexOf(letter, pointer)
            do {
                correct += 1
                currentText = "${currentText.substring(0,index*2)}${letter}${currentText.substring((index*2)+1)}"
                pointer = index + 1
                index = answer.indexOf(letter, pointer)
            } while (index > -1)
            binding.tvWord.text = currentText
            if (correct >= answer.length) finishGame(true)
        } else {
            mistakes += 1
            binding.ivHangman.setImageDrawable(getDrawable(getHangmanImage()))
            if (mistakes >= 6) finishGame(false)
        }
        letterBtn.isEnabled = false
    }

    private fun getHangmanImage() : Int {
        return when (mistakes) {
            0 -> R.drawable.hangman00
            1 -> R.drawable.hangman01
            2 -> R.drawable.hangman02
            3 -> R.drawable.hangman03
            4 -> R.drawable.hangman04
            5 -> R.drawable.hangman05
            else -> R.drawable.hangman06
        }
    }

    private fun finishGame(won: Boolean) {
        var btnArray : Array<Button>
        with(binding) {
            btnArray = arrayOf(btnLetter01, btnLetter02, btnLetter03, btnLetter04, btnLetter05, btnLetter06, btnLetter07,
                btnLetter08, btnLetter09, btnLetter10, btnLetter11, btnLetter12, btnLetter13, btnLetter14,
                btnLetter15, btnLetter16, btnLetter17, btnLetter18, btnLetter19, btnLetter20, btnLetter21,
                btnLetter22, btnLetter23, btnLetter24, btnLetter25, btnLetter26)
        }
        btnArray.forEach { btn -> btn.isEnabled = false }
        val dialogTitle : String
        val dialogMessage : String
        if (won) {
            dialogTitle = getString(R.string.gameWon_text)
            dialogMessage = getString(R.string.newGame_text)
        }
        else {
            dialogTitle = getString(R.string.gameLost_text)
            dialogMessage = "${getString(R.string.answer_text, word?.word!!)}\n${getString(R.string.newGame_text)}"
        }
        newGameDialog(dialogTitle, dialogMessage)
    }

    private fun newGameDialog(title : String, message : String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setIcon(R.drawable.ic_warning)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                getWord()
                var btnArray : Array<Button>
                with(binding) {
                    btnArray = arrayOf(btnLetter01, btnLetter02, btnLetter03, btnLetter04, btnLetter05, btnLetter06, btnLetter07,
                        btnLetter08, btnLetter09, btnLetter10, btnLetter11, btnLetter12, btnLetter13, btnLetter14,
                        btnLetter15, btnLetter16, btnLetter17, btnLetter18, btnLetter19, btnLetter20, btnLetter21,
                        btnLetter22, btnLetter23, btnLetter24, btnLetter25, btnLetter26)
                }
                btnArray.forEach { btn -> btn.isEnabled = true }
                mistakes = 0
                correct = 0
                binding.ivHangman.setImageDrawable(getDrawable(getHangmanImage()))
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) {dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onRestart() {
        super.onRestart()
        restartGame(View(this))
    }

    fun restartGame(view: View) {
        newGameDialog(getString(R.string.restart_text), getString(R.string.restart_confirm_text))
    }
}