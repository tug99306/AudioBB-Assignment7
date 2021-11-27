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
import edu.temple.audlibplayer.PlayerService
import org.w3c.dom.Text


class MainActivity : AppCompatActivity(), BookListFragment.EventInterface, BookListFragment.Search, ControlFragment.ControlClick{

    private lateinit var startForResult: ActivityResultLauncher<Intent>
    var connection = false
    private lateinit var bookListFragment: BookListFragment
    private lateinit var controlBinder: PlayerService.MediaControlBinder

    companion object{
        const val BOOKLISTFRAGMENTKEY = "BookListFragment"
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
    val durationBarHandler = Handler(Looper.getMainLooper()){
        if (it.obj != null){
            val audioDurationObj = it.obj as PlayerService.BookProgress
            val durationTime = audioDurationObj.progress
            //val duration = selectedBookView.getBook().value?.duration

            var durationText = findViewById<TextView>(R.id.durationText)
            durationText.text = durationTime.toString()

            var durationBar = findViewById<SeekBar>(R.id.durationBar)
            durationBar.progress = durationTime
        }
        true
    }
    val serviceConnection = object: ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            connection = true
            controlBinder = service as PlayerService.MediaControlBinder
            controlBinder.setProgressHandler(durationBarHandler)
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
        super.onBackPressed()
        selectedBookView.setBook(null)
    }

    override fun selectionMade(book: Book) {
        if (isSingleContainer) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView1, BookDetailsFragment())
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit()
        }
    }



    override fun doSearch(){
        startForResult.launch(Intent(this, BookSearchActivity::class.java))
    }

    override fun playClick(durationTime: Int) {
        val selectedBook = selectedBookView.getBook().value
        if(selectedBook != null){
            if (durationTime > 0){
                controlBinder.seekTo(durationTime)
                controlBinder.pause()
            } else {
                controlBinder.play(selectedBook.id)
            }
        }
    }

    override fun pauseClick() {
        controlBinder.pause()
    }

    override fun stopClick() {
        controlBinder.stop()
    }

    // Implemented in ControlClick Interface
    override fun seekBarClick() {
    }
}
