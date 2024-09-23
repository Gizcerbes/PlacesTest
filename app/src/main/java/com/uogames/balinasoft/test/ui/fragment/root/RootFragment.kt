package com.uogames.balinasoft.test.ui.fragment.root

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.uogames.balinasoft.test.Config
import com.uogames.balinasoft.test.R

class RootFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return RelativeLayout(container?.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHost = requireActivity().findNavController(R.id.main_nav_host)
        if (Config.accessToken.value == null) {
            val graph = navHost.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(R.id.authFragment)
            navHost.setGraph(graph, null)
        } else {
            val graph = navHost.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(R.id.homeFragment)
            navHost.setGraph(graph, null)
        }
    }

}