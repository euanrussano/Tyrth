package com.sophia.tyrth

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import kotlin.math.max
import kotlin.math.min

object MapCreator {

    fun mapTest(engine : Engine) {
        engine.removeAllEntities()

        val heroPosition = 2 to 2
        val width = 20
        val height = 20
        for (i in 0 .. width){
            for (j in 0 .. height){
                val isWall = (i == 0 || j == 0 || i == width || j == height || MathUtils.randomBoolean(0.1f)) && (i to j != heroPosition)
                EntityFactory.tile(engine, i, j, isWall, false)
            }
        }

        EntityFactory.hero(engine, heroPosition.first, heroPosition.second)

        for (i in 0..10){
            EntityFactory.randomMonster(engine, i, 4)
        }
    }



    fun mapDungeon(engine: Engine, depth : Int = 1) : List<Rectangle> {
        val width = 80
        val height = 50

        val isWall = Array(width){Array(height){ true} }

        val rooms = mutableListOf<Rectangle>()
        val MAX_ROOMS = 30
        val MIN_SIZE = 6
        val MAX_SIZE = 10

        for (i in 0 .. MAX_ROOMS){
            val w = MathUtils.random(MIN_SIZE, MAX_SIZE)
            val h = MathUtils.random(MIN_SIZE, MAX_SIZE)
            val x = MathUtils.random(1, width - w - 1)-1
            val y = MathUtils.random(1, height - h - 1)-1
            val newRoom = Rectangle(x.toFloat(), y.toFloat(), w.toFloat(), h.toFloat())
            val ok = rooms.firstOrNull { rectangle -> rectangle.overlaps(newRoom) } == null
            if (ok){
                applyRoomToMap(newRoom, isWall)

                if (rooms.isNotEmpty()){
                    val center = Vector2()
                    val prevCenter = Vector2()
                    newRoom.getCenter(center)
                    rooms.last().getCenter(prevCenter)
                    if (MathUtils.randomBoolean()){
                        applyHorizontalTunnel(isWall, prevCenter.x.toInt(), center.x.toInt(), prevCenter.y.toInt())
                        applyVerticalTunnel(isWall, prevCenter.y.toInt(), center.y.toInt(), center.x.toInt())
                    } else {
                        applyHorizontalTunnel(isWall, prevCenter.x.toInt(), center.x.toInt(), center.y.toInt())
                        applyVerticalTunnel(isWall, prevCenter.y.toInt(), center.y.toInt(), prevCenter.x.toInt())
                    }
                }

                rooms.add(newRoom)
            }
        }

        for (i in isWall.indices){
            for (j in isWall[i].indices){
                EntityFactory.tile(engine, i, j, isWall[i][j], false)
            }
        }

        if (depth == 1){
            val heroX = rooms.first().x.toInt()
            val heroY = rooms.first().y.toInt()
            EntityFactory.hero(engine, heroX, heroY)
            EntityFactory.healthPotion(engine, heroX, heroY+1)
            EntityFactory.healthPotion(engine, heroX, heroY+2)
            EntityFactory.healthPotion(engine, heroX, heroY+3)
            EntityFactory.healthPotion(engine, heroX, heroY+4)
            EntityFactory.healthPotion(engine, heroX, heroY+5)
            EntityFactory.healthPotion(engine, heroX, heroY+6)
            EntityFactory.healthPotion(engine, heroX, heroY+7)
        }


        for (room in rooms.drop(1)){
            spawnRoom(engine, room)
        }

        // placeDownstairs in last room
        val center = Vector2()
        rooms.first().getCenter(center)
        EntityFactory.tile(engine, center.x.toInt(), center.y.toInt(), false, true)

        return rooms

    }



    fun applyRoomToMap(room : Rectangle, isWall : Array<Array<Boolean>>){
        for (x in (room.x).toInt() .. (room.x + room.width).toInt()){
            for (y in (room.y).toInt() .. (room.y + room.height).toInt()){
                isWall[x][y] = false
            }
        }
    }

    fun applyHorizontalTunnel(isWall: Array<Array<Boolean>>, x1 : Int, x2 : Int, y : Int){
        for (x in min(x1, x2).. max(x1, x2)){
            isWall[x][y] = false
        }
    }

    fun applyVerticalTunnel(isWall: Array<Array<Boolean>>, y1 : Int, y2 : Int, x : Int){
        for (y in min(y1, y2).. max(y1, y2)){
            isWall[x][y] = false
        }
    }

    fun spawnRoom(engine : Engine, room: Rectangle) {
        spawnMonsters(engine, room)
        spawnItems(engine, room)
    }

    private fun spawnMonsters(engine: Engine, room: Rectangle) {
        val MAX_MONSTERS = 4

        val numberMonsters = MathUtils.random(1, MAX_MONSTERS)
        val monsterSpawnPoints = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until numberMonsters) {
            var added = false
            while (!added) {
                val x = room.x.toInt() + MathUtils.random(0, room.width.toInt())
                val y = room.y.toInt() + MathUtils.random(0, room.height.toInt())
                if (x to y !in monsterSpawnPoints) {
                    monsterSpawnPoints.add(x to y)
                    added = true
                }
            }
        }

        // actually spawn the monsters
        for ((x, y) in monsterSpawnPoints) {
            EntityFactory.randomMonster(engine, x, y)
        }
    }

    private fun spawnItems(engine: Engine, room: Rectangle) {
        val MAX_ITEMS = 2

        val numberItems = MathUtils.random(1, MAX_ITEMS)
        val itemSpawnPoints = mutableListOf<Pair<Int, Int>>()
        for (i in 0 until numberItems) {
            var added = false
            while (!added) {
                val x = room.x.toInt() + MathUtils.random(0, room.width.toInt())
                val y = room.y.toInt() + MathUtils.random(0, room.height.toInt())
                if (x to y !in itemSpawnPoints) {
                    itemSpawnPoints.add(x to y)
                    added = true
                }
            }
        }

        // actually spawn the items
        for ((x, y) in itemSpawnPoints) {
            EntityFactory.healthPotion(engine, x, y)
        }
    }

}
