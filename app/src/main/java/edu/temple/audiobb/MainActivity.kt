package edu.temple.audiobb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BookListFragment.EventInterface {

    private var twoFragment = false
    lateinit var bookViewModel: BookViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        val booksList = BookList()
        bookArray(booksList)
        val bookListFragment = BookListFragment.newInstance(booksList)
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) is BookDetailsFragment &&
            !twoFragment
        ) {
            supportFragmentManager.popBackStack()
        }
        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) is BookDetailsFragment &&
            !twoFragment
        ) {
            if (ViewModelProvider(this).get(BookViewModel::class.java).getBook().value?.title != ""
                && !bookViewModel.isBookEmpty()) {
                selectionMade()
            }
        }


        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, bookListFragment)
                .commit()
        }
        if(twoFragment){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView2, BookDetailsFragment())
                .commit()
        }
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
    override fun selectionMade(){
        if(!twoFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}