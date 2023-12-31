package com.sophia.tyrth.model

class FieldOfView(
    val range : Int
) {
    val revealedTiles = mutableSetOf<Pair<Int, Int>>()
    val visibleTiles = mutableSetOf<Pair<Int, Int>>()
    val visibileEntities = mutableSetOf<Entity>()

}
