package edu.temple.audiobb

import android.nfc.tech.TagTechnology
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [BookDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookDetailsFragment : Fragment() {

    lateinit var bookName: TextView
    lateinit var bookAuthor : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookName = view.findViewById(R.id.detailBookTitle)
        bookAuthor = view.findViewById(R.id.detailBookAuthor)
        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getBook()
            .observe(requireActivity()) {
                bookDetails(it)
            }
    }

    private fun bookDetails (_book : Book){
        bookName.text = _book.title
        bookAuthor.text = _book.author
    }


}