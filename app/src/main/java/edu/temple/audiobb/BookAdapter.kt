package edu.temple.audiobb

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView

class BookAdapter(_context: Context, private var _bookList:BookList, private val clickListener : (bookInt: Int) -> Unit) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private val inflater = LayoutInflater.from(_context)

    class BookViewHolder(itemView: View, val clickListener : (bookInt:Int) -> Unit) :  RecyclerView.ViewHolder(itemView){
        val bookTitleView : TextView = itemView.findViewById(R.id.bookTitleView)
        val bookAuthorView : TextView = itemView.findViewById(R.id.bookAuthorView)

        init {
            itemView.setOnClickListener {
                clickListener(adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookAdapter.BookViewHolder {
        val view = inflater.inflate(R.layout.bookrecycler, null)
        return BookViewHolder(view, clickListener)
    }

    override fun onBindViewHolder(holder: BookAdapter.BookViewHolder, position: Int) {
        holder.bookTitleView.text = _bookList.getBook(position).title
        holder.bookAuthorView.text = _bookList.getBook(position).author
    }

    override fun getItemCount(): Int {
        return _bookList.getSize()
    }
}