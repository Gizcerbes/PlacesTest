package com.uogames.balinasoft.test.ui.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.uogames.balinasoft.test.R

object NavigationUtil {


    fun Fragment.navigateNavHost(
        @IdRes id: Int,
        bundle: Bundle? = null,
        navOptions: NavOptions? = getNavOptions()
    ){
        findNavHost().navigate(id,bundle,navOptions)
    }

    fun Fragment.findNavHost(): NavController {
        return requireActivity().findNavController(R.id.main_nav_host)
    }

    fun getNavOptions(): NavOptions = navOptions {
        anim {
            enter = R.anim.from_top
            exit = R.anim.hide
            popEnter = R.anim.show
            popExit = R.anim.to_top
        }
    }

    fun getLeftNavOptions(): NavOptions = navOptions {
        anim {
            enter = R.anim.from_left
            exit = R.anim.to_right
        }
    }

    fun getRightNavOptions(): NavOptions = navOptions {
        anim {
            enter = R.anim.from_right
            exit = R.anim.to_left
        }
    }


}