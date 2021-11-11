package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONException


class BookSearchActivity : AppCompatActivity() {
    val bookList = BookList()

    val volleyQueue : RequestQueue by lazy {
        Volley.newRequestQueue(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)

        searchButton.setOnClickListener {
            fetchBook(searchEditText.text.toString())
        }
    }

    private fun fetchBook(bookSearch : String){
        val url = "https://kamorris.com/lab/cis3515/search.php?term=$bookSearch"

        volleyQueue.add(
            JsonArrayRequest(
                Request.Method.GET, url, null,
                {
                    Log.d("Response", it.toString())
                    try{
                        for(i in 0 until it.length()){
                            val jsonObject = it.getJSONObject(i)
                            val book = Book(jsonObject.getInt("id"),
                            jsonObject.getString("title"),
                            jsonObject.getString("author"),
                            jsonObject.getString("cover_url"))
                            bookList.add(book)
                        }
                        val resultIntent = intent
                        resultIntent.putExtra("jsonbooklist", bookList)
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    }
                    catch (e : JSONException){
                        e.printStackTrace()
                    }


                },{
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show()
                })
        )

    }

}