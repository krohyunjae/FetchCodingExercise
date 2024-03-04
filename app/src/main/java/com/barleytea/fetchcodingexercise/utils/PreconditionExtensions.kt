package com.barleytea.fetchcodingexercise.utils

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
inline fun <T : Any> T?.require(): T {
    contract {
        returns() implies (this@require != null)
    }
    return requireNotNull(this)
}