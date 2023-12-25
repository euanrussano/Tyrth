package com.sophia.tyrth

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
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



}
