package com.sophia.tyrth.model.manager

import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Polygon
import com.badlogic.gdx.math.Vector2
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.ecs.component.*
import com.sophia.tyrth.model.Entity
import com.sophia.tyrth.model.World
import ktx.ashley.allOf
import ktx.ashley.remove

class VisibilityManager(val world: World) : WorldManager {

    // polygon to check LOS blocking
    val blockingPolygon = Polygon(floatArrayOf(0f, 0f, 1f, 0f, 1f, 1f, 0f, 1f))
    val centerTile = Vector2()
    val centerEntity = Vector2()

    override fun update(delta: Float) {
        for (entity in world.entities){
            update(entity)
        }
    }

    private fun update(entity: Entity) {
        val position = entity.position
        val fieldOfView = entity.fieldOfView

        // always update the current visible entities, since these are always changing
        fieldOfView.visibileEntities.clear()
        for (entity2 in world.entities){
            if (entity2 == entity) continue
            if (entity2.position in fieldOfView.visibleTiles){
                fieldOfView.visibileEntities.add(entity2)
            }
        }

        // do not run the algo if the revealed tiles is already and the entity didn't move
        if (fieldOfView.revealedTiles.isNotEmpty() && entity.velocity == 0 to 0) return

        fieldOfView.revealedTiles.addAll(fieldOfView.visibleTiles)
        fieldOfView.visibleTiles.clear()

        for (x in position.first - fieldOfView.range .. position.first + fieldOfView.range){
            for (y in position.second - fieldOfView.range .. position.second + fieldOfView.range){
                fieldOfView.visibleTiles.add(x to y)
            }
        }

        // loop over the tiles that block the view and analyse the LOS (line of sight)
        val tilesBlocked = mutableSetOf<Pair<Int, Int>>()
        for (tile in world.tilemap.tiles.flatten().filter { it.terrain.isWall } ){
            if (tile.x to tile.y in fieldOfView.visibleTiles){

                blockingPolygon.setPosition(tile.x.toFloat(), tile.y.toFloat())

                for ((x, y) in fieldOfView.visibleTiles){
                    if (x to y == tile.x to tile.y) continue
                    // center the positions of the points in the tile and in the entity
                    centerTile.set(x.toFloat()+ 0.5f, y.toFloat()+ 0.5f)
                    centerEntity.set(position.first.toFloat()+ 0.5f, position.second.toFloat() + 0.5f)
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
//        for (hiddenEntity in engine.getEntitiesFor(allOf(NameComponent::class, PositionComponent::class, HiddenComponent::class).get())){
//            val (hiddenX, hiddenY) = with(PositionComponent.ID[hiddenEntity]){x to y}
//            if (hiddenX to hiddenY !in fieldOfView.visibleTiles) continue
//
//            // 1/24 chance of spotting the hidden item
//            val roll = MathUtils.random(1, 24)
//
//            if (roll == 1){
//                val name = NameComponent.ID[hiddenEntity].name
//                hiddenEntity.remove<HiddenComponent>()
//
//                HeroComponent.ID[entity]?.let {
//                    GameLog.add("You spotted a $name")
//                }
//                break
//
//            }
//        }
    }

}
