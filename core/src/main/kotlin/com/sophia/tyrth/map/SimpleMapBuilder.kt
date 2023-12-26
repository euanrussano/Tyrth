package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.sophia.tyrth.EntityFactory
//import com.sophia.tyrth.MapCreator

class SimpleMapBuilder(override var depth: Int) : MapBuilder {

    val rooms = mutableListOf<Rectangle>()

    val width = 80
    val height = 50
    override var map: Array<Array<TileType>> = Array(width){Array(height){ TileType.WALL } }
    override var heroPosition = -1 to -1



    override fun build(){
        roomsAndCorridors(map)
    }

    override fun spawnEntities(engine: Engine) {
        // spawn the environment (tile) entities
        for (i in map.indices){
            for (j in map[i].indices){
                val isWall = map[i][j] == TileType.WALL
                val isDownstairs = map[i][j] == TileType.DOWNSTAIRS
                EntityFactory.tile(engine, i, j, isWall, isDownstairs)
            }
        }

        // test entities
        val (heroX, heroY) = heroPosition
        EntityFactory.bearTrap(engine, heroX, heroY+1)
        EntityFactory.shield(engine, heroX, heroY+2)
        EntityFactory.healthPotion(engine, heroX, heroY+3)
        EntityFactory.healthPotion(engine, heroX, heroY+4)
        EntityFactory.healthPotion(engine, heroX, heroY+5)
        EntityFactory.healthPotion(engine, heroX, heroY+6)
        EntityFactory.healthPotion(engine, heroX, heroY+7)


        for (room in rooms.drop(1)){
            EntityFactory.spawnRoom(engine, room, depth)
        }
    }

    private fun roomsAndCorridors(map: Array<Array<TileType>>){
        val width = map.size
        val height = map[0].size

        rooms.clear()
        val MAX_ROOMS = 30
        val MIN_SIZE = 6
        val MAX_SIZE = 10

        for (i in 0 .. MAX_ROOMS){
            val w = MathUtils.random(MIN_SIZE, MAX_SIZE)
            val h = MathUtils.random(MIN_SIZE, MAX_SIZE)
            val x = MathUtils.random(1, width - w - 1)
            val y = MathUtils.random(1, height - h - 1)
            val newRoom = Rectangle(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())
            val ok = rooms.firstOrNull { rectangle -> rectangle.overlaps(newRoom) } == null
            if (ok){
                MapCommons.applyRoomToMap(newRoom, map)

                if (rooms.isNotEmpty()){
                    val center = Vector2()
                    val prevCenter = Vector2()
                    newRoom.getCenter(center)
                    rooms.last().getCenter(prevCenter)
                    if (MathUtils.randomBoolean()){
                        MapCommons.applyHorizontalTunnel(
                            map,
                            prevCenter.x.toInt(),
                            center.x.toInt(),
                            prevCenter.y.toInt()
                        )
                        MapCommons.applyVerticalTunnel(map, prevCenter.y.toInt(), center.y.toInt(), center.x.toInt())
                    } else {
                        MapCommons.applyHorizontalTunnel(
                            map,
                            prevCenter.x.toInt(),
                            center.x.toInt(),
                            center.y.toInt()
                        )
                        MapCommons.applyVerticalTunnel(
                            map,
                            prevCenter.y.toInt(),
                            center.y.toInt(),
                            prevCenter.x.toInt()
                        )
                    }
                }

                rooms.add(newRoom)
            }
        }

        val stairsPosition = Vector2()
        rooms.last().getCenter(stairsPosition)
        map[stairsPosition.x.toInt()][stairsPosition.y.toInt()] = TileType.DOWNSTAIRS

        val heroPositionVec = Vector2()
        rooms.first().getCenter(heroPositionVec)

        heroPosition = heroPositionVec.x.toInt() to heroPositionVec.y.toInt()
    }


}
