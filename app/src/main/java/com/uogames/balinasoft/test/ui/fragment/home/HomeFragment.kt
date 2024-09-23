package com.uogames.balinasoft.test.ui.fragment.home

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.IdRes
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.google.android.material.snackbar.Snackbar
import com.uogames.balinasoft.core.usecase.image.ImageUploadUseCase
import com.uogames.balinasoft.test.R
import com.uogames.balinasoft.test.databinding.FragmentHomeBinding
import com.uogames.balinasoft.test.ui.animator.PartAnimator
import com.uogames.balinasoft.test.ui.util.LocationService
import com.uogames.balinasoft.test.ui.util.NavigationUtil
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _bind: FragmentHomeBinding? = null
    private val bind get() = _bind!!

    private val vm by viewModels<HomeViewModel>()

    private var observeJob: Job? = null

    @Inject
    lateinit var locationService: LocationService

    private var selectedItem: Int = R.id.photos

    private val contract =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            val loc = it.getOrElse(Manifest.permission.ACCESS_FINE_LOCATION) { false }
            val coarse = it.getOrElse(Manifest.permission.ACCESS_COARSE_LOCATION) { false }
            if (loc) {
                locationService.startService()
                MapKitFactory.getInstance().onStart()
                MapKitFactory.getInstance().onStart()
            }
        }

    private val camera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            vm.load(requireActivity(), imageUri())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = FragmentHomeBinding.inflate(inflater, container, false)
        return _bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedItem = vm.selectedItem.value
        bind.nvMenu.setNavigationItemSelectedListener { item ->
            vm.selectedItem.value = item.itemId
            lifecycleScope.launch {
                delay(200)
                vm.navVisibility.value = false
            }
            true
        }
        bind.rlBlind.setOnClickListener { vm.navVisibility.value = false }
        bind.mtbBar.setNavigationOnClickListener { vm.navVisibility.value = true }
        contract.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION

            )
        )
        bind.fabOpenCamera.setOnClickListener {
            if (locationService.location.value.accuracy > 100) {
                Snackbar.make(
                    requireView(),
                    getString(R.string.we_need_your_location),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else camera.launch(imageUri())
        }
    }

    override fun onStart() {

        observeJob = lifecycleScope.launch {
            vm.navVisibility.onEach {
                val visibility = if (it) View.VISIBLE else View.GONE
                PartAnimator(
                    if (it) R.animator.slide_in else R.animator.slide_out
                ).apply {
                    start(
                        bind.nvMenu,
                        onStart = {
                            if (it) bind.nvMenu.visibility = View.VISIBLE
                        },
                        onEnd = {
                            bind.nvMenu.visibility = visibility
                        }
                    )
                }
            }.launchIn(this)

            vm.username.onEach {
                val un = bind.nvMenu.getHeaderView(0).findViewById<TextView>(R.id.tv_username)
                un.text = it
            }.launchIn(this)

            vm.selectedItem.onEach {
                val navigateID = when (it) {
                    R.id.map -> R.id.mapFragment
                    else -> R.id.locationsFragment
                }

                val nav =
                    when (getIndexOfNavigationId(selectedItem).compareTo(getIndexOfNavigationId(it))) {
                        1 -> NavigationUtil.getLeftNavOptions()
                        -1 -> NavigationUtil.getRightNavOptions()
                        else -> navOptions {  }
                    }
                if (selectedItem != it) bind.fcvHomeContent
                    .findNavController()
                    .navigate(navigateID, null, nav)
                selectedItem = it
            }.launchIn(this)

            vm.isUploading.onEach {
                val visibility = if (it) View.VISIBLE else View.GONE
                bind.progressCard.visibility = visibility
            }.launchIn(this)

            vm.navVisibility.combine(vm.isUploading) { f, s ->
                bind.rlBlind.visibility = if (f || s) View.VISIBLE else View.GONE
            }.launchIn(this)

            vm.progress.onEach {
                bind.progress.progress = (100 * it).toInt()
            }.launchIn(this)

            vm.errorHandler.onEach {
                val message = when (it) {
                    is ImageUploadUseCase.TooBig -> requireContext().getString(R.string.the_file_is_too_big)
                    is ImageUploadUseCase.TooSmall -> requireContext().getString(R.string.the_file_is_too_small)
                    else -> it.message ?: requireContext().getString(R.string.unknown_error)
                }
                Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
            }.launchIn(this)
        }
        super.onStart()
    }

    override fun onStop() {
        observeJob?.cancel()
        super.onStop()
    }


    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }

    private fun imageUri(): Uri {
        val file = File(
            requireContext().filesDir,
            "ImageTemp.jpg"
        )
        return FileProvider.getUriForFile(
            requireContext(),
            requireContext().applicationContext.packageName + ".provider",
            file
        )
    }

    private fun getIndexOfNavigationId(@IdRes id: Int): Int {
        return when (id) {
            R.id.photos -> 0
            R.id.map -> 1
            else -> 0
        }

    }


}