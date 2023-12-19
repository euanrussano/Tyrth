package com.sophia.tyrth.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.MapCreator
import com.sophia.tyrth.TyrthGame
import com.sophia.tyrth.ecs.component.MonsterComponent
import com.sophia.tyrth.ecs.system.*
import ktx.ashley.add

class WorldScreen(tyrthGame: TyrthGame) : Screen {

    private val guiViewport = ExtendViewport(500f, 500f)
    private val worldViewport = ExtendViewport(20f, 20f)
    private val batch = SpriteBatch()

    private val engine = PooledEngine()

    override fun show() {
        engine.addSystem(InputSystem())
        engine.addSystem(MonsterAI())

        engine.addSystem(CollisionSystem())
        engine.addSystem(MeeleCombatSystem())
        engine.addSystem(MovementSystem())
        engine.addSystem(VisibilitySystem())
        engine.addSystem(DeathSystem())
        engine.addSystem(RenderingSystem(worldViewport, batch))
        engine.addSystem(GUISystem(guiViewport, batch))
        //MapCreator.mapTest(engine)
        MapCreator.mapDungeon(engine)

        GameLog.entries.add("Welcome to Tyrth!")
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.BLACK)

        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        worldViewport.update(width, height, true)
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

}
