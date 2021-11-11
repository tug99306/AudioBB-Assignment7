package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class BookListFragment : Fragment() {
    private lateinit var  viewModel : BookViewModel
    private lateinit var recycler : RecyclerView
    lateinit var mainBookList: BookList
    lateinit var layout : View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mainBookList = it.getSerializable("bookList") as BookList
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(R.layout.fragment_book_list, container, false)
        //viewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)
        //listViewModel = ViewModelProvider(requireActivity()).get(ListViewModel::class.java)
        //recycler = layout.findViewById(R.id.bookRecycler)
        //recycler.layoutManager = GridLayoutManager(requireContext(),1)

        //val adapter = BookAdapter(requireContext(), mainBookList){
            //position -> clickListener(position)
        //}
        //recycler.adapter = adapter
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler = layout.findViewById(R.id.bookRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = BookAdapter(requireContext(), mainBookList){
            updateViewModel(recycler.getChildAdapterPosition(it))
        }
        recycler.adapter = adapter

        val searchButton = layout.findViewById<Button>(R.id.searchListButton)
        searchButton.setOnClickListener{(requireActivity() as Search).doSearch()}
    }


    private fun clickListener(bookIndex: Int) {
        (activity as EventInterface).selectionMade()
        viewModel.setBook(mainBookList.getBook(bookIndex))
    }

    companion object {
        fun newInstance(_bookList: BookList): BookListFragment {
            val frag = BookListFragment().apply {
                mainBookList = _bookList

                arguments = Bundle().apply {
                    putSerializable("bookList", mainBookList)
                }
            }
            return frag
        }
    }

    interface EventInterface{
        fun selectionMade()
    }
    interface Search{
        fun doSearch()
    }

    private fun updateViewModel(index:Int){
        ViewModelProvider(requireActivity())
            .get(BookViewModel::class.java)
            .setBook(mainBookList.getBook(index))
        (requireActivity() as EventInterface).selectionMade()
    }

    fun updateBookList(bookList: BookList){
        this.mainBookList = bookList
        val adapter = BookAdapter(requireContext(), bookList){
            updateViewModel(recycler.getChildAdapterPosition(it))
        }
        recycler.adapter = adapter
        arguments = Bundle().apply{
            putSerializable("bookList", bookList)
        }
    }

}