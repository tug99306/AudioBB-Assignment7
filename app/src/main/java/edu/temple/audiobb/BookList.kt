package edu.temple.audiobb
import androidx.lifecycle.ViewModel
import java.io.Serializable

class BookList : ViewModel(), Serializable {
    companion object {
        const val BOOKLIST_KEY = "bookList"
    }
    private val bookList = ArrayList<Book>()

    fun add(_book : Book){
        bookList.add(_book)
    }

    fun getBook(int: Int) : Book{
        return bookList[int]
    }

    fun getSize() : Int{
        return bookList.size
    }

    fun clear(){
        bookList.clear()
    }

    fun addBooks (newBookList: BookList){
        bookList.clear()
        bookList.addAll(newBookList.bookList)
    }


}