package com.dhimandasgupta.myjetpacknews.ext

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi

fun Context.openAppOrBrowser(url: String) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) openApp(url) else openBrowser(url)

@RequiresApi(Build.VERSION_CODES.R)
private fun Context.openApp(url: String) {
    try {
        val i = Intent(ACTION_VIEW, Uri.parse(url)).apply {
            addCategory(CATEGORY_BROWSABLE)
            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_REQUIRE_NON_BROWSER
        }
        startActivity(i)
    } catch (e: ActivityNotFoundException) {
        openBrowser(url)
    }
}

private fun Context.openBrowser(url: String) {
    val i = Intent(ACTION_VIEW, Uri.parse(url)).also { intent ->
        intent.addFlags(FLAG_ACTIVITY_MULTIPLE_TASK or FLAG_ACTIVITY_NEW_TASK)
    }
    i.resolveActivity(packageManager)?.let {
        startActivity(i)
    } ?: Toast.makeText(this, "Looks like you don't have browser", Toast.LENGTH_SHORT).show()
}
