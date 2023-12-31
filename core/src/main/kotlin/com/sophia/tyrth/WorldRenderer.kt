package com.sophia.tyrth


import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.Viewport
import com.sophia.tyrth.model.World
import ktx.graphics.use

class WorldRenderer(val batch: SpriteBatch, val viewport: Viewport, val world: World) {

    fun render(){
        centerCameraOnHero()
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            renderMap()
            renderObjects()
        }

    }

    private fun centerCameraOnHero() {
        val (x, y)  = world.hero.position.first.toFloat() to world.hero.position.second.toFloat()
        viewport.camera.position.set(x, y, 0f)
    }

    private fun renderMap() {
        world.tilemap.tiles.flatten().forEach { tile ->
            tile.draw(batch)
        }
    }

    private fun renderObjects() {
        world.hero.draw(batch)
        for (monsterInstance in world.monsterInstances) {
            monsterInstance.draw(batch)
        }
    }

}
