package com.ynd.data

import com.ynd.domain.CoroutineDispatcherProvider
import kotlinx.coroutines.Dispatchers

class CoroutineDispatcherProviderImpl : CoroutineDispatcherProvider {
    override val main = Dispatchers.Main
    override val io = Dispatchers.IO
    override val default = Dispatchers.Default
    override val unconfined = Dispatchers.Unconfined
}