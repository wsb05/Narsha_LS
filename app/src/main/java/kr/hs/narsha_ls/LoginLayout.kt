package kr.hs.narsha_ls

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kr.hs.narsha_ls.const.Const
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginLayout : AppCompatActivity() {

    val MY_PERMISSION_ACCESS_ALL = 100

    var context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED|| ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            var permissions = arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, MY_PERMISSION_ACCESS_ALL)
        }

        val loginbutton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_login)
        loginbutton.setOnClickListener{
            val loginID = findViewById<EditText>(R.id.et_id)
            val loginPassword = findViewById<EditText>(R.id.et_password)
            UserCheck().execute(loginID.text.toString(), loginPassword.text.toString())
        }

        val registerbutton = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_register)
        registerbutton.setOnClickListener{
            startActivity(Intent(this@LoginLayout,RegisterLayout::class.java))
            finish()
        }

        val password = findViewById<EditText>(R.id.et_password)
        password.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val loginID = findViewById<EditText>(R.id.et_id)
                UserCheck().execute(loginID.text.toString(), password.text.toString())
            }
            true
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode === MY_PERMISSION_ACCESS_ALL){
            if(grantResults.size > 0){
                for(grant in grantResults){
                    if(grant!=PackageManager.PERMISSION_GRANTED) System.exit(0)
                }
            }
        }
    }

    inner class UserCheck : AsyncTask<String, String, String>(){

        //var urlen = "http://10.80.163.166:3000/users"
        //var urlen = "http://10.80.163.166:3000/join?id=test191&password=1234"
        //var urlen = "http://10.80.161.186:3000/login?"
        var urlen = Const.SERVER+"/login?"
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