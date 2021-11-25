package kr.hs.narsha_ls

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kr.hs.narsha_ls.const.Const
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class RegisterLayout : AppCompatActivity() {
    var context:Context = this;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_layout)

        var button = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.RegisterBtn)
        button.setOnClickListener {
            val registerid = findViewById<EditText>(R.id.RegisterID)
            val password = findViewById<EditText>(R.id.RegisterPassword)
            val Rpassword = findViewById<EditText>(R.id.RegisterRPassword)

            if(password.text.toString() == Rpassword.text.toString()) {
                UserCheck(). execute(registerid.text.toString(), password.text.toString())
            }
            else
                Toast.makeText(this, "패스워드가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    inner class UserCheck : AsyncTask<String, String, String>(){

        //var urlen = "http://10.80.163.166:3000/users"
//        var urlen = "http://10.80.163.166:3000/join?id=test191&password=1234"
        var urlen = Const.SERVER+"/join?"
        override fun doInBackground(vararg p0: String?): String {
            try {
                Log.d("test", "button click1 : "+p0[0]+" , "+ p0[1]);
                urlen = urlen + "id="+p0[0] +"&password="+ p0[1];
                Log.d("test", "button click11 : "+urlen);
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
                    var res = content.toString();
                    runOnUiThread(){
                        if(res.equals("ok")){
                            Toast.makeText(context,"가입성공", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegisterLayout,LoginLayout::class.java))
                            finish()
                        }
                        else if(res.equals("failed")){
                            Toast.makeText(context,"이미 가입된 아이디입니다.", Toast.LENGTH_SHORT).show()
                        }
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