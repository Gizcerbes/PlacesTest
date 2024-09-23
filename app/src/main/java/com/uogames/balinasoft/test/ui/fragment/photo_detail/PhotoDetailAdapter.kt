package com.uogames.balinasoft.test.ui.fragment.photo_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.uogames.balinasoft.core.model.comment.Comment
import com.uogames.balinasoft.test.databinding.CardCommentBinding
import com.uogames.balinasoft.test.ui.util.LocalDateTimeExchange.fromEpochMilliseconds
import com.uogames.balinasoft.test.ui.util.LocalDateTimeExchange.toLongText
import com.uogames.balinasoft.test.ui.util.LocalDateTimeExchange.toShortText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

class PhotoDetailAdapter(
    private val callSize: () -> Int,
    private val callItem: (position: Int) -> Comment?
) : RecyclerView.Adapter<PhotoDetailAdapter.Holder>() {

    private val recyclerScope = CoroutineScope(Dispatchers.Main)

    private val _onDelete = MutableSharedFlow<Int>()
    val onDelete = _onDelete.asSharedFlow()


    inner class Holder(private val bind: CardCommentBinding) : RecyclerView.ViewHolder(bind.root) {

        fun onShow(position: Int) {
            val item = callItem(position) ?: return
            bind.ivMessage.text = item.text
            bind.tvTime.text = LocalDateTime.fromEpochMilliseconds(
                item.date * 1000,
                TimeZone.currentSystemDefault()
            ).toLongText()
            bind.root.setOnLongClickListener {
                recyclerScope.launch { _onDelete.emit(item.id) }
                true
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            CardCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return callSize()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onShow(position)
    }

}