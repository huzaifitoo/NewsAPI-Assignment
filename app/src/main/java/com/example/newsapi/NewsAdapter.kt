package com.example.newsapi

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.databinding.ItemLayoutBinding
import com.squareup.picasso.Picasso

class NewsAdapter(var context: Context, private var mList: ArrayList<ArticlesItem>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener) {

        mListener = listener
    }


    class NewsViewHolder(private var binding: ItemLayoutBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(list: ArticlesItem) {
            binding.tvAuthor.text = list.author
            binding.tvTitle.text = list.title

            Picasso.get().load(list.urlToImage).into(binding.imageView)
        }
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return NewsViewHolder(binding, mListener)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val list = mList[position]
        holder.bind(list)
    }
}