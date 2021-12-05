package edu.temple.audiobb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import edu.temple.audlibplayer.PlayerService


class MainActivity : AppCompatActivity(), BookListFragment.EventInterface, ControlFragment.ControlInterface{

    //private lateinit var startForResult: ActivityResultLauncher<Intent>
    private lateinit var serviceIntent : Intent
    var connection = false
    private lateinit var bookListFragment: BookListFragment
    private lateinit var mediaControlBinder: PlayerService.MediaControlBinder

    companion object{
        const val BOOKLISTFRAGMENTKEY = "BookListFragment"
    }

    private val playingBookViewModel : PlayingBookViewModel by lazy {
        ViewModelProvider(this).get(PlayingBookViewModel::class.java)
    }
    private val isSingleContainer : Boolean by lazy{
        findViewById<View>(R.id.fragmentContainerView2) == null
    }

    private val bookViewModel : BookList by lazy{
        ViewModelProvider(this).get(BookList::class.java)
    }

    private val selectedBookView : BookViewModel by lazy{
        ViewModelProvider(this).get(BookViewModel::class.java)
    }

    val durationBarHandler = Handler(Looper.getMainLooper()){ msg ->
        msg.obj?.let { msgObj ->
            val bookProgress = msgObj as PlayerService.BookProgress

            if (playingBookViewModel.getPlayingBook().value == null) {
                Volley.newRequestQueue(this)
                    .add(JsonObjectRequest(Request.Method.GET, API.getBookDataUrl(bookProgress.bookId), null, { jsonObject ->
                        playingBookViewModel.setPlayingBook(Book(jsonObject))
                        // If no book is selected (if activity was closed and restarted)
                        // then use the currently playing book as the selected book.
                        // This allows the UI to display the book details
                        if (selectedBookView.getBook().value == null) {
                            // set book
                            selectedBookView.setBook(playingBookViewModel.getPlayingBook().value)
                            // display book - this function was previously implemented as a callback for
                            // the BookListFragment, but it turns out we can use it here - Don't Repeat Yourself
                            selectionMade()
                        }
                    }, {}))
            }
            supportFragmentManager.findFragmentById(R.id.controlContainer)?.run{
                with (this as ControlFragment) {
                    playingBookViewModel.getPlayingBook().value?.also {
                        setPlayProgress(((bookProgress.progress / it.duration.toFloat()) * 100).toInt())
                    }
                }
            }
        }
        true
    }


    private val serviceConnection = object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            connection = true
            mediaControlBinder = service as PlayerService.MediaControlBinder
            mediaControlBinder.setProgressHandler(durationBarHandler)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            connection = false
        }
    }


    private val searchRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        supportFragmentManager.popBackStack()
        it.data?.run{
            bookViewModel.addBooks(getSerializableExtra(BookList.BOOKLIST_KEY) as BookList)
            bookListFragment.bookListUpdate()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playingBookViewModel.getPlayingBook().observe(this, {
            (supportFragmentManager.findFragmentById(R.id.controlContainer) as ControlFragment).setNowPlaying(it.title)
        })

        serviceIntent = Intent(this, PlayerService::class.java)

        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE)

        supportFragmentManager.beginTransaction().add(R.id.controlContainer, ControlFragment()).commit()
        bindService(Intent(this, PlayerService::class.java), serviceConnection, BIND_AUTO_CREATE)

        if (supportFragmentManager.findFragmentById(R.id.fragmentContainerView1) is BookDetailsFragment
            && selectedBookView.getBook().value != null) {
            supportFragmentManager.popBackStack()
        }

        if (savedInstanceState == null) {
            bookListFragment = BookListFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView1, bookListFragment, BOOKLISTFRAGMENTKEY)
                .commit()
        } else {
            bookListFragment = supportFragmentManager.findFragmentByTag(BOOKLISTFRAGMENTKEY) as BookListFragment

            if (isSingleContainer && selectedBookView.getBook().value != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView1, BookDetailsFragment())
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit()
            }
        }

        if (!isSingleContainer && supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) !is BookDetailsFragment)
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainerView2, BookDetailsFragment())
                .commit()

        findViewById<Button>(R.id.mainSearchButton).setOnClickListener {
            searchRequest.launch(Intent(this, BookSearchActivity::class.java))
        }
    }

    override fun onBackPressed(){
        selectedBookView.setBook(null)
        super.onBackPressed()
    }

    override fun selectionMade() {
        if (isSingleContainer) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }



    override fun play() {
        if(connection && selectedBookView.getBook().value != null){
            mediaControlBinder.play(selectedBookView.getBook().value!!.id)
            playingBookViewModel.setPlayingBook(selectedBookView.getBook().value)
            startService(serviceIntent)
        }
    }

    override fun pause() {
        if (connection) {
            mediaControlBinder.pause()
        }
    }

    override fun stop() {
        if (connection){
            mediaControlBinder.stop()
            stopService(serviceIntent)
        }
    }

    // Implemented in ControlClick Interface
    override fun seek(position : Int) {
        if (connection && mediaControlBinder.isPlaying){
            mediaControlBinder.seekTo(
                (playingBookViewModel.getPlayingBook().value!!.duration * (position.toFloat()/100)).toInt())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }
}
