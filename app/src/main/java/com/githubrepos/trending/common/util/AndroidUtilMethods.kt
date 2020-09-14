package com.githubrepos.trending.common.util

import android.view.View

/**
 *  Returns View.VISIBLE or VIEW.GONE based on the passed value
 */
fun visibilityIf(visible : Boolean) : Int = if(visible) View.VISIBLE else View.GONE