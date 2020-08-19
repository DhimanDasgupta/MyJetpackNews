package com.dhimandasgupta.myjetpacknews.ui.ext

import com.dhimandasgupta.myjetpacknews.ui.data.Page

fun List<Page>.toListOfPairedPages(): List<Pair<Page, Page?>> {
    val listOfPairPages = mutableListOf<Pair<Page, Page?>>()
    for (i in 0..this.size - 1 step 2) {
        val pair = Pair(this[i], if (i + 1 < this.size) this[i + 1] else null)
        listOfPairPages.add(pair)
    }

    return listOfPairPages.toList()
}