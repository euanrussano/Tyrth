package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.sophia.tyrth.EntityFactory
import kotlin.math.max
import kotlin.math.min

class BspDungeonBuilder(
    override var depth: Int,
) : MapBuilder {

    val rooms = mutableListOf<Rectangle>()
    val rects  = mutableListOf<Rectangle>()
    override var map: Array<Array<TileType>> = Array(80){Array(50){ TileType.WALL} }
    override var heroPosition: Pair<Int, Int> = -1 to -1

    override fun build() {
        rects.clear()
        rects.add(Rectangle(2f, 2f, map.size-5f, map[0].size-5f))
        val firstRoom = rects[0]
        addSubRectangles(firstRoom) // Divide the first room

        // Up to 240 times, we get a random rectangle and divide it. If its possible to squeeze a
        // room in there, we place it and add it to the rooms list.
        var numberRooms = 0
        while (numberRooms < 240){
            val rect = randomRectangle()
            val candidate = randomSubRectangle(rect)

            if (isPossible(candidate)){
                MapCommons.applyRoomToMap(candidate, map)
                rooms.add(candidate)
                addSubRectangles(rect)
            }

            numberRooms++
        }

        val start = Vector2()
        rooms.first().getCenter(start)
        heroPosition = start.x.toInt() to start.y.toInt()

        rooms.sortBy { rectangle -> rectangle.x }

        for (i in 0 until rooms.size - 1){
            val room = rooms[i]
            val nextRoom = rooms[i+1]
            val startX = room.x.toInt() + MathUtils.random(1, room.width.toInt() - 1)
            val startY = room.y.toInt() + MathUtils.random(1, room.height.toInt() - 1)
            val endX = nextRoom.x.toInt() + MathUtils.random(1, nextRoom.width.toInt() - 1)
            val endY = nextRoom.y.toInt() + MathUtils.random(1, nextRoom.height.toInt() - 1)
            applyCorridor(startX, startY, endX, endY)
        }

        val downStairsPosition = Vector2()
        rooms.last().getCenter(downStairsPosition)
        map[downStairsPosition.x.toInt()][downStairsPosition.y.toInt()] = TileType.DOWNSTAIRS


    }

    private fun applyCorridor(startX: Int, startY: Int, endX: Int, endY: Int) {
        var x = startX
        var y = startY

        while (x != endX || y != endY){
            if (x < endX){
                x += 1
            } else if (x > endX){
                x -= 1
            } else if (y < endY){
                y += 1
            } else if (y > endY){
                y -= 1
            }
            map[x][y] = TileType.FLOOR
        }
    }

    private fun addSubRectangles(room: Rectangle) {
        val halfWidth = room.width/2
        val halfHeight = room.height/2

        rects.add(Rectangle(room.x, room.y, halfWidth, halfHeight))
        rects.add(Rectangle(room.x, room.y + halfHeight, halfWidth, halfHeight))
        rects.add(Rectangle(room.x + halfWidth, room.y, halfWidth, halfHeight))
        rects.add(Rectangle(room.x + halfWidth, room.y + halfHeight, halfWidth, halfHeight))
    }

    private fun randomRectangle() : Rectangle{
        val idx = MathUtils.random(0, rects.size-1)
        return rects[idx]
    }

    private fun randomSubRectangle(rectangle: Rectangle) : Rectangle{
        val result = Rectangle(rectangle)

        val w = max(3, MathUtils.random(1, min(result.width.toInt(), 10)))
        val h = max(3, MathUtils.random(1, min(result.height.toInt(), 10)))

        result.x += MathUtils.random(1, 6)-1
        result.y += MathUtils.random(1, 6)-1
        result.width = w.toFloat()
        result.height = h.toFloat()

        return result
    }

    private fun isPossible(rectangle: Rectangle) : Boolean{
        val expanded = Rectangle(rectangle)
        expanded.x -= 2
        expanded.width += 4
        expanded.y -= 2
        expanded.height += 4

        var canBuild = true

        for (y in expanded.y.toInt() .. (expanded.y + expanded.height).toInt()){
            for (x in expanded.x.toInt() .. (expanded.x + expanded.width).toInt()){
                if (x > map.size - 2) canBuild = false
                if (y > map[0].size - 2) canBuild = false
                if (x < 1) canBuild = false
                if (y < 1) canBuild = false
                if (canBuild){
                    if (map[x][y] != TileType.WALL){
                        canBuild = false
                    }
                }

            }
        }

        return canBuild

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

        for (room in rooms.drop(1)){
            EntityFactory.spawnRoom(engine, room, depth)
        }
    }
}
