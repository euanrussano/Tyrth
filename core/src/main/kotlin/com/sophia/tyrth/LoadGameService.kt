package com.sophia.tyrth

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.XmlReader
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.entity
import ktx.ashley.plusAssign
import java.io.BufferedReader
import java.io.FileReader

object LoadGameService {

    val entities = mutableListOf<Entity>()
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
        val itemsElement = root.getChildByName("Items")
        for (i in 0 until itemsElement.childCount){
            val itemElement = itemsElement.getChild(i)
            deserializeEntity(engine, itemElement)
        }
        val monstersElement = root.getChildByName("Monsters")
        for (i in 0 until monstersElement.childCount){
            val monsterElement = monstersElement.getChild(i)
            deserializeEntity(engine, monsterElement)
        }
        val heroElement = root.getChildByName("Hero")
        deserializeEntity(engine, heroElement)

        file.close()
    }

    private fun deserializeEntity(engine: Engine, tileElement: XmlReader.Element) {
        val entity = engine.entity()
        entities.add(entity)
        for (i in 0 until tileElement.childCount){
            val componentElement = tileElement.getChild(i)
            val component: Component? =
            when(componentElement.name){
                BackpackComponent::class.simpleName -> {
                    BackpackComponent().apply {
//                        ID = componentElement.getInt("ID")
                        maxWeight = componentElement.getInt("maxWeight")
                        val itemsElement = componentElement.getChildByName("items")
                        for (j in 0 until itemsElement.childCount){
                            val itemElement = itemsElement.getChild(j)
                            val id = itemElement.getInt("id")
                            val item = entities[id]
                            items.add(item)
                        }
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
                DefenseBonusComponent::class.simpleName->{
                    DefenseBonusComponent().apply {
                        defense = componentElement.getInt("defense")
                    }
                }
                DownStairsComponent::class.simpleName ->{
                    DownStairsComponent()
                }
                EquipmentHolderComponent::class.simpleName ->{
                    EquipmentHolderComponent().apply {
                        val slotsElement = componentElement.getChildByName("slots")
                        for (j in 0 until slotsElement.childCount){
                            val slotElement = slotsElement.getChild(j)
                            val name = slotElement.getChild(0).name
                            val id = slotElement.getInt("id")
                            val currentSlot = EquipmentSlot.entries.first {
                                it.name == name
                            }
                            val entity2 = entities[id]
                            slots[currentSlot] = entity2
                        }
                    }
                }
                EquippableComponent::class.simpleName ->{
                    EquippableComponent()
                }
                FieldOfViewComponent::class.simpleName ->{
                    FieldOfViewComponent().apply {
                        range = componentElement.getInt("range")
                        val revealedTilesElement = componentElement.getChildByName("revealedTiles")
                        for (j in 0 until revealedTilesElement.childCount){
                            val child = revealedTilesElement.getChild(j)
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
                ItemComponent::class.simpleName ->{
                    ItemComponent()
                }
                MeleePowerBonusComponent::class.simpleName->{
                    MeleePowerBonusComponent().apply {
                        power = componentElement.getInt("power")
                    }
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
                    Gdx.app.error(this::class.simpleName, "Component <${componentElement.name}> was not recognized not loading")
                    null
                }
            }
            component?.let {  entity += it }

        }
    }

}
