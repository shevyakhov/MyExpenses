package com.chelz.features.profile.presentation.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chelz.features.profile.R
import com.chelz.features.profile.domain.entity.ArticlesEntity
import java.net.HttpURLConnection
import java.net.URL

class ArticlesAdapter(private var articles: List<ArticlesEntity>) :
	RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder>() {

	class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		val imageViewArticle: ImageView = itemView.findViewById(R.id.imageViewArticle)
		val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
		val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
		return ArticleViewHolder(view)
	}

	override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
		val article = articles[position]

		// Load image using Glide
		Glide.with(holder.itemView)
			.load(article.urlToImage)
			.into(holder.imageViewArticle)

		holder.textViewTitle.text = article.title
		holder.textViewDescription.text = article.description

		// Set click listener to open the article URL in a browser
		holder.itemView.setOnClickListener {
			val context = holder.itemView.context
			val articleUrl = article.url
			if (!articleUrl.isNullOrBlank()) {
				val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(articleUrl))
				context.startActivity(browserIntent)
			}
		}
	}

	override fun getItemCount(): Int {
		return articles.size
	}

	fun getFinalRedirectedUrl(initialUrl: String): String {
		var urlConnection: HttpURLConnection? = null

		try {
			var currentUrl = initialUrl
			var redirected: Boolean

			do {
				val url = URL(currentUrl)
				urlConnection = url.openConnection() as HttpURLConnection
				urlConnection.instanceFollowRedirects = false
				val statusCode = urlConnection.responseCode

				if (statusCode == HttpURLConnection.HTTP_MOVED_PERM || statusCode == HttpURLConnection.HTTP_MOVED_TEMP || statusCode == HttpURLConnection.HTTP_SEE_OTHER) {
					redirected = true
					currentUrl = urlConnection.getHeaderField("Location")
				} else {
					redirected = false
				}
			} while (redirected)

			return currentUrl
		} finally {
			urlConnection?.disconnect()
		}
	}

	fun submitData(it: List<ArticlesEntity>) {
		articles = it
		notifyDataSetChanged()
	}
}