package edu.temple.audiobb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.app.Activity
import android.util.Log
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModelProvider
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult


class MainActivity : AppCompatActivity(), BookListFragment.EventInterface, BookListFragment.Search {

    private var twoFragment = false
    var initialLoad = false
    private lateinit var bookViewModel: BookViewModel
    var bookList : BookList = BookList()
    lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        twoFragment = findViewById<View>(R.id.fragmentContainerView2) != null
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        startForResult = registerForActivityResult(StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    val jsonBook = it.data?.getSerializableExtra("jsonbooklist") as BookList
                    bookList.clear()
                    for (i in 0 until jsonBook.getSize()) {
                        Log.i("Received Book:", jsonBook.getBook(i).title)
                        bookList.add(jsonBook.getBook(i))
                    }
                    if (initialLoad) {
                        fragmentInit()
                        initialLoad = false
                    } else {
                        if (supportFragmentManager.fragments[0] !is BookListFragment) {
                            (supportFragmentManager.fragments[1] as BookListFragment)
                                .updateBookList(bookList)
                        } else {
                            (supportFragmentManager.fragments[0] as BookListFragment)
                                .updateBookList(bookList)
                        }
                    }
                }
            }
        }
        if (savedInstanceState == null) {
            initialLoad = true
            doSearch()
        } else {
            fragmentInit()
        }
    }

    // A message from the fragment whenever a book is selected
    override fun selectionMade(){

        //Replace BookListFragment with BookDetailsFragment when a book is selected in portrait mode
        if(!twoFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
        else{
            //Add BookDetailsFragment to Fragment2 when a book is selected in landscape mode
            if(supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null){
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView2, BookDetailsFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
    private fun fragmentInit(){

    if(initialLoad) {
        bookViewModel.setBook(Book(-1,"","", ""))
        if (twoFragment) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView1, BookListFragment.newInstance(bookList))
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView1, BookListFragment.newInstance(bookList))
                .addToBackStack(null)
                .commit()
        }
    }

        if(twoFragment){
            if(supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) is BookDetailsFragment){
                supportFragmentManager.popBackStack()
            }
            if(supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) == null){
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainerView2, BookDetailsFragment.newInstance())
                    .commit()
            }
        }else if (bookViewModel.getBook().value != Book(-1,"","", "")){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

    }

    override fun onBackPressed(){
        super.onBackPressed()
        bookViewModel.setBook(Book(-1,"","", ""))
    }


    override fun doSearch(){
        startForResult.launch(Intent(this, BookSearchActivity::class.java))
    }
}
