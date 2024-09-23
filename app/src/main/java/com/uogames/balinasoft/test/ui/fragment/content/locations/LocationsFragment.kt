package com.uogames.balinasoft.test.ui.fragment.content.locations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.uogames.balinasoft.test.R
import com.uogames.balinasoft.test.databinding.FragmentLocationsBinding
import com.uogames.balinasoft.test.ui.fragment.photo_detail.PhotoDetailFragment
import com.uogames.balinasoft.test.ui.util.NavigationUtil.navigateNavHost
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LocationsFragment : Fragment() {

    private var _bind: FragmentLocationsBinding? = null
    private val bind get() = _bind!!

    private val vm by viewModels<LocationsViewModel>()

    private var observer: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = FragmentLocationsBinding.inflate(inflater, container, false)
        return _bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.swipe.setOnRefreshListener { vm.refresh() }

    }

    override fun onStart() {
        bind.rvContent.adapter = vm.adapter
        bind.rvContent.layoutManager?.onRestoreInstanceState(vm.recyclerStat)
        observer = lifecycleScope.launch {
            vm.errorFlow.onEach {
                Snackbar.make(
                    requireView(),
                    it.message ?: getString(R.string.got_error),
                    Snackbar.LENGTH_SHORT
                ).show()
            }.launchIn(this)

            vm.isBusy.onEach {
                bind.swipe.isRefreshing = it
            }.launchIn(this)
            vm.imageListFlow.map { it.size }.combine(vm.isBusy) { f, s ->
                if (!s) {
                    if (f == 0) bind.tvEmptyList.visibility = View.VISIBLE
                    else bind.tvEmptyList.visibility = View.GONE
                    bind.rvContent.stopScroll()
                    bind.rvContent.adapter?.notifyDataSetChanged()
                }
            }.launchIn(this)
            vm.adapter.onDelete.onEach {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.delete)
                    .setMessage(R.string.do_you_really_wanna_delete_it)
                    .setPositiveButton(R.string.yes) { _, _ -> vm.delete(it) }
                    .setNegativeButton(R.string.no) { _, _ -> }
                    .show()
            }.launchIn(this)
            vm.adapter.onOpen.onEach { photoID ->
                navigateNavHost(
                    R.id.photoDetailFragment,
                    bundleOf(PhotoDetailFragment.IMAGE_ID to photoID)
                )
            }.launchIn(this)
        }

        super.onStart()
    }


    override fun onStop() {
        observer?.cancel()
        vm.recyclerStat = bind.rvContent.layoutManager?.onSaveInstanceState()
        bind.rvContent.adapter = null
        super.onStop()
    }


    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }


}