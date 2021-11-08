package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BookListFragment.EventInterface {

    private var twoFragment = false
    private lateinit var bookViewModel: BookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        twoFragment = findViewById<View>(R.id.fragmentContainerView2) != null

        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        val booksList = BookList()
        bookArray(booksList)

        val bookListFragment = BookListFragment.newInstance(booksList)

        //Pop redundant DetailsFragment from stack if book is selected to set up landscape view
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) is BookDetailsFragment &&
            twoFragment
        ) {
            supportFragmentManager.popBackStack()
        }

        //Open a BookDetailsFragment in portrait mode if the BookDetailsFragment is displaying a book in landscape
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) is BookDetailsFragment &&
            !twoFragment
        ) {
            if (ViewModelProvider(this).get(BookViewModel::class.java).getBook().value?.title != "") {
                selectionMade()
            }
        }

        //Add BookListFragment on app startup
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, bookListFragment)
                .commit()
        }
         //When second fragment is available, place an instance of BookDetailsFragment
        if(twoFragment){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, BookDetailsFragment())
                .commit()
        }
    }

    /*private fun bookArray (bookList : BookList){
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
    }*/

    // A message from the fragment whenever a book is selected
    override fun selectionMade(){

        //Replace BookListFragment with BookDetailsFragment when a book is selected in portrait mode
        if(!twoFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment())
                .addToBackStack(null)
                .commit()
        }
        else{
            //Add BookDetailsFragment to Fragment2 when a book is selected in landscape mode
            if(supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView2, BookDetailsFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onBackPressed(){
        super.onBackPressed()
        bookViewModel.setBook(Book("",""))
    }
}