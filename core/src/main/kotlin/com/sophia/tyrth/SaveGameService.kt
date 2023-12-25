package com.sophia.tyrth

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.XmlReader
import com.badlogic.gdx.utils.XmlWriter
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter


object SaveGameService {
    fun saveGame(engine: Engine) {
        val file = BufferedWriter(FileWriter("test.xml"))
        val xml = XmlWriter(file)
        xml.element("Entities")
            xml.element("Map")
                for (entity in engine.getEntitiesFor(allOf(TileComponent::class).get())){
                    xml.element("Tile")
                    serializeEntity(xml, entity)
                    xml.pop()
                }
            xml.pop()
            val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
            xml.element("Hero")
                serializeEntity(xml, hero)
            xml.pop()
            xml.element("Monsters")
                for (monster in engine.getEntitiesFor(allOf(MonsterComponent::class).get())){
                    xml.element("Monster")
                    serializeEntity(xml, monster)
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
        xml.pop()

        xml.close()

        file.close()
    }

    private fun serializeEntity(xml: XmlWriter, entity: Entity) {
        for (component in entity.components){
            xml.element(component::class.simpleName)
            when(component::class){
                BackpackComponent::class -> {
                    component as BackpackComponent
                    xml.element("ID").text(component.ID).pop()
                    xml.element("currentWeight").text(component.currentWeight).pop()
                    xml.element("maxWeight").text(component.maxWeight).pop()
                }
                CombatStatsComponent::class -> {
                    component as CombatStatsComponent
                    xml.element("power").text(component.power).pop()
                    xml.element("defense").text(component.defense).pop()
                }
                HealthComponent::class -> {
                    component as HealthComponent
                    xml.element("maxHP").text(component.maxHP).pop()
                    xml.element("hp").text(component.hp).pop()
                }
                InBackpackComponent::class ->{
                    component as InBackpackComponent
                    xml.element("backpackID").text(component.backpackID).pop()
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
                else ->{
                }
            }
            xml.pop()
        }

    }






}
