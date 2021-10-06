package kr.hs.narsha_ls

import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.CookieManager
import java.net.HttpURLConnection
import java.net.URL

class RegisterLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_layout)

        var button = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.RegisterBtn);
        button.setOnClickListener {
            Log.d("test", "button click")
            UserCheck(). execute("");
        }
    }

    class UserCheck : AsyncTask<String, String, String>(){
        var urlen = "http://10.80.163.166:3000/users"
        override fun doInBackground(vararg p0: String?): String {
            try {
                Log.d("test", "button click1")
                val url = URL(urlen)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"

                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    // 데이터 읽기
                    val streamReader = InputStreamReader(urlConnection.inputStream)
                    val buffered = BufferedReader(streamReader)

                    val content = StringBuilder()
                    while(true) {
                        val line = buffered.readLine() ?: break
                        content.append(line)
                    }
                    Log.d("test", "button click3 : "+content.toString())
                    // 스트림과 커넥션 해제
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