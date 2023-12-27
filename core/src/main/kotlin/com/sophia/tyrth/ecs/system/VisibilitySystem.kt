package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Game
import com.badlogic.gdx.math.*
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import ktx.ashley.remove

class VisibilitySystem : IteratingSystem(
    allOf(
        FieldOfViewComponent::class,
        PositionComponent::class
    ).get()
) {

    // polygon to check LOS blocking
    val blockingPolygon = Polygon(floatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f))
    val centerTile = Vector2()
    val centerEntity = Vector2()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val position = PositionComponent.ID[entity]
        val fieldOfView = FieldOfViewComponent.ID[entity]

        // do not run the algo if the revealed tiles is already and the entity didn't move
        if (fieldOfView.revealedTiles.isNotEmpty() && EntityMovedComponent.ID[entity] == null) return

        fieldOfView.revealedTiles.addAll(fieldOfView.visibleTiles)
        fieldOfView.visibleTiles.clear()

        for (x in position.x - fieldOfView.range .. position.x + fieldOfView.range){
            for (y in position.y - fieldOfView.range .. position.y + fieldOfView.range){
                fieldOfView.visibleTiles.add(x to y)
            }
        }

        // loop over the tiles that block the view and analyse the LOS (line of sight)
        val tilesBlocked = mutableListOf<Pair<Int, Int>>()
        for (entity2 in engine.getEntitiesFor(allOf(BlockViewComponent::class, PositionComponent::class).get() )){
            if (entity == entity2) continue
            val position2 = PositionComponent.ID[entity2]
            if (position2.x to position2.y in fieldOfView.visibleTiles){

                blockingPolygon.setPosition(position2.x.toFloat(), position2.y.toFloat())

                for ((x, y) in fieldOfView.visibleTiles){
                    if (x to y == position2.x to position2.y) continue
                    // center the positions of the points in the tile and in the entity
                    centerTile.set(x.toFloat()+ 0.5f, y.toFloat()+ 0.5f)
                    centerEntity.set(position.x.toFloat()+ 0.5f, position.y.toFloat() + 0.5f)
                    // check if the segment representing LOS intercepts the polygon
                    val intersects = Intersector.intersectSegmentPolygon(
                        centerTile, centerEntity, blockingPolygon
                    )
                    if (intersects){
                        tilesBlocked.add(x to y)
                    }
                }

            }
        }
        fieldOfView.visibleTiles.removeAll(tilesBlocked)

        // there is a chance of 1/24 of viewing a hidden entity inside the visible range
        for (hiddenEntity in engine.getEntitiesFor(allOf(NameComponent::class, PositionComponent::class, HiddenComponent::class).get())){
            val (hiddenX, hiddenY) = with(PositionComponent.ID[hiddenEntity]){x to y}
            if (hiddenX to hiddenY !in fieldOfView.visibleTiles) continue

            // 1/24 chance of spotting the hidden item
            val roll = MathUtils.random(1, 24)
            
            if (roll == 1){
                val name = NameComponent.ID[hiddenEntity].name
                hiddenEntity.remove<HiddenComponent>()

                HeroComponent.ID[entity]?.let {
                    GameLog.add("You spotted a $name")
                }
                break

            }
        }
    }
}
