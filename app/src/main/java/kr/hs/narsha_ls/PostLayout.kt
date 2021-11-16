package kr.hs.narsha_ls

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.hs.narsha_ls.adapter.PostAdepter
import kr.hs.narsha_ls.adapter.PostData

class PostLayout : AppCompatActivity() {
    lateinit var postAdepter: PostAdepter
    val datas = mutableListOf<PostData>()
    lateinit var rv_post : RecyclerView

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
        rv_post = findViewById<RecyclerView>(R.id.rv_post)
        initRecycler();
    }

    private fun initRecycler() {
        postAdepter = PostAdepter(this)
        rv_post.adapter = postAdepter


        datas.apply {
            add(PostData(name_TV = "test1", postcontents_TV = "1234"))
            add(PostData(name_TV = "test2", postcontents_TV = "1234"))
            add(PostData(name_TV = "test3", postcontents_TV = "1234"))

            postAdepter.datas = datas
            postAdepter.notifyDataSetChanged()

        }
    }
}