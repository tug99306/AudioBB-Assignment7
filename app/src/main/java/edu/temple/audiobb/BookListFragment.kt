package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val  ARG_PARAM1 = "param1"

class BookListFragment : Fragment() {
    private lateinit var  viewModel : BookViewModel
    private lateinit var recycler : RecyclerView
    private lateinit var mainBookList: BookList
    private lateinit var layout : View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mainBookList = it.getSerializable(ARG_PARAM1) as BookList
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_book_list, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)
        recycler = layout.findViewById(R.id.bookRecycler)
        recycler.layoutManager = GridLayoutManager(requireContext(),1)

        val adapter = BookAdapter(requireContext(), mainBookList){
            position -> clickListener(position)
        }
        recycler.adapter = adapter
        return layout
    }


    private fun clickListener(bookIndex: Int) {
        (activity as EventInterface).selectionMade()
        viewModel.setBook(mainBookList.getBook(bookIndex))
    }

    companion object {
        fun newInstance(param1: BookList) =
            BookListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
    interface EventInterface{
        fun selectionMade()
    }

}