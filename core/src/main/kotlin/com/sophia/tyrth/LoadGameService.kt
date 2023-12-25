package com.sophia.tyrth

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.XmlReader
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.plusAssign
import java.io.BufferedReader
import java.io.FileReader
import javax.naming.Name

object LoadGameService {
    fun loadGame(engine: Engine) {
        engine.removeAllEntities()

        val file = BufferedReader(FileReader("test.xml"))
        val xml = XmlReader()
        val root = xml.parse(file)

        val mapElement = root.getChildByName("Map")
        for (i in 0 until mapElement.childCount){
            val tileElement = mapElement.getChild(i)
            deserializeEntity(engine, tileElement)
        }
        val heroElement = root.getChildByName("Hero")
        deserializeEntity(engine, heroElement)
        val monstersElement = root.getChildByName("Monsters")
        for (i in 0 until monstersElement.childCount){
            val monsterElement = monstersElement.getChild(i)
            deserializeEntity(engine, monsterElement)
        }
        val itemsElement = root.getChildByName("Items")
        for (i in 0 until itemsElement.childCount){
            val itemElement = itemsElement.getChild(i)
            deserializeEntity(engine, itemElement)
        }

        file.close()
    }

    private fun deserializeEntity(engine: Engine, tileElement: XmlReader.Element) {
        val entity = engine.entity()
        for (i in 0 until tileElement.childCount){
            val componentElement = tileElement.getChild(i)
            val component: Component? =
            when(componentElement.name){
                BackpackComponent::class.simpleName -> {
                    BackpackComponent().apply {
                        ID = componentElement.getInt("ID")
                        maxWeight = componentElement.getInt("maxWeight")
                        currentWeight = componentElement.getInt("currentWeight")
                    }
                }
                BlockViewComponent::class.simpleName -> {
                    BlockViewComponent()
                }
                CollisionComponent::class.simpleName ->{
                    CollisionComponent()
                }
                CombatStatsComponent::class.simpleName ->{
                    CombatStatsComponent().apply {
                        power = componentElement.getInt("power")
                        defense = componentElement.getInt("defense")
                    }
                }
                ConsumableComponent::class.simpleName->{
                    ConsumableComponent()
                }
                FieldOfViewComponent::class.simpleName ->{
                    FieldOfViewComponent().apply {
                        range = componentElement.getInt("range")
                        val revealedTilesElement = componentElement.getChildByName("revealedTiles")
                        for (i in 0 until revealedTilesElement.childCount){
                            val child = revealedTilesElement.getChild(i)
                            val x = child.getInt("x")
                            val y = child.getInt("y")
                            println(x to y)
                            revealedTiles.add(x to y)
                        }
                    }
                }
                HealthComponent::class.simpleName -> {
                    HealthComponent().apply {
                        maxHP = componentElement.getInt("maxHP")
                        hp = componentElement.getInt("hp")
                    }
                }
                HeroComponent::class.simpleName -> {
                    HeroComponent()
                }
                InBackpackComponent::class.simpleName ->{
                    InBackpackComponent().apply {
                        backpackID = componentElement.getInt("backpackID")
                    }
                }
                ItemComponent::class.simpleName ->{
                    ItemComponent()
                }
                MonsterComponent::class.simpleName ->{
                    MonsterComponent()
                }
                NameComponent::class.simpleName -> {
                    NameComponent().apply {
                        name = componentElement.get("name")
                    }
                }
                PositionComponent::class.simpleName -> {
                    PositionComponent().apply {
                        x = componentElement.getInt("x")
                        y = componentElement.getInt("y")
                    }
                }
                ProvidesHealingComponent::class.simpleName ->{
                    ProvidesHealingComponent().apply {
                        healAmount = componentElement.getInt("healAmount")
                    }
                }
                RenderableComponent::class.simpleName ->{
                    RenderableComponent()
                }
                TileComponent::class.simpleName -> {
                    TileComponent()
                }
                VelocityComponent::class.simpleName ->{
                    VelocityComponent()
                }
                else -> {
                    Gdx.app.error(this::class.simpleName, "Component was not recognized not loading")
                    null
                }
            }
            component?.let {  entity += it }

        }
    }

}
