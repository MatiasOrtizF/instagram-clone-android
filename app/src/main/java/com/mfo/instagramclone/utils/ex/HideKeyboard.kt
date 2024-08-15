package com.mfo.instagramclone.utils.ex

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

// Extensión para Activity
fun Activity.hideKeyboard() {
    // Obtiene la vista actual con el foco
    val view = this.currentFocus ?: View(this)

    // Oculta el teclado
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

// Extensión para Fragment
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard() }
}