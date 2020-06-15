package com.batman.ui.kotlin

import android.view.View
import com.batman.ui.R
import com.batman.ui.skin.UISkinHelper
import com.batman.ui.skin.UISkinValueBuilder

fun throttleClick(wait: Long = 200, block: ((View) -> Unit)): View.OnClickListener {

    return View.OnClickListener { v ->
        val current = System.currentTimeMillis()
        val lastClickTime = (v.getTag(R.id.ui_click_timestamp) as? Long) ?: 0
        if (current - lastClickTime > wait) {
            v.setTag(R.id.ui_click_timestamp, current)
            block(v)
        }
    }
}

fun debounceClick(wait: Long = 200, block: ((View) -> Unit)): View.OnClickListener {
    return View.OnClickListener { v ->
        var action = (v.getTag(R.id.ui_click_debounce_action) as? DebounceAction)
        if(action == null){
            action = DebounceAction(v, block)
            v.setTag(R.id.ui_click_debounce_action, action)
        }else{
            action.block = block
        }
        v.removeCallbacks(action)
        v.postDelayed(action, wait)
    }
}

class DebounceAction(val view: View,  var block: ((View) -> Unit)): Runnable {
    override fun run() {
        if(view.isAttachedToWindow){
            block(view)
        }
    }
}

fun View.onClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(throttleClick(wait, block))
}

fun View.onDebounceClick(wait: Long = 200, block: ((View) -> Unit)) {
    setOnClickListener(debounceClick(wait, block))
}

fun View.skin(block:(UISkinValueBuilder.() -> Unit)){
    val builder = UISkinValueBuilder.acquire();
    builder.block()
    UISkinHelper.setSkinValue(this, builder)
    builder.release()
}
