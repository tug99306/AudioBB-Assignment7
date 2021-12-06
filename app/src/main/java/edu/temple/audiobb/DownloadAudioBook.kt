package edu.temple.audiobb

import android.content.Context
import android.os.AsyncTask
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.service.controls.Control
import java.io.BufferedInputStream
import java.net.URL

class DownloadAudioBook (val context: Context, bookId: String): AsyncTask<String, String, String>(){

    var fileName = bookId

    override fun doInBackground(vararg p0: String?): String {
        val url = URL(p0[0])
        val buffer = ByteArray(1024)
        val connect = url.openConnection()
        connect.connect()
        val inputStream = BufferedInputStream(url.openStream())
        val fileName = "${this.fileName}.mp3"
        val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
        var count = inputStream.read(buffer)
        var total = count

        //Write method
        while (count != -1){
            outputStream.write(buffer, 0 , count)
            count = inputStream.read(buffer)
            total += count
        }

        outputStream.flush()
        outputStream.close()
        inputStream.close()
        println("File saved")
        return "Complete"
    }

}