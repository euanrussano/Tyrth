package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.sophia.tyrth.Assets
import com.sophia.tyrth.EquipmentSlot
import com.sophia.tyrth.ecs.component.*
import ktx.actors.txt
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.getSystem
import ktx.ashley.has
import ktx.graphics.use
import ktx.scene2d.Scene2DSkin

class RenderingSystem(val viewport: ExtendViewport, val batch: Batch) : IteratingSystem(
    allOf(
        PositionComponent::class,
        RenderableComponent::class
    ).exclude(
        HiddenComponent::class
    ).
    get()
){

    val font = BitmapFont().apply {
        setUseIntegerPositions(false)
        this.data.setScale(0.5f/data.xHeight)
    }

    var debug: Boolean = false
    private val revealedColor: Color = Color(Color.DARK_GRAY).apply { a = 0.5f }
    private var visibleTiles: MutableSet<Pair<Int, Int>> = mutableSetOf()
    private var revealedTiles: MutableSet<Pair<Int, Int>> = mutableSetOf()

    override fun update(deltaTime: Float) {
        engine.getEntitiesFor(allOf(HeroComponent::class).get()).firstOrNull()?.let { hero ->
            FieldOfViewComponent.ID[hero]?.let {
                visibleTiles = it.visibleTiles
                revealedTiles = it.revealedTiles
            }
        }




        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            super.update(deltaTime)
        }
    }
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        entity ?: return
        val position = PositionComponent.ID[entity]
        val renderable = RenderableComponent.ID[entity]
        val monster = MonsterComponent.ID[entity]

        var isVisible = position.x to position.y in visibleTiles || debug
        var isRevealed = position.x to position.y in revealedTiles

        if (monster != null){
            if (!isVisible) return
        }

        if (isVisible){
            batch.color = Color.WHITE
        } else if (isRevealed){
            batch.color = revealedColor
        } else {
            return
        }

        var textureRegion = Assets.hero
        if (HeroComponent.ID[entity] != null){
            textureRegion = Assets.hero

            val equipmentHolder = EquipmentHolderComponent.ID[entity]
            equipmentHolder.slots[EquipmentSlot.MELEE]?.let {
                textureRegion = Assets.heroWithMelee
                equipmentHolder.slots[EquipmentSlot.SHIELD]?.let {
                    textureRegion = Assets.heroWithShield
                }
            }


        } else if (TileComponent.ID[entity] != null ){
            textureRegion = if (entity.has(CollisionComponent.ID)){
                Assets.wall
            } else if (entity.has(DownStairsComponent.ID)){
                Assets.downstairs
            } else {
                Assets.floor
            }
        } else {
            val name = NameComponent.ID[entity].name.lowercase()
            textureRegion = Assets.tiles[name]!!
        }

        batch.draw(textureRegion, position.x.toFloat(), position.y.toFloat(), 1f, 1f)


        if (TouchedComponent.ID[entity] != null && NameComponent.ID[entity] != null) {
            val name = NameComponent.ID[entity].name
            font.draw(batch,name,position.x.toFloat(), position.y.toFloat())
        }

        if (debug){
            if (HeroComponent.ID[entity] != null){
                for ((x, y) in visibleTiles) {
                    batch.draw(Assets.target2, x.toFloat(), y.toFloat(), 1f, 1f)
                }
            } else {
                FieldOfViewComponent.ID[entity]?.let {
                    for ((x, y) in it.visibleTiles) {
                        batch.draw(Assets.target, x.toFloat(), y.toFloat(), 1f, 1f)
                    }
                }
            }
        }


    }

}
