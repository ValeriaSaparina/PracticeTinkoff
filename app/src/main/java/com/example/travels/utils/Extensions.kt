package com.example.travels.utils

import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.observe(fragment: Fragment, crossinline block: suspend (T) -> Unit): Job {
    val lifecycleOwner = fragment.viewLifecycleOwner
    return lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            collect { data -> block(data) }
        }
    }
}

fun EditText.validate(errorMessage: String, validateFun: (String) -> Boolean) {
    this.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus && !validateFun(this.text.toString())) {
            this.error = errorMessage
        }
    }
}

suspend inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
