package com.sophia.tyrth.screen

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
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

class WorldScreen(val game: TyrthGame) : Screen {

    private val guiViewport = game.UIViewport
    private val worldViewport = ExtendViewport(20f, 20f)
    private val engine = game.engine

    override fun show() {
        val im = InputMultiplexer()
        Gdx.input.inputProcessor = im

        engine.addSystem(InputSystem(worldViewport))
        engine.addSystem(MonsterAI())

        engine.addSystem(CollisionSystem())
        engine.addSystem(MeeleCombatSystem())
        engine.addSystem(MovementSystem())
        engine.addSystem(MapDepthSystem())
        engine.addSystem(ItemCollectingSystem())
        engine.addSystem(ItemUsingSystem())
        engine.addSystem(ItemUnequippingSystem())
        engine.addSystem(ItemDroppingSystem())
        engine.addSystem(VisibilitySystem())
        engine.addSystem(DeathSystem())
        engine.addSystem(CameraControlSystem(worldViewport))
        engine.addSystem(RenderingSystem(worldViewport, game.batch))
        engine.addSystem(GUISystem(game.UIViewport, game.batch))


        GameLog.add("Welcome to Tyrth!")
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
