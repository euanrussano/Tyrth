package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.getSystem
import ktx.ashley.plusAssign
import ktx.ashley.remove

class InputSystem(val viewport: Viewport) : EntitySystem() {

    val touchPosition = Vector2()
    val bounds = Rectangle(0f, 0f, 1f, 1f)
    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
        val velocity = VelocityComponent.ID[hero]

        if (Gdx.input.isKeyJustPressed(Input.Keys.W)){
            velocity.dy = 1
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)){
            velocity.dy = -1
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.A)){
            velocity.dx = -1
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D)){
            velocity.dx = 1
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            engine.getSystem<RenderingSystem>().debug = !engine.getSystem<RenderingSystem>().debug
        } else if (Gdx.input.isTouched){
            for (entity in engine.getEntitiesFor(allOf(TouchedComponent::class).get())){
                entity.remove<TouchedComponent>()
            }
            touchPosition.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
            viewport.unproject(touchPosition)
            for (entity in engine.getEntitiesFor(allOf(NameComponent::class, PositionComponent::class).get()).reversed()){
                val position = PositionComponent.ID[entity]
                val name = NameComponent.ID[entity]
                bounds.setPosition(position.x.toFloat(), position.y.toFloat())
                if (bounds.contains(touchPosition)){
                    entity += TouchedComponent()
                    break
                }
            }
        }
    }

}
