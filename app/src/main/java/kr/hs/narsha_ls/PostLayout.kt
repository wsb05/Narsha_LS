package kr.hs.narsha_ls

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kr.hs.narsha_ls.adapter.PostAdepter
import kr.hs.narsha_ls.adapter.PostData
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class PostLayout : AppCompatActivity() {
    lateinit var postAdepter: PostAdepter
    val datas = mutableListOf<PostData>()
    lateinit var rv_post : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_layout)
        loadpost().execute("")

        val writingBtn = findViewById<Button>(R.id.writing_btn)
        writingBtn.setOnClickListener{
            startActivity(Intent(this, Writingscreenactivity::class.java))
        }

        val LogoutTv = findViewById<TextView>(R.id.LogoutTv)
        LogoutTv.setOnClickListener{
            startActivity(Intent(this, LoginLayout::class.java))
            finish()
        }
        rv_post = findViewById<RecyclerView>(R.id.rv_post)
        initRecycler();
    }

    private fun initRecycler() {
        postAdepter = PostAdepter(this)
        rv_post.adapter = postAdepter


        datas.apply {
//            add(PostData(name_TV = "test1", postcontents_TV = "1234"))
//            add(PostData(name_TV = "test2", postcontents_TV = "1234"))
//            add(PostData(name_TV = "test3", postcontents_TV = "1234"))

            postAdepter.datas = datas
            postAdepter.notifyDataSetChanged()

        }
    }


    inner class loadpost : AsyncTask<String, String, String>(){

        //var urlen = "http://10.80.163.166:3000/users"
        //var urlen = "http://10.80.163.166:3000/join?id=test191&password=1234"
        var urlen = "http://10.80.161.186:3000/read"
        override fun doInBackground(vararg p0: String?): String {
            try {
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
                    var res = content.toString();
                    runOnUiThread(){
                        Log.d("TEST", res)
                    }
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