package kr.hs.narsha_ls.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.hs.narsha_ls.LoginLayout
import kr.hs.narsha_ls.R
import kr.hs.narsha_ls.const.Const

class PostAdepter (private val context: Context) : RecyclerView.Adapter<PostAdepter.ViewHolder>() {

    var datas = mutableListOf<PostData>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name_TV: TextView = itemView.findViewById(R.id.name_TV)
        private val postcontents_TV: TextView = itemView.findViewById(R.id.postcontents_TV)
        private val postcontents_IMG: ImageView = itemView.findViewById(R.id.postcontents_IMG)
        private val comments1_TV: TextView = itemView.findViewById(R.id.comments1_TV)
        private val comments2_TV: TextView = itemView.findViewById(R.id.comments2_TV)
        private val allcomments_TV: TextView = itemView.findViewById(R.id.allcomments_TV)

        fun bind(item: PostData) {
            name_TV.text = item.writer.toString()
            postcontents_TV.text = item.text.toString()
            postcontents_IMG.visibility = View.GONE
            comments1_TV.visibility = View.GONE
            comments2_TV.visibility = View.GONE
            allcomments_TV.visibility = View.GONE
//http://10.80.161.186:3000/getimg?img=1638355246370img.png
            if(item.picture != null && item.picture.length>5){
//                postcontents_IMG.visibility = View.VISIBLE
//                "http://10.80.161.186:3000"
                var url = Const.SERVER+"/getimg?img=" +item.picture
                Glide.with(itemView).load(url).into(postcontents_IMG)

            }
            itemView.setOnClickListener{
//                postcontents_IMG.visibility = View.VISIBLE
                if( postcontents_IMG.visibility == View.VISIBLE){
                    postcontents_IMG.visibility = View.GONE
                }else{
                    postcontents_IMG.visibility = View.VISIBLE
                }
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.post_recycler_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    override fun getItemCount(): Int {
        if(datas != null)
            return datas.size
        else
            return 0
    }
}