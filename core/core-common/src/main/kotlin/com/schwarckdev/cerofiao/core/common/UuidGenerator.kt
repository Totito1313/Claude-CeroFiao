package com.schwarckdev.cerofiao.core.common

import java.util.UUID

object UuidGenerator {
    fun generate(): String = UUID.randomUUID().toString()
}
