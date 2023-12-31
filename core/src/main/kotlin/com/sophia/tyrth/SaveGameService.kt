package com.sophia.tyrth

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.XmlReader
import com.badlogic.gdx.utils.XmlWriter
import com.sophia.tyrth.ecs.component.*
import com.sophia.tyrth.model.World
import ktx.ashley.allOf
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter


object SaveGameService {

    private var entities = mutableListOf<Entity>()

    fun saveGame(engine: Engine) {
        entities.clear()
        val file = BufferedWriter(FileWriter("test.xml"))
        val xml = XmlWriter(file)
        // put all entities in a list for giving an id
        xml.element("Entities")
            xml.element("Map")
                for (entity in engine.getEntitiesFor(allOf(TileComponent::class).get())){
                    xml.element("Tile")
                    serializeEntity(xml, entity)
                    xml.pop()
                }
            xml.pop()
            xml.element("Items")
                for (item in engine.getEntitiesFor(allOf(ItemComponent::class).get())){
                    xml.element("Item")
                    serializeEntity(xml, item)
                    xml.pop()
                }
            xml.pop()
            xml.element("Monsters")
            for (monster in engine.getEntitiesFor(allOf(MonsterComponent::class).get())){
                xml.element("Monster")
                serializeEntity(xml, monster)
                xml.pop()
            }
            xml.pop()
            val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
            xml.element("Hero")
            serializeEntity(xml, hero)
            xml.pop()
        xml.pop()

        xml.close()

        file.close()
    }

    private fun serializeEntity(xml: XmlWriter, entity: Entity) {
        entities.add(entity)
        for (component in entity.components){
            xml.element(component::class.simpleName)
            when(component::class){
                BackpackComponent::class -> {
                    component as BackpackComponent
                    xml.element("items")
                        for (item in component.items){
                            val id = entities.indexOf(item)
                            if (id == -1){
                                throw Error("Entity not found for saving!!")
                            }
                            xml.element("item")
                                xml.element("id").text(id).pop()
                            xml.pop()
                        }
                    xml.pop()
                    xml.element("maxWeight").text(component.maxWeight).pop()
                }
                CombatStatsComponent::class -> {
                    component as CombatStatsComponent
                    xml.element("power").text(component.power).pop()
                    xml.element("defense").text(component.defense).pop()
                }
                DefenseBonusComponent::class ->{
                    component as DefenseBonusComponent
                    xml.element("defense").text(component.defense).pop()
                }
                EquipmentHolderComponent::class ->{
                    component as EquipmentHolderComponent
                    xml.element("slots")
                    for ((slot, item) in component.slots){
                        if (item == null) continue
                        val id = entities.indexOf(item)
                        xml.element("slot")
                            xml.element(slot.name).pop()
                            xml.element("id").text(id).pop()
                        xml.pop()
                    }
                    xml.pop()
                }
                FieldOfViewComponent::class ->{
                    component as FieldOfViewComponent
                    xml.element("range").text(component.range).pop()
                    xml.element("revealedTiles")
                    for ((x, y) in component.revealedTiles){
                        xml.element("Tile")
                        xml.element("x").text(x).pop()
                        xml.element("y").text(y).pop()
                        xml.pop()
                    }
                    xml.pop()
                }
                HealthComponent::class -> {
                    component as HealthComponent
                    xml.element("maxHP").text(component.maxHP).pop()
                    xml.element("hp").text(component.hp).pop()
                }
                MeleePowerBonusComponent::class ->{
                    component as MeleePowerBonusComponent
                    xml.element("power").text(component.power).pop()
                }
                NameComponent::class -> {
                    component as NameComponent
                    xml.element("name").text(component.name).pop()
                }
                PositionComponent::class ->{
                    component as PositionComponent
                    val (x, y) = component.x to component.y
                    xml.element("x").text(x).pop()
                    xml.element("y").text(y).pop()
                }
                ProvidesHealingComponent::class ->{
                    component as ProvidesHealingComponent
                    xml.element("healAmount").text(component.healAmount).pop()
                }
                else ->{
                }
            }
            xml.pop()
        }

    }

    fun saveWorld(world: World) {
        val json = Json()
//        println(json.prettyPrint(world.hero))
        println(json.prettyPrint(world.monsterInstances[0]))
//        println(json.prettyPrint(world.itemInstances[0]))
    }


}
