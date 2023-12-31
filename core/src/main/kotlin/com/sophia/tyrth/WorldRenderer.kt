package com.sophia.tyrth


import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.sophia.tyrth.model.Entity
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.hero.Hero
import com.sophia.tyrth.model.item.ItemInstance
import com.sophia.tyrth.model.monster.MonsterInstance
import ktx.graphics.use

class WorldRenderer(val batch: SpriteBatch, val viewport: Viewport, val world: World) : InputAdapter() {

    private var selectedEntity: Entity? = null

    var debug: Boolean = false
    val colorTransparent = Color(Color.WHITE).apply { a = 0.5f }
    val font = BitmapFont().apply {
        setUseIntegerPositions(false)
        this.data.setScale(0.5f/data.xHeight)
    }

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
        if (debug){
            world.tilemap.tiles.flatten().forEach { it.draw(batch) }
            return
        }

        batch.setColor(Color.DARK_GRAY)
        for ((x, y) in world.hero.fieldOfView.revealedTiles){
            world.tilemap.tiles[x][y].draw(batch)
        }

        batch.setColor(Color.WHITE)
        for ((x, y) in world.hero.fieldOfView.visibleTiles){
            world.tilemap.tiles[x][y].draw(batch)
        }

    }

    private fun renderObjects() {
        world.itemInstances.forEach { renderItem(it) }
        renderHero()
        world.monsterInstances.forEach {renderMonster(it)}
        renderSelectedEntity()
    }

    private fun renderItem(itemInstance: ItemInstance) {
        if (itemInstance.position in world.hero.fieldOfView.visibleTiles || debug) {
            itemInstance.draw(batch)
        }
    }

    private fun renderSelectedEntity() {
        val entity = selectedEntity ?: return
        if (entity.position !in world.hero.fieldOfView.visibleTiles && !debug) return

        val text = when(entity::class){
            MonsterInstance::class -> { (selectedEntity as MonsterInstance).monster.name }
            Hero::class -> { (selectedEntity as Hero).name }
            else -> { "<undef>"}
        }

        font.draw(batch, text, entity.position.first.toFloat(), entity.position.second.toFloat())
    }

    private fun renderMonster(monsterInstance: MonsterInstance) {
        if (monsterInstance.position in world.hero.fieldOfView.visibleTiles || debug) {
            monsterInstance.draw(batch)
            if (debug){
                batch.color = colorTransparent
                for ((x, y) in monsterInstance.fieldOfView.visibleTiles){
                    batch.draw(Assets.target, x.toFloat(), y.toFloat(), 1f, 1f)
                }
                batch.color = Color.WHITE
            }
        }
    }

    private fun renderHero() {
        world.hero.draw(batch)
        if (debug){
            batch.color = colorTransparent
            for ((x, y) in world.hero.fieldOfView.visibleTiles){
                batch.draw(Assets.target2, x.toFloat(), y.toFloat(), 1f, 1f)
            }
            batch.color = Color.WHITE
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val touchPosition = Vector2(screenX.toFloat(), screenY.toFloat())
        viewport.unproject(touchPosition)
        val x = touchPosition.x.toInt()
        val y = touchPosition.y.toInt()
        if (x to y == world.hero.position){
            selectedEntity = world.hero
            return true
        }

        world.monsterInstances.firstOrNull { monsterInstance -> monsterInstance.position == x to y }?.let {
            selectedEntity = it
            return true
        }

        selectedEntity = null
        return false

    }

}
