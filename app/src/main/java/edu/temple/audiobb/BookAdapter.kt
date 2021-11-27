package edu.temple.audiobb

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView

class BookAdapter(_books: BookList, _onClick: (Book) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val books = _books
    private val onClick = _onClick


    class BookViewHolder(_itemView: View, onClick : (Book) -> Unit) : RecyclerView.ViewHolder(_itemView){
        val bookTitleView : TextView
        val bookAuthorView : TextView
        lateinit var book:Book
        init{
            bookTitleView = _itemView.findViewById(R.id.bookTitleView)
            bookAuthorView = _itemView.findViewById(R.id.bookAuthorView)
            bookTitleView.setOnClickListener{
                onClick(book)
            }
        }

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookViewHolder {
        return BookViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bookrecycler, parent, false), onClick)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bookTitleView.text = books.getBook(position).title
        holder.bookAuthorView.text = books.getBook(position).author
        holder.book = books.getBook(position)
    }

    override fun getItemCount(): Int {
        return books.getSize()
    }
}