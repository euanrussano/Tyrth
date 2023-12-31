package com.sophia.tyrth.infrastructure.repository

abstract class Repository<T> {

    val entities = mutableListOf<T>()

    fun findByName(name: String): T{
        return entities.first { getName(it) == name }
    }

    abstract fun getName(it: T): String


}
