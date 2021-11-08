package edu.temple.audiobb

class Book (val title : String, val author : String, val id : Int, val coverURL : String){

    fun getBookTitle() : String{
        return title
    }
    fun getBookAuthor() : String {
        return author
    }
}