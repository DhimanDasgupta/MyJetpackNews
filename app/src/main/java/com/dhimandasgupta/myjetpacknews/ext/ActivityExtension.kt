package com.dhimandasgupta.myjetpacknews.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.openBrowser(url: String) {
    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url)).also { intent ->
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    i.resolveActivity(packageManager)?.let {
        startActivity(i)
    } ?: Toast.makeText(this, "Looks like you don't have browser", Toast.LENGTH_SHORT).show()
}
