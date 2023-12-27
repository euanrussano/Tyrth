package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector
import com.badlogic.gdx.math.Vector2
import com.sophia.tyrth.EntityFactory

class BspInteriorBuilder(
    override var depth: Int
) : MapBuilder {

    private val MIN_ROOM_SIZE =5
    val rooms = mutableListOf<Rectangle>()
    val rects  = mutableListOf<Rectangle>()
    override var map: Array<Array<TileType>> = Array(80){ Array(50){ TileType.WALL} }
    override var heroPosition: Pair<Int, Int> = -1 to -1

    override fun build() {
        rects.clear()
        rects.add(Rectangle(1f,1f, map.size-2f, map[0].size- 2f))
        val firstRoom = rects[0]
        addSubRectangles(firstRoom)

        val rooms2 = rects.toMutableList()
        for (room in rooms2){
            rooms.add(room)
            for (y in room.y.toInt() .. (room.y + room.height).toInt()){
                for (x in room.x.toInt() .. (room.x + room.width).toInt()){
                    if (x+1 in map.indices && y+1 in map[0].indices){
                        map[x][y] = TileType.FLOOR
                    }
                }
            }
        }

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

        val start = Vector2()
        rooms.first().getCenter(start)
        heroPosition = start.x.toInt() to start.y.toInt()
    }

    private fun addSubRectangles(rectangle: Rectangle) {
        // Remove the last rect from the list
        rects.remove(rects.lastOrNull())

        // Calculate boundaries
        val halfWidth = rectangle.width/2 - 1
        val halfHeight = rectangle.height/2 - 1

        val split = MathUtils.random(1, 4)

        if (split <= 2){
            // Horizontal split
            val h1 = Rectangle(rectangle.x, rectangle.y, halfWidth, rectangle.height)
            rects.add(h1)
            if (halfWidth > MIN_ROOM_SIZE){ addSubRectangles(h1)}
            val h2 = Rectangle(rectangle.x + halfWidth, rectangle.y, halfWidth, rectangle.height)
            rects.add(h2)
            if (halfWidth > MIN_ROOM_SIZE){ addSubRectangles(h2)}
        } else {
            // Vertical split
            val v1 = Rectangle(rectangle.x, rectangle.y, rectangle.width, halfHeight)
            rects.add(v1)
            if (halfHeight > MIN_ROOM_SIZE){ addSubRectangles(v1)}
            val v2 = Rectangle(rectangle.x, rectangle.y + halfHeight, rectangle.width, halfHeight)
            rects.add(v2)
            if (halfHeight > MIN_ROOM_SIZE){ addSubRectangles(v2)}
        }
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
