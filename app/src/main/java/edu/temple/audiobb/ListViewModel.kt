package edu.temple.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListViewModel: ViewModel() {

    private val bookList: MutableLiveData<BookList> by lazy{
        MutableLiveData<BookList>()
    }
    fun setList(list: BookList): Unit{
        this.bookList.value = list
    }
    fun getList(): LiveData<BookList>{
        return bookList
    }
}