package com.rubenshardt.newsapp.modules.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rubenshardt.newsapp.R
import com.rubenshardt.newsapp.databinding.ItemArticleBinding
import com.rubenshardt.newsapp.models.Article

class ArticleViewHolder(private val binding: ItemArticleBinding, val onClick: (Article) -> Unit) :
    RecyclerView.ViewHolder(binding.root) {
    private var article: Article? = null

    init {
        itemView.setOnClickListener {
            article?.let {
                onClick(it)
            }
        }
    }

    fun bind(article: Article) {
        this.article = article
        with(binding) {
            articleImageView.load(article.imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_drawable)
            }
            dateTextView.text = article.formattedDate
            nameTextView.text = article.title
            authorTextView.text = article.author
            readImageView.isVisible = article.read == true
        }
    }

    companion object {
        fun from(parent: ViewGroup, onClick: (Article) -> Unit): ArticleViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemArticleBinding.inflate(layoutInflater, parent, false)
            return ArticleViewHolder(binding, onClick)
        }
    }
}