package com.sophia.tyrth

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.with

object EntityFactory {

    fun monster(engine: Engine, x: Int, y: Int) {
        var texture = Assets.tilesheet[8][31] // Rat
        var name = "Rat"
        if (MathUtils.randomBoolean()){
            texture = Assets.tilesheet[5][24] // Scorpion
            name = "Scorpion"
        }
        engine.entity {
            with<PositionComponent> {
                this.x = x
                this.y = y
            }
            with<RenderableComponent> {
                this.texture = texture
            }
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
            with<RenderableComponent> {
                texture = Assets.tilesheet[0][25]
            }
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



        }
    }

    fun tile(engine: Engine, x: Int, y: Int, isWall: Boolean) {
        engine.entity {
            with<PositionComponent>{
                this.x = x
                this.y = y
            }
            with<RenderableComponent>{
                texture = if (isWall) Assets.tilesheet[13][0] else Assets.floor
            }
            if(isWall){
                with<CollisionComponent>()
                with<BlockViewComponent>{}
            }
        }
    }
}
