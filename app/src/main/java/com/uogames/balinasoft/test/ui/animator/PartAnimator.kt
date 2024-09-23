package com.uogames.balinasoft.test.ui.animator

import android.animation.AnimatorInflater
import android.view.View
import androidx.annotation.AnimatorRes
import androidx.core.animation.doOnEnd

class PartAnimator(
    @AnimatorRes
    vararg list: Int
) {

    private val list = list.toList()

    fun start(
        view: View,
        onEnd: () -> Unit = {},
        onStart: () -> Unit = {}
    ) {
        if (list.isEmpty()) return
        view.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        var anim = AnimatorInflater.loadAnimator(view.context, list[list.size - 1]).apply {
            setTarget(view)
            doOnEnd {
                view.setLayerType(View.LAYER_TYPE_NONE, null)
                onEnd()
            }
        }
        repeat(list.size - 1) {
            val position = list.size - 2 - it
            val oldAnim = anim
            anim = AnimatorInflater.loadAnimator(view.context, list[position]).apply {
                setTarget(view)
                doOnEnd { oldAnim.start() }
            }
        }
        onStart()
        anim.start()
    }


}