package dev.sora.protohax.relay.gui

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Point
import android.os.Build
import android.util.Log
import android.view.Display
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import dev.sora.protohax.R

class PopupWindow {

    private var layout: View? = null
    private val menu = SelectionMenu(this)

    val screenSize = Point()

    fun toggle(wm: WindowManager, ctx: Context) {
        if (layout == null) {
            display(wm, ctx)
        } else {
            destroy(wm)
        }
    }

    fun destroy(wm: WindowManager) {
        if (layout == null) return
        wm.removeView(layout)
        layout = null
    }

    private fun getScreenSize(wm: WindowManager): Point {
        return if (Build.VERSION.SDK_INT >= 30) {
            wm.maximumWindowMetrics.bounds.let {
                Point(it.width(), it.height())
            }
        } else {
            val point = Point()
            wm.defaultDisplay.getRealSize(point)
            point
        }
    }

    fun display(wm: WindowManager, ctx: Context) {
        if (layout != null) return

        getScreenSize(wm).also { screenSize.set(it.x, it.y) }

        val layout = LinearLayout(ctx).apply {
            orientation = LinearLayout.VERTICAL
        }
        menu.apply(ctx, layout, wm)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.END
        params.x = 0   // Initial Position of window
        params.y = 0 // Initial Position of window
        wm.addView(layout, params)
        this.layout = layout
    }

}