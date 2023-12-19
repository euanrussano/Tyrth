package com.sophia.tyrth

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with
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
                EntityFactory.tile(engine, i, j, isWall)
            }
        }

        EntityFactory.hero(engine, heroPosition.first, heroPosition.second)

        for (i in 0..10){
            EntityFactory.monster(engine, i, 4)
        }
    }



    fun mapDungeon(engine: Engine) {
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
            val x = MathUtils.random(1, width - w - 1)
            val y = MathUtils.random(1, height - h - 1)
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
                EntityFactory.tile(engine, i, j, isWall[i][j])
            }
        }

        val heroX = rooms.first().x.toInt()
        val heroY = rooms.first().y.toInt()
        EntityFactory.hero(engine, heroX, heroY)

        val center = Vector2()
        for (room in rooms.drop(1)){
            room.getCenter(center)
            EntityFactory.monster(engine, center.x.toInt(), center.y.toInt())
        }


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

}
