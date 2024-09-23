package com.uogames.balinasoft.test.ui.util

import com.google.android.material.tabs.TabLayout

class TabLayoutOnSelectedListener(
    private val onSelected: (TabLayout.Tab?) -> Unit = {},
    private val onUnselected: (TabLayout.Tab?) -> Unit = {},
    private val onReselected: (TabLayout.Tab?) -> Unit = {}
) : TabLayout.OnTabSelectedListener {
    override fun onTabSelected(tab: TabLayout.Tab?) {
        onSelected(tab)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        onUnselected(tab)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        onReselected(tab)
    }
}