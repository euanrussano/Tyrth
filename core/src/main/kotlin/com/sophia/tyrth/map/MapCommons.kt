package com.sophia.tyrth.map

import com.badlogic.gdx.math.Rectangle
import kotlin.math.max
import kotlin.math.min

object MapCommons {

    fun applyRoomToMap(room : Rectangle, map : Array<Array<TileType>>){
        for (x in (room.x).toInt() until (room.x + room.width).toInt()){
            for (y in (room.y).toInt() until (room.y + room.height).toInt()){
                map[x][y] = TileType.FLOOR
            }
        }
    }

    fun applyHorizontalTunnel(map: Array<Array<TileType>>, x1 : Int, x2 : Int, y : Int){
        for (x in min(x1, x2).. max(x1, x2)){
            map[x][y] = TileType.FLOOR
        }
    }

    fun applyVerticalTunnel(map: Array<Array<TileType>>, y1 : Int, y2 : Int, x : Int){
        for (y in min(y1, y2).. max(y1, y2)){
            map[x][y] = TileType.FLOOR
        }
    }
}
