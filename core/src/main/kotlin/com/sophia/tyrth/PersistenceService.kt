package com.sophia.tyrth

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.XmlWriter
import com.sophia.tyrth.ecs.component.*
import ktx.ashley.allOf


object PersistenceService {
    fun saveGame(engine: Engine) {
//        val file = BufferedWriter(FileWriter("test.xml"))
//        val xml = XmlWriter(file)
//        xml.element("Map")
//        saveMap(engine, xml)
//        xml.pop()
//        xml.element("Hero")
//        saveHero(engine, xml)
//        xml.pop()
//        xml.element("Monsters")
//        saveMonsters(engine, xml)
//        xml.pop()
//
//        xml.close()
//
//        file.close()
    }

    fun loadGame(engine: Engine) {
        //TODO("Not yet implemented")
    }

    private fun saveMonsters(engine: Engine, xml: XmlWriter) {
        //TODO("Not yet implemented")
    }

    private fun saveHero(engine: Engine, xml: XmlWriter) {
//        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
//        val position = PositionComponent.ID[hero]
//        xml.element("Position")
//        .element("x").text(position.x).pop()
//        .element("y").text(position.y).pop()
//        .pop()
//        collision?.let {  xml.element("Collision").pop() }
//        blockView?.let {  xml.element("BlockView").pop() }
//        xml.element("Renderable")
//            .element("name").text(renderable.name).pop()
//            .pop()
//        xml.pop()
    }

    private fun saveMap(engine : Engine, xml : XmlWriter) {
        for (entity in engine.getEntitiesFor(allOf(TileComponent::class).get())){
            val position = PositionComponent.ID[entity]
            val collision = CollisionComponent.ID[entity]
            val blockView = BlockViewComponent.ID[entity]
            val renderable = RenderableComponent.ID[entity]

            xml.element("Tile")
                    .element("Position")
                        .element("x").text(position.x).pop()
                        .element("y").text(position.y).pop()
                    .pop()
                    collision?.let {  xml.element("Collision").pop() }
                    blockView?.let {  xml.element("BlockView").pop() }
                    xml.element("Renderable")
                        .element("name").text(renderable.name).pop()
                    .pop()
            xml.pop()

        }
    }


}
