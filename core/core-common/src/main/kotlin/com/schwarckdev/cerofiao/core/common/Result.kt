package com.schwarckdev.cerofiao.core.common

sealed interface CeroFiaoResult<out T> {
    data class Success<T>(val data: T) : CeroFiaoResult<T>
    data class Error(val exception: Throwable? = null, val message: String? = null) : CeroFiaoResult<Nothing>
    data object Loading : CeroFiaoResult<Nothing>
}
