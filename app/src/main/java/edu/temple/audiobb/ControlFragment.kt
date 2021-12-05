package edu.temple.audiobb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView


class ControlFragment : Fragment() {

    private lateinit var playButton: Button
    private lateinit var stopButton: Button
    private lateinit var pauseButton: Button
    var durationBar: SeekBar? = null
    var titleText: TextView? = null
    lateinit var durationText: TextView
    //var durationInt: Int = 0

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

        durationBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    (activity as ControlInterface).seek(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }


            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        val onClickListener = View.OnClickListener {
            val parent = activity as ControlInterface
            when (it.id) {
                R.id.playButton -> parent.play()
                R.id.pauseButton -> parent.pause()
                R.id.stopButton -> parent.stop()
            }
        }

        playButton.setOnClickListener(onClickListener)
        pauseButton.setOnClickListener(onClickListener)
        stopButton.setOnClickListener(onClickListener)

        return layout
    }
    fun setNowPlaying(title: String) {
        titleText?.text = title
    }

    fun setPlayProgress(progress: Int) {
        durationBar?.setProgress(progress, true)
    }
        interface ControlInterface{
            fun play()
            fun pause()
            fun stop()
            fun seek(position: Int)
        }

}