package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider


class ControlFragment : Fragment() {

    private lateinit var playButton: Button
    private lateinit var stopButton: Button
    private lateinit var pauseButton: Button
    lateinit var durationBar: SeekBar
    private lateinit var titleText: TextView
    lateinit var durationText: TextView
    var durationInt: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val layout = inflater.inflate(R.layout.fragment_control, container, false)

        playButton = layout.findViewById(R.id.playButton)
        pauseButton = layout.findViewById(R.id.pauseButton)
        stopButton = layout.findViewById(R.id.stopButton)
        titleText = layout.findViewById(R.id.titleText)
        durationBar = layout.findViewById(R.id.durationBar)
        durationText = layout.findViewById(R.id.durationText)

        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookViewModel = ViewModelProvider(requireActivity()).get(BookViewModel::class.java)
        playButton.setOnClickListener {
            val selectedBook = bookViewModel.getBook().value

            if(selectedBook != null){
                titleText.text = getString(R.string.nowPlaying) + selectedBook.title
                durationBar.max = selectedBook.duration
            }
            (activity as ControlClick).playClick(durationInt)
        }
        pauseButton.setOnClickListener {
            val selectedBook = bookViewModel.getBook().value
            if (selectedBook != null){
                durationInt = durationBar.progress
            }
            (activity as ControlClick).pauseClick()
        }
        stopButton.setOnClickListener {
            durationInt = 0
            durationBar.progress = 0
            (activity as ControlClick).stopClick()
        }
        durationBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(bar: SeekBar, progress: Int, p2: Boolean) {
                val selectedBook = bookViewModel.getBook().value
                if(selectedBook != null){
                    durationText.text = progress.toString()
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                (activity as ControlClick).pauseClick()
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val selectedBook = bookViewModel.getBook().value
                if(selectedBook != null){
                    durationInt = durationBar.progress
                    durationText.text = durationInt.toString()
                    (activity as ControlClick).playClick(durationInt)
                }
            }
        })
    }

        interface ControlClick{
            fun playClick(durationTime: Int)
            fun pauseClick()
            fun stopClick()
            fun seekBarClick()
        }

}