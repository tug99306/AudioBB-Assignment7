package edu.temple.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BookViewModel : ViewModel() {

    private var isEmpty : Boolean = true

    private val book : MutableLiveData<Book> by lazy {
        MutableLiveData<Book>()
    }

    fun getBook() : LiveData<Book>{
        return book
    }

    fun setBook(_book: Book){
        book.value = _book
        isEmpty = false
    }

    fun isBookEmpty() : Boolean{
        return isEmpty
    }
}