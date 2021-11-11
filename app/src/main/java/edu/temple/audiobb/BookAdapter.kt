package edu.temple.audiobb

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView

class BookAdapter(_context: Context, _books: BookList, _onClick: View.OnClickListener) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val inflater = LayoutInflater.from(_context)
    private val books = _books
    private val onClick = _onClick


    class BookViewHolder(_itemView: View) : RecyclerView.ViewHolder(_itemView){
        val view = _itemView
        val bookTitleView : TextView = _itemView.findViewById(R.id.bookTitleView)
        val bookAuthorView : TextView = _itemView.findViewById(R.id.bookAuthorView)


        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookViewHolder {
        val view = inflater.inflate(R.layout.bookrecycler, null)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bookTitleView.text = books.getBook(position).title
        holder.bookAuthorView.text = books.getBook(position).author
        holder.view.setOnClickListener(onClick)
    }

    override fun getItemCount(): Int {
        return books.getSize()
    }
}