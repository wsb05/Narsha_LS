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
import kr.hs.narsha_ls.const.Const
import java.net.HttpURLConnection
import java.net.URL

import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import okhttp3.ResponseBody

import okhttp3.RequestBody

import okhttp3.MultipartBody

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kr.hs.narsha_ls.restapi.ApiService
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

import retrofit2.Retrofit
import okhttp3.OkHttpClient
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.ContactsContract


class Writingscreenactivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var imageView: ImageView ;
    private lateinit var writing_IMG: ImageView ;
    private lateinit var bitmap: Bitmap ;
    private lateinit var mUri: Uri;
    private var imgName: String ="";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.writingscreen)
        var button = findViewById<Button>(R.id.post)
        button.setOnClickListener{
            val text = findViewById<EditText>(R.id.substance)
            if(writing_IMG.drawable==null){
                Post(). execute("anonymous", text.text.toString())
            }else {
                multipartImageUpload()
            }


        }
        writing_IMG = findViewById<ImageView>(R.id.writing_IMG)
        imageView = findViewById<ImageView>(R.id.Camera)
        imageView.setOnClickListener{
            getPickImageChooserIntent();

        }



    }

    inner class Post : AsyncTask<String, String, String>(){
        var urlen = Const.SERVER+"/write?"
        override fun doInBackground(vararg p0: String?): String {
            try {
                urlen = urlen +"&writer="+ p0[0] + "&text=" + p0[1]  + "&picture=" + imgName;
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
                        //startActivity(Intent(this@Writingscreenactivity, PostLayout::class.java))

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

    val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            findViewById<ImageView>(R.id.writing_IMG).setImageURI(result.data?.data)
            Log.d("test", "test img : "+ result.data?.data.toString())

//            bitmap = BitmapFactory.decodeFile(result.data?.data.toString())
            val drawable = writing_IMG.drawable as BitmapDrawable
            bitmap = drawable.bitmap
            mUri = result.data?.data!!




    }

    //이미지 가져오기
    fun getPickImageChooserIntent() {


        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        intent.type = "image/*"
        getContent.launch(intent)
    }

    //실제 이미지를 업로드 하는 파트
    //여기서 파일의 이름을 바꿔야함
    private fun multipartImageUpload() {
        try {
            val filesDir: File = applicationContext.filesDir
            //여기서 png 앞에를 유저 id + 레지스터 넘버 이런식으로 바꿀 것
//            val file = File(filesDir, filesDir.name + ".png")
//            Log.d("test", "test  filesDir.listFiles() : "+  filesDir.listFiles())
            imgName = ""+ System.currentTimeMillis() + "img.png"
            val file = File(filesDir, imgName)
//            Log.d("test", "test path.name : "+ path)
//            val file = File(path)
            Log.d("test", "test filesDir.name : "+ file.name)
            val bos = ByteArrayOutputStream()
            if(bitmap == null)
                return
//            mBitmap.get(index).compress(Bitmap.CompressFormat.PNG, 0, bos)
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, bos)

            Log.d("test", "test bitmap.byteCount : "+ bitmap.byteCount)
            Log.d("test", "test bos.toByteArray().size : "+ bos.toByteArray().size)
            val bitmapdata: ByteArray = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            Log.d("test", "test file.length : "+ file.length())
            val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            val body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile)
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload")
            val client = OkHttpClient.Builder().build()
            var apiService =
                Retrofit.Builder().baseUrl(Const.SERVER).client(client).build().create<ApiService>(
                    ApiService::class.java
                )
            val req: Call<ResponseBody?>? = apiService.postImage(body, name)

            if (req != null) {
                req.enqueue(object : Callback<ResponseBody?> {
                    override fun onResponse(
                        call: Call<ResponseBody?>,
                        response: Response<ResponseBody?>
                    ) {
                        if (response.code() == 200) {
        //                        textView.setText("uploaded success")
        //                        textView.setTextColor(Color.BLUE)
                            Log.d("test", "test uploaded success ")
                        }
                        Toast.makeText(
                            applicationContext,
                            response.code().toString() + "",
                            Toast.LENGTH_SHORT
                        ).show()
                        file.delete()
                        Post(). execute("anonymous", findViewById<EditText>(R.id.substance).text.toString())
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        //                    textView.setText("uploaded fail")
        //                    textView.setTextColor(Color.RED)
                        Log.d("test", "test uploaded fail ")
                        Toast.makeText(applicationContext, "req fail", Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
                        file.delete()
                    }
                })
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}