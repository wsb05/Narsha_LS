package kr.hs.narsha_ls

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PostLayout : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_layout)

        val writingBtn = findViewById<Button>(R.id.writing_btn)
        writingBtn.setOnClickListener{
            startActivity(Intent(this, Writingscreenactivity::class.java))
        }

        val LogoutTv = findViewById<TextView>(R.id.LogoutTv)
        LogoutTv.setOnClickListener{
            startActivity(Intent(this, LoginLayout::class.java))
            finish()
        }
    }
}