package kr.hs.narsha_ls.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kr.hs.narsha_ls.R

class PostAdepter (private val context: Context) : RecyclerView.Adapter<PostAdepter.ViewHolder>() {

    var datas = mutableListOf<PostData>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name_TV: TextView = itemView.findViewById(R.id.name_TV)
        private val postcontents_TV: TextView = itemView.findViewById(R.id.postcontents_TV)
        private val postcontents_IMG: ImageView = itemView.findViewById(R.id.postcontents_IMG)

        fun bind(item: PostData) {
            name_TV.text = item.title.toString()
            postcontents_TV.text = item.text.toString()
//            Glide.with(itemView).load(item.postcontents_IMG).into(postcontents_IMG)


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