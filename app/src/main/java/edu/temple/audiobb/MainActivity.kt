package edu.temple.audiobb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider

class MainActivity : AppCompatActivity(), BookListFragment.EventInterface {

    private var twoFragment = false
    private lateinit var bookViewModel: BookViewModel
    private lateinit var listViewModel: ListViewModel
    var bookList : BookList = BookList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        twoFragment = findViewById<View>(R.id.fragmentContainerView2) != null
        listViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        listViewModel.setList(bookList)

        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            val intent = Intent(this, BookSearchActivity::class.java).apply {
                putExtra("booksList", bookList)
            }
            startActivityForResult(intent, 1)
        }


        //val bookListFragment = BookListFragment.newInstance(booksList)

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
                .replace(R.id.fragmentContainerView1, BookListFragment.newInstance())
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
        bookViewModel.setBook(Book("","",0, ""))
    }

    override fun onResume(){
        super .onResume()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView1, BookListFragment.newInstance())
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            if (data != null){
                val resultBookList = data.getSerializableExtra("list") as BookList
                listViewModel.setList(resultBookList)


            }
        }

    }
}