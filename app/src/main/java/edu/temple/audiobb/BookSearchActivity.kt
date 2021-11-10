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
import org.json.JSONObject
import org.json.JSONArray
import org.json.JSONException


class BookSearchActivity : AppCompatActivity() {
    lateinit var bookList: BookList
    val searchEditText : EditText by lazy {
        findViewById(R.id.searchEditText)
    }
    val searchButton : Button by lazy {
        findViewById(R.id.searchButton)
    }
    val volleyQueue : RequestQueue by lazy {
        Volley.newRequestQueue(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_search)

        searchButton.setOnClickListener {
            fetchBook(searchEditText.text.toString())
        }
    }

    private fun fetchBook(bookSearch : String){
        val url = "https://kamorris.com/lab/cis3515/search.php?term=$bookSearch"

        volleyQueue.add(
            JsonArrayRequest(
                Request.Method.GET
                , url
                , null
                ,
                {
                    Log.d("Response", it.toString())
                    try{
                        for(i in 0 until it.length()){
                            val jsonObject = it.getJSONObject(i)
                            val book = Book(jsonObject.getString("title"),
                                jsonObject.getString("author"),
                                jsonObject.getInt("id"),
                                jsonObject.getString("coverURL"))
                            bookList.add(book)
                        }
                        val resultIntent = intent
                        resultIntent.putExtra("book", bookList)
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