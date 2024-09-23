package com.uogames.balinasoft.test.ui.fragment.content.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.uogames.balinasoft.core.model.image.Image
import com.uogames.balinasoft.test.R
import com.uogames.balinasoft.test.databinding.FragmentMapBinding
import com.uogames.balinasoft.test.ui.fragment.photo_detail.PhotoDetailFragment
import com.uogames.balinasoft.test.ui.util.BitmapUtil
import com.uogames.balinasoft.test.ui.util.LocationService
import com.uogames.balinasoft.test.ui.util.NavigationUtil.navigateNavHost
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment() {

    private var _bind: FragmentMapBinding? = null
    private val bind get() = _bind!!

    @Inject
    lateinit var locationService: LocationService

    private val vm by viewModels<MapViewModel>()

    private var observer: Job? = null

    private val imgProvider by lazy {
        ImageProvider.fromBitmap(
            BitmapUtil.getFromXML(requireContext(), R.drawable.location)
        )
    }

    private val placemarkTapListener = MapObjectTapListener { obj, _ ->
        val location = obj.userData as? Image ?: return@MapObjectTapListener false
        navigateNavHost(
            R.id.photoDetailFragment,
            bundleOf(PhotoDetailFragment.IMAGE_ID to location.id)
        )
        true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bind = FragmentMapBinding.inflate(inflater, container, false)
        return _bind?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.root.mapWindow.map.apply {
            isRotateGesturesEnabled = false
            isFastTapEnabled = false
            isScrollGesturesEnabled = true
            isZoomGesturesEnabled = true
            set2DMode(true)
        }


        val camPosition = when (val r = vm.selected.value) {
            null -> vm.getMyPosition()
            else -> r
        }

        bind.root.mapWindow.map.move(camPosition)

        bind.root.mapWindow.map.addCameraListener { _, cameraPosition, cameraUpdateReason, _ ->
            vm.selected.value = cameraPosition
            when (cameraUpdateReason) {
                CameraUpdateReason.GESTURES -> vm.defaultPosition.value = false
                CameraUpdateReason.APPLICATION -> {}
            }
        }

    }

    override fun onStart() {
        observer = lifecycleScope.launch {
            vm.points.onEach {
                bind.root.mapWindow.map.mapObjects.clear()
                it.forEach { loc ->
                    val point = Point(loc.lat, loc.lng)
                    bind.root.mapWindow.map.mapObjects.addPlacemark().apply {
                        geometry = point
                        setIcon(imgProvider)
                        userData = loc
                        addTapListener(placemarkTapListener)
                    }
                }
            }.launchIn(this)
        }
        super.onStart()
    }

    override fun onStop() {
        observer?.cancel()
        super.onStop()
    }


    override fun onDestroyView() {
        _bind = null
        super.onDestroyView()
    }


}