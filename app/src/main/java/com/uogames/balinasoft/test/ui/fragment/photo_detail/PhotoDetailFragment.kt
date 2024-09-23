package com.uogames.balinasoft.test.ui.fragment.photo_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.uogames.balinasoft.core.usecase.comment.CommentSendUseCase
import com.uogames.balinasoft.test.R
import com.uogames.balinasoft.test.databinding.FragmentPhotoDetailBinding
import com.uogames.balinasoft.test.ui.util.LocalDateTimeExchange.fromEpochMilliseconds
import com.uogames.balinasoft.test.ui.util.LocalDateTimeExchange.toLongText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime

@AndroidEntryPoint
class PhotoDetailFragment : Fragment() {

    companion object {
        const val IMAGE_ID = "PhotoDetailFragment.IMAGE_ID"
    }

    private var _bind: FragmentPhotoDetailBinding? = null
    private val bind get() = _bind!!

    private val vm by viewModels<PhotoDetailViewModel>()

    private var observer: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = FragmentPhotoDetailBinding.inflate(layoutInflater, container, false)
        return _bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageID = arguments?.getInt(IMAGE_ID)
        if (imageID == 0) findNavController().navigateUp()
        vm.imageID.value = imageID ?: 0
        bind.tilComment.editText?.setText(vm.message.value)
        bind.tilComment.editText?.setSelection(vm.message.value.length)
        bind.tilComment.editText?.doOnTextChanged { text, _, _, _ ->
            vm.message.value = text?.toString().orEmpty()
        }
        bind.tilComment.setEndIconOnClickListener { vm.sendComment() }
        bind.mtbBar.setNavigationOnClickListener { findNavController().navigateUp() }
        bind.srlRefresh.setOnRefreshListener { vm.refresh() }
    }

    override fun onStart() {
        bind.rvComments.adapter = vm.adapter
        observer = lifecycleScope.launch {
            vm.image.onEach { image ->
                if (image == null) return@onEach
                Glide.with(requireContext())
                    .load(image.url)
                    .error(R.drawable.broken_image)
                    .into(bind.ivImage)
                bind.tvDate.text = LocalDateTime.fromEpochMilliseconds(image.date * 1000).toLongText()
            }.launchIn(this)

            vm.busy.combine(vm.comments) { f, s ->
                if (!f || s.size != bind.rvComments.adapter?.itemCount) {
                    bind.rvComments.stopScroll()
                    bind.rvComments.adapter?.notifyDataSetChanged()
                }
            }.launchIn(this)

            vm.busy.onEach {
                bind.srlRefresh.isRefreshing = it
                bind.tilComment.isEnabled = !it

            }.launchIn(this)

            vm.adapter.onDelete.onEach {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.delete))
                    .setMessage(getString(R.string.do_you_really_wanna_delete_it))
                    .setPositiveButton(getString(R.string.yes)) { _, _ -> vm.deleteComment(it) }
                    .setNegativeButton(getString(R.string.no)) { _, _ -> }
                    .show()
            }.launchIn(this)

            vm.errorHandler.onEach {
                val message = when (it) {
                    is CommentSendUseCase.TextEmpty -> getString(R.string.text_is_empty)
                    is CommentSendUseCase.Forbidden -> getString(R.string.forbidden)
                    else -> it.message ?: getString(R.string.unknown_error)
                }
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
            }.launchIn(this)

            vm.onClearTextField.onEach {
                bind.tilComment.editText?.setText("")
                val manager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(bind.tilComment.editText?.windowToken, 0)
            }.launchIn(this)
        }
        super.onStart()
    }

    override fun onStop() {
        observer?.cancel()
        bind.rvComments.adapter = null
        super.onStop()
    }

    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }


}