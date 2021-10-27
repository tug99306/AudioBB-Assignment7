package edu.temple.audiobb
import java.io.Serializable

class BookList : Serializable {

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


}