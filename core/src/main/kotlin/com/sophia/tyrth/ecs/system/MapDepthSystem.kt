package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.ai.msg.MessageManager
import com.sophia.tyrth.GameLog
//import com.sophia.tyrth.MapCreator
import com.sophia.tyrth.Messages
import com.sophia.tyrth.ecs.component.*
import com.sophia.tyrth.map.MapUtils
import ktx.ashley.allOf
import ktx.ashley.oneOf
import kotlin.math.max
import kotlin.math.min

class MapDepthSystem : IteratingSystem(
    allOf(
        DownStairsComponent::class,
        PositionComponent::class
    ).get()
) {
    var currentDepth = 1
    val hero : Entity
        get() = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        val (heroX, heroY) = with(PositionComponent.ID[hero]){ x to y}

        val (x, y) = with(PositionComponent.ID[entity]){x to y}
//        println("$x. $y")

        if (heroX != x || heroY != y) return

        nextDepth()
    }

    private fun nextDepth() {
        currentDepth += 1
        removeEntitiesOnDepthChange()

        val builder = MapUtils.randomBuilder(currentDepth)
        builder.build()
        builder.spawnEntities(engine)

        // build the map
//        val rooms = MapCreator.mapDungeon(engine, currentDepth)

        // reposition the hero
        val position = PositionComponent.ID[hero]
//        position.x = rooms.first().x.toInt()
//        position.y = rooms.first().y.toInt()
        position.x = builder.heroPosition.first
        position.y = builder.heroPosition.second

        // notify hero and give some health
        GameLog.add("You descend to the next level, and take a moment to heal.")
        val health = HealthComponent.ID[hero]
        health.hp = max(health.hp, health.maxHP/2)
        MessageManager.getInstance().dispatchMessage(Messages.HERO_HEALTH_CHANGED)
    }

    private fun removeEntitiesOnDepthChange() {
        // remove all monsters and tiles (with position component)
        engine.removeAllEntities(allOf(PositionComponent::class).oneOf(ItemComponent::class, MonsterComponent::class, TileComponent::class).get())

        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
        val heroBackpack = BackpackComponent.ID[hero]
        val heroEquipmentHold = EquipmentHolderComponent.ID[hero]

        // remove all items that are in a backpack not from hero, or not being equipped by hero
        for (item in engine.getEntitiesFor(allOf(ItemComponent::class).get())){
            if (item in heroBackpack.items) continue
            if (item in heroEquipmentHold.slots.values) continue
            engine.removeEntity(item)
        }



    }



}
