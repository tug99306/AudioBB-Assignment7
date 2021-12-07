package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso

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
    lateinit var bookImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val layout = inflater.inflate(R.layout.fragment_book_details, container, false)
        bookName = layout.findViewById(R.id.detailBookTitle)
        bookAuthor = layout.findViewById(R.id.detailBookAuthor)
        bookImage = layout.findViewById(R.id.coverImageView)

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .getBook()
            .observe(viewLifecycleOwner, {updateDetails(it)})
    }

    private fun updateDetails (book: Book?) {
        book?.run {
            bookName.text = title
            bookAuthor.text = author
            Picasso.get()
                .load(cover_url)
                .into(bookImage)
        }
    }

}