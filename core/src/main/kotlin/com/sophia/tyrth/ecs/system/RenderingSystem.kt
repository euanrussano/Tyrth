package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ktx.graphics.use

class RenderingSystem(val viewport: ExtendViewport, val batch: SpriteBatch) : IteratingSystem(
    allOf(
        PositionComponent::class,
        RenderableComponent::class
    ).get()
) {


    private val revealedColor: Color = Color(Color.DARK_GRAY).apply { a = 0.5f }
    private lateinit var visibleTiles: MutableSet<Pair<Int, Int>>
    private lateinit var revealedTiles: MutableSet<Pair<Int, Int>>

    override fun update(deltaTime: Float) {
        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
        val position = PositionComponent.ID[hero]
        val fieldOfView = FieldOfViewComponent.ID[hero]

        visibleTiles = fieldOfView.visibleTiles
        revealedTiles = fieldOfView.revealedTiles

        viewport.camera.position.set(position.x.toFloat(), position.y.toFloat(), 0f)


        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.use {
            super.update(deltaTime)
        }
    }
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val position = PositionComponent.ID[entity]
        val renderable = RenderableComponent.ID[entity]
        val monster = MonsterComponent.ID[entity]

        var isVisible = position.x to position.y in visibleTiles
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

        batch.draw(renderable.texture, position.x.toFloat(), position.y.toFloat(), 1f, 1f)


    }

}
