package com.rubenshardt.newsapp.modules.article

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.rubenshardt.newsapp.R
import com.rubenshardt.newsapp.databinding.ActivityArticleBinding
import com.rubenshardt.newsapp.models.Article
import com.rubenshardt.newsapp.utils.Constants
import com.rubenshardt.newsapp.utils.parcelable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private var viewModel: ArticleViewModel? = null
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProvider(this)[ArticleViewModel::class.java]
        intent.parcelable<Article>(Constants.ARTICLE)?.let { viewModel?.setup(it) }

        setupObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        if (item.itemId == R.id.markAsRead) {
            viewModel?.markAsReadClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupObservers() {
        viewModel?.article?.observe(this) {article ->
            with(binding) {
                articleImageView.load(article.imageUrl) {
                    crossfade(600)
                    error(R.drawable.ic_error_drawable)
                }
                dateTextView.text = article.formattedDate
                nameTextView.text = article.title
                authorTextView.text = getString(R.string.by_author, article.author)
                contentTextView.text = article.content
                readImageView.isVisible = article.read
                val titleRes = if (article.read) R.string.mark_as_unread else R.string.mark_as_read
                menu?.findItem(R.id.markAsRead)?.title = getString(titleRes)
            }
        }
    }
}