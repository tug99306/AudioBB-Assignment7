package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val booksList = ArrayList<Book>()
        val array = BookList(booksList)
        bookArray(array)
    }

    fun bookArray (bookList : BookList){
        bookList.add(Book("Harry Potter","J.K. Rowling"))
        bookList.add(Book("The Hunger Games","Suzanne Collins"))
        bookList.add(Book("Divergent","Veronica Roth"))
        bookList.add(Book("The Four Winds","Kristin Hannah"))
        bookList.add(Book("The Lost Apothecary","Sarah Penner"))
        bookList.add(Book("The Push","Ashley Audrain"))
        bookList.add(Book("The Paris Library","Janet Skeslien Charles"))
        bookList.add(Book("Malibu Rising","Taylor Jenkins Reid"))
        bookList.add(Book("The Rose Code","Kate Quinn"))
        bookList.add(Book("One Last Stop","Casey McQuiston"))
    }
}