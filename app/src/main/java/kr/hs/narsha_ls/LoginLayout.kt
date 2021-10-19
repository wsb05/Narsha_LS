package kr.hs.narsha_ls

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginLayout : AppCompatActivity() {
    var context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginbutton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_login)
        loginbutton.setOnClickListener{
            val loginID = findViewById<EditText>(R.id.et_id)
            val loginPassword = findViewById<EditText>(R.id.et_password)
            UserCheck(). execute(loginID.text.toString(), loginPassword.text.toString())
        }

        val registerbutton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_register)
        registerbutton.setOnClickListener{
            startActivity(Intent(this@LoginLayout,RegisterLayout::class.java))
            finish()
        }
    }

    inner class UserCheck : AsyncTask<String, String, String>(){

        //var urlen = "http://10.80.163.166:3000/users"
        //var urlen = "http://10.80.163.166:3000/join?id=test191&password=1234"
        var urlen = "http://10.80.161.186:3000/login?"
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
                        if(res.equals("1")){
                            Toast.makeText(context,"로그인 성공", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginLayout,PostLayout::class.java))
                            finish()
                        }
                        else if(res.equals("0")){
                            startActivity(Intent(this@LoginLayout,RegisterLayout::class.java))
                            Toast.makeText(context,"없는 아이디 또는 패스워드입니다.", Toast.LENGTH_SHORT).show()
                            finish()
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