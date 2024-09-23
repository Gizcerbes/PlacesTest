package com.uogames.balinasoft.test.ui.fragment.content.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uogames.balinasoft.core.model.image.Image
import com.uogames.balinasoft.test.R
import com.uogames.balinasoft.test.databinding.CardPhotoBinding
import com.uogames.balinasoft.test.ui.util.LocalDateTimeExchange.fromEpochMilliseconds
import com.uogames.balinasoft.test.ui.util.LocalDateTimeExchange.toShortText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone

class LocationsAdapter(
    private val callSize: () -> Int,
    private val callItem: (position: Int) -> Image?
) : RecyclerView.Adapter<LocationsAdapter.Holder>() {


    private val recyclerScope = CoroutineScope(Dispatchers.Main)
    private val _onDelete = MutableSharedFlow<Int>()
    val onDelete = _onDelete.asSharedFlow()

    private val _onOpen = MutableSharedFlow<Int>()
    val onOpen = _onOpen.asSharedFlow()

    inner class Holder(
        private val bind: CardPhotoBinding
    ) : RecyclerView.ViewHolder(bind.root) {

        fun onShow(position: Int) {
            val image = callItem(position) ?: return
            bind.tvDate.text = LocalDateTime
                .fromEpochMilliseconds(image.date * 1000, TimeZone.currentSystemDefault())
                .toShortText()

            Glide.with(itemView.context)
                .load(image.url)
                .error(R.drawable.broken_image)
                .into(bind.ivImage)

            bind.mcvCard.setOnClickListener { recyclerScope.launch { _onOpen.emit(image.id) } }
            bind.mcvCard.setOnLongClickListener {
                recyclerScope.launch { _onDelete.emit(image.id) }
                true
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(CardPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return callSize()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onShow(position)
    }


}