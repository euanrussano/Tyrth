package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.Viewport
import com.sophia.tyrth.ecs.component.WantsToMoveCamera
import com.sophia.tyrth.ecs.component.WantsToZoomIn
import com.sophia.tyrth.ecs.component.WantsToZoomOut
import ktx.ashley.allOf
import ktx.ashley.oneOf
import ktx.ashley.remove

class CameraControlSystem(val viewport: Viewport) : IteratingSystem(
    oneOf(
        WantsToZoomIn::class,
        WantsToZoomOut::class,
        WantsToMoveCamera::class
    ).get()
) {
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        WantsToZoomIn.ID[entity]?.let {
            (viewport.camera as OrthographicCamera).zoom -= 0.1f
        }
        WantsToZoomOut.ID[entity]?.let {
            (viewport.camera as OrthographicCamera).zoom += 0.1f
        }
        WantsToMoveCamera.ID[entity]?.let{
            (viewport.camera as OrthographicCamera).position.add(it.dx.toFloat(), it.dy.toFloat(), 0f)
        }
        entity?.remove<WantsToZoomIn>()
        entity?.remove<WantsToZoomOut>()
        entity?.remove<WantsToMoveCamera>()
    }

}
