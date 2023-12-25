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
            spawnRoom(engine, room, depth)
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

    fun spawnRoom(engine : Engine, room: Rectangle, depth:Int) {
        val spawnTable = mutableMapOf(
            "Rat" to 10,
            "Scorpion" to 1 + depth,
            "Health Potion" to 7,
            "Dagger" to 3,
            "Shield" to 3,
            "Longsword" to depth-1,
            "Tower Shield" to depth-1,
            "Rations" to 10,
            "Map Scroll" to 1,
            "Bear Trap" to 2
        )
        val spawnPoints = mutableMapOf<Pair<Int, Int>, String>()

        val MAX_MONSTERS = 4 + (depth-1)

        val numberSpawns = MathUtils.random(-2, MAX_MONSTERS)

        for (i in 0 until numberSpawns){
            var added = false
            var tries = 0
            while (!added && tries < 20){
                val x = room.x.toInt() + MathUtils.random(0, room.width.toInt())
                val y = room.y.toInt() + MathUtils.random(0, room.height.toInt())
                if (x to y in spawnPoints){ tries += 1; continue}
                spawnPoints[x to y] = rollSpawnTable(spawnTable)
                added = true
            }
        }


        for ((position, name) in spawnPoints){
            val (x, y) = position
            when(name){
                "Rat" -> EntityFactory.rat(engine, x, y)
                "Scorpion" -> EntityFactory.scorpion(engine, x, y)
                "Health Potion" -> EntityFactory.healthPotion(engine, x, y)
                "Dagger" -> EntityFactory.dagger(engine, x, y)
                "Shield" -> EntityFactory.shield(engine, x, y)
                "Longsword" -> EntityFactory.longsword(engine, x, y)
                "Tower Shield" -> EntityFactory.towerShield(engine, x, y)
                "Rations" -> EntityFactory.rations(engine, x, y)
                "Map Scroll" -> EntityFactory.mapScroll(engine, x, y)
                "Bear Trap" -> EntityFactory.bearTrap(engine, x, y)
            }
        }
    }

    private fun rollSpawnTable(spawnTable: MutableMap<String, Int>): String {
        val totalWeight = spawnTable.values.sum()
        if (totalWeight ==0 ) return "None"

        var roll = MathUtils.random(1, totalWeight)
        var index = 0
        for ((key, value) in spawnTable){
            if (roll < value){
                return key
            }
            roll -= value
        }
        return "None"
    }
}
