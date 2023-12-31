package com.sophia.tyrth.infrastructure.repository

import com.badlogic.gdx.math.MathUtils
import kotlin.random.asKotlinRandom

abstract class Repository<T> {

    val entities = mutableListOf<T>()

    fun findByName(name: String): T{
        return entities.first { getName(it) == name }
    }

    fun random(): T{
        return entities.random(MathUtils.random.asKotlinRandom())
    }

    abstract fun getName(it: T): String



}
