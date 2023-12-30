package com.sophia.tyrth.map

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.sophia.tyrth.EntityFactory

class MazeBuilder(override var depth: Int) : MapBuilder {

    val width = 40
    val height = 25
    override var map: Array<Array<TileType>> = Array(width){Array(height){ TileType.WALL } }

    override var heroPosition: Pair<Int, Int> = -1 to -1

    val spawnAreas = mutableListOf<Pair<Int, Int>>()

    override fun build() {

        // Maze gen
        val maze = Grid(map.size/2 -2, map[0].size/2-2)
        maze.generateMaze(this)
        maze.applyMazeToMap(map)

        val startIdx = 2 to 2
        heroPosition = startIdx

        val downStairPosition = MapCommons.removeUnreachableAreasReturnsMostDistant(map, startIdx)

        map[downStairPosition.first][downStairPosition.second] = TileType.DOWNSTAIRS

        // (The original article uses Vornoi diagram https://bfnightly.bracketproductions.com/chapter_27.html)
        for (x in map.indices){
            for (y in map[0].indices){
                if (map[x][y] == TileType.FLOOR){
                    spawnAreas.add(x to y)
                }
            }
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

        // the original article uses regions
        for (i in 0 .. 10){
            val firstIdx = MathUtils.random(0, spawnAreas.size-1)
            val secondIdx = MathUtils.random(firstIdx, spawnAreas.size-1)
            val subList = mutableListOf<Pair<Int, Int>>()
            for (j in firstIdx until secondIdx){
                subList.add(spawnAreas[j])
            }
            EntityFactory.spawnRegion(engine, subList, depth)
            spawnAreas.removeAll(subList)
        }
    }

    companion object{
        val TOP = 0
        val RIGHT = 1
        val BOTTOM = 2
        val LEFT = 3
    }

    data class Cell(val row : Int, val col : Int, val walls : Array<Boolean> = Array(4){ true }, var visited : Boolean = false){

        fun removeWalls(next : Cell){
            val x  = col - next.col
            val y = row - next.row

            if (x == 1){
                walls[LEFT]  = false
                next.walls[RIGHT] = false
            } else if (x == -1){
                walls[RIGHT]  = false
                next.walls[LEFT] = false
            } else if (y == 1){
                walls[TOP]  = false
                next.walls[BOTTOM] = false
            }else if (y == -1){
                walls[BOTTOM]  = false
                next.walls[TOP] = false
            }
        }
    }

    class Grid(val width : Int, val height : Int){
        val cells = Array(width){ row -> Array(height){ col -> Cell(row, col)} }
        val backtrace = mutableListOf<Pair<Int, Int>>()
        var current = 0 to 0

        fun getAvailableNeighbors() : List<Pair<Int, Int>> {
            val neighbors = mutableListOf<Pair<Int, Int>>()
            val (currentRow, currentColumn) = cells[current.first][current.second]


            val neighborIndices = listOf(
                currentRow-1 to currentColumn,
                currentRow to currentColumn+1,
                currentRow + 1 to currentColumn,
                currentRow to currentColumn - 1
            )

            for ((row, col) in neighborIndices){
                val cell = cells.getOrNull(row)?.getOrNull(col)?: continue
                if (!cell.visited){
                    neighbors.add(cell.row to cell.col)
                }
            }

            return neighbors

        }

        fun findNextCell() : Pair<Int, Int>? {
            val neighbors = getAvailableNeighbors()
            if (neighbors.isNotEmpty()){
                return neighbors.random()
            }

            return null
        }

        fun generateMaze(generator : MazeBuilder){
            var i = 0
            while(true){
                cells[current.first][current.second].visited = true
                val next = findNextCell()
                if (next != null){
                    cells[next.first][next.second].visited = true
                    backtrace.add(next)

                    val nextCell = cells[next.first][next.second]
                    val currentCell = cells[current.first][current.second]
                    currentCell.removeWalls(nextCell)
                    current = next
                } else {
                    if (backtrace.isNotEmpty()){
                        current = backtrace[0]
                        backtrace.removeAt(0)
                    } else {
                        break
                    }

                }

                i+= 1

            }

        }

        fun applyMazeToMap(map: Array<Array<TileType>>) {
            //clear the map
            map.flatten().map { TileType.WALL }
            for (cell in cells.flatten()){
                val x = (cell.row + 1)*2
                val y = (cell.col + 1)*2

                map[x][y] = TileType.FLOOR
                if (!cell.walls[TOP]) map[x][y+1] = TileType.FLOOR
                if (!cell.walls[RIGHT]) map[x+1][y] = TileType.FLOOR
                if (!cell.walls[BOTTOM]) map[x][y-1] = TileType.FLOOR
                if (!cell.walls[LEFT]) map[x-1][y] = TileType.FLOOR
            }
        }
    }
}
