package edu.temple.audiobb

class BookList(val _bookList: ArrayList<Book>) {

    fun add(_book : Book){
        _bookList.add(_book)
    }

    fun remove(_book : Book){
        _bookList.remove(_book)
    }
    fun getBook(int: Int) : Book{
        return _bookList[int]
    }

    fun getSize() : Int{
        return _bookList.size
    }
}