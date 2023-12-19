package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.sophia.tyrth.ecs.component.HeroComponent
import com.sophia.tyrth.ecs.component.VelocityComponent
import ktx.ashley.allOf

class InputSystem : EntitySystem() {

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
        }
    }

}
