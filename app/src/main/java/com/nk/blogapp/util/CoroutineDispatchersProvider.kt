package com.nk.blogapp.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object CoroutineDispatcherProvider {
    val mainDispatcher: CoroutineDispatcher = Dispatchers.Main
    val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    val Default: CoroutineDispatcher = Dispatchers.Default
}