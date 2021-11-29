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











class Writingscreenactivity : AppCompatActivity() {
    var context: Context = this
    private lateinit var imageView: ImageView ;
    private lateinit var bitmap: Bitmap ;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.writingscreen)
        var button = findViewById<Button>(R.id.post)
        button.setOnClickListener{
            val text = findViewById<EditText>(R.id.substance)
            Post(). execute("anonymous", text.text.toString())

        }
        imageView = findViewById<ImageView>(R.id.Camera)
        imageView.setOnClickListener{
            getPickImageChooserIntent();

        }



    }

    inner class Post : AsyncTask<String, String, String>(){
        var urlen = Const.SERVER+"/write?"
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
                    multipartImageUpload();
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
            val drawable = imageView.drawable as BitmapDrawable
            bitmap = drawable.bitmap



    }

    //이미지 가져오기
    fun getPickImageChooserIntent() {
//        val allIntents: List<Intent> = ArrayList()
//        val packageManager = packageManager
//        val images: ArrayList<Image> = ArrayList()
//        //Intent intent = new Intent(Intent.ACTION_PICK);
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//        //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//        startActivityForResult()
//        startActivityForResult(Intent.createChooser(intent, "이미지 다중 선택"), Const.REQUEST_CODE)

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
            val file = File(filesDir, filesDir.name + ".png")
            val bos = ByteArrayOutputStream()
            if(bitmap == null)
                return
//            mBitmap.get(index).compress(Bitmap.CompressFormat.PNG, 0, bos)
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapdata: ByteArray = bos.toByteArray()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
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
                        }
                        Toast.makeText(
                            applicationContext,
                            response.code().toString() + "",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
        //                    textView.setText("uploaded fail")
        //                    textView.setTextColor(Color.RED)
                        Toast.makeText(applicationContext, "req fail", Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
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