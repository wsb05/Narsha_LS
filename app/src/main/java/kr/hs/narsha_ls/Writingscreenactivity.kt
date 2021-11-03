package kr.hs.narsha_ls

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Writingscreenactivity : AppCompatActivity() {
    var context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.writingscreen)
        var button = findViewById<Button>(R.id.post)
        button.setOnClickListener{
            val text = findViewById<EditText>(R.id.substance)
            Post(). execute("nickname", text.text.toString())

        }
    }

    inner class Post : AsyncTask<String, String, String>(){
        var urlen = "http://10.80.161.186:3000/write?"
        override fun doInBackground(vararg p0: String?): String {
            try {
                urlen = urlen +"&writer="+ p0[0] + "&text=" + p0[1]
                Log.d("test", "button click11 : "+urlen);
                val url = URL(urlen)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"

                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    // 데이터 읽기
                    val streamReader = InputStreamReader(urlConnection.inputStream)
                    val buffered = BufferedReader(streamReader)

                    val content = StringBuilder()
                    while (true) {
                        val line = buffered.readLine() ?: break
                        content.append(line)
                    }
                    Log.d("test", "button click3 : " + content.toString())
                    runOnUiThread() {
                        startActivity(Intent(this@Writingscreenactivity, PostLayout::class.java))
                        finish()
                    }

                    buffered.close()
                    urlConnection.disconnect()
                }
                // 수행이 끝나고 리턴하는 값은 다음에 수행될 onProgressUpdate 의 파라미터가 된다
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("test", "button click2 : "+e.stackTraceToString())
            } finally {

            }
            return "";
        }

    }
}