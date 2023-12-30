package com.sophia.tyrth.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.sophia.tyrth.TyrthGame
import com.sophia.tyrth.ecs.system.*
import ktx.ashley.add
import ktx.ashley.entity

class EditorScreen(val game: TyrthGame) : Screen {

    private val worldViewport = ExtendViewport(20f, 20f)
    private val engine = game.engine

    override fun show() {
        val im = InputMultiplexer()
        Gdx.input.inputProcessor = im

        engine.addSystem(CameraControlSystem(worldViewport))
        engine.addSystem(RenderingSystem(worldViewport, game.batch).apply { debug = true })
        engine.addSystem(EditorGUISystem(game, game.UIViewport, worldViewport, game.batch))

        // basic controlling entity
        engine.entity {
        }
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.BLACK)

        engine.update(delta)
    }

    override fun resize(width: Int, height: Int) {
        worldViewport.update(width, height, true)
        game.UIViewport.update(width, height, true)
    }

    override fun pause() {
        TODO("Not yet implemented")
    }

    override fun resume() {
        TODO("Not yet implemented")
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

}
