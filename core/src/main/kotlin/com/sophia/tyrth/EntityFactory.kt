package com.sophia.tyrth

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with

object EntityFactory {

    fun tile(engine: Engine, x: Int, y: Int, isWall: Boolean, isDownStairs : Boolean) {
        engine.entity {
            with<PositionComponent>{
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            if(isWall){
                with<CollisionComponent>()
                with<BlockViewComponent>{}
            }
            with<TileComponent>{}
            if (isDownStairs){
                with<DownStairsComponent>()
            }
        }
    }

    fun randomMonster(engine: Engine, x : Int, y : Int){
        if (MathUtils.randomBoolean()){
            rat(engine, x, y)
        } else{
            scorpion(engine, x, y)
        }
    }
    fun rat(engine : Engine, x : Int, y : Int){
        monster(engine, x, y, "rat", "Rat")
    }

    fun scorpion(engine : Engine, x : Int, y : Int){
        monster(engine, x, y, "scorpion", "Scorpion")
    }

    private fun monster(engine: Engine, x: Int, y: Int, textureName : String, name : String) {
        engine.entity {
            with<PositionComponent> {
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            with<FieldOfViewComponent>{
                range = 3
            }
            with<CollisionComponent>()
            with<MonsterComponent>()
            with<NameComponent>{
                this.name = name
            }
            with<VelocityComponent> {}
            with<HealthComponent>{
                this.maxHP = 16
                this.hp = 16
            }
            with<CombatStatsComponent>{
                this.power = 4
                this.defense = 1
            }
        }
    }

    fun hero(engine: Engine, x: Int, y: Int) {
        engine.entity {
            with<PositionComponent> {
                this.x = x
                this.y = y
            }
            with<VelocityComponent> {}
            with<CollisionComponent>()
            with<HeroComponent> {}
            with<FieldOfViewComponent>{
                range = 4
            }
            with<RenderableComponent>()
            with<HealthComponent>{
                this.maxHP = 30
                this.hp = 30
            }
            with<CombatStatsComponent>{
                this.power = 5
                this.defense = 2
            }
            with<NameComponent>{
                name = "Scott"
            }
            with<BackpackComponent>{
                maxWeight = 3
            }
            with<EquipmentHolderComponent>{}
            with<HungerClockComponent>{}
        }
    }

    fun healthPotion(engine : Engine, x : Int, y : Int){
        engine.entity {
            with<PositionComponent> {
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            with<NameComponent>{
                name = "Health Potion"
            }
            with<ItemComponent>{}
            with<ProvidesHealingComponent>{
                healAmount = 2
            }
            with<ConsumableComponent>{}
        }
    }

    fun dagger(engine: Engine, x : Int, y : Int){
        engine.entity {
            with<PositionComponent> {
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            with<NameComponent>{
                name = "Dagger"
            }
            with<ItemComponent>{}
            with<EquippableComponent>{
                slot = EquipmentSlot.MELEE
            }
            with<MeleePowerBonusComponent>{
                power = 2
            }
        }
    }

    fun longsword(engine: Engine, x : Int, y : Int){
        engine.entity {
            with<PositionComponent> {
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            with<NameComponent>{
                name = "Longsword"
            }
            with<ItemComponent>{}
            with<EquippableComponent>{
                slot = EquipmentSlot.MELEE
            }
            with<MeleePowerBonusComponent>{
                power = 4
            }
        }
    }

    fun shield(engine: Engine, x : Int, y : Int){
        engine.entity {
            with<PositionComponent> {
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            with<NameComponent>{
                name = "Shield"
            }
            with<ItemComponent>{}
            with<EquippableComponent>{
                slot = EquipmentSlot.SHIELD
            }
            with<DefenseBonusComponent>{
                defense = 1
            }
        }
    }

    fun towerShield(engine: Engine, x : Int, y : Int){
        engine.entity {
            with<PositionComponent> {
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            with<NameComponent>{
                name = "Tower Shield"
            }
            with<ItemComponent>{}
            with<EquippableComponent>{
                slot = EquipmentSlot.SHIELD
            }
            with<DefenseBonusComponent>{
                defense = 3
            }
        }
    }


    fun rations(engine: Engine, x : Int, y: Int){
        engine.entity{
            with<PositionComponent>{
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            with<NameComponent>{
                this.name = "Rations"
            }
            with<ItemComponent>()
            with<ProvidesFoodComponent>()
            with<ConsumableComponent>()
        }
    }


    fun mapScroll(engine: Engine, x : Int, y: Int){
        engine.entity{
            with<PositionComponent>{
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            with<NameComponent>{
                this.name = "Map Scroll"
            }
            with<ItemComponent>()
            with<MapRevealerComponent>()
            with<ConsumableComponent>()
        }
    }

    fun bearTrap(engine: Engine, x : Int, y: Int){
        engine.entity{
            with<PositionComponent>{
                this.x = x
                this.y = y
            }
            with<RenderableComponent>()
            with<NameComponent>{
                this.name = "Bear Trap"
            }
            with<HiddenComponent>()
            with<EntryTriggerComponent>()
            with<InflictDamageComponent>{
                damage = 6
            }
            with<SingleActivationComponent>{}
        }
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
                val x = room.x.toInt() + MathUtils.random(0, room.width.toInt()-1)
                val y = room.y.toInt() + MathUtils.random(0, room.height.toInt()-1)
                if (x to y in spawnPoints){ tries += 1; continue}
                spawnPoints[x to y] = rollSpawnTable(spawnTable)
                added = true
            }
        }


        for ((position, name) in spawnPoints){
            val (x, y) = position
            when(name){
                "Rat" -> rat(engine, x, y)
                "Scorpion" -> scorpion(engine, x, y)
                "Health Potion" -> healthPotion(engine, x, y)
                "Dagger" -> dagger(engine, x, y)
                "Shield" -> shield(engine, x, y)
                "Longsword" -> longsword(engine, x, y)
                "Tower Shield" -> towerShield(engine, x, y)
                "Rations" -> rations(engine, x, y)
                "Map Scroll" -> mapScroll(engine, x, y)
                "Bear Trap" -> bearTrap(engine, x, y)
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
