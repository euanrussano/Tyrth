package com.sophia.tyrth.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.Screen
import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.sophia.tyrth.HeroAction
import com.sophia.tyrth.TyrthGame
import com.sophia.tyrth.WorldGUI
import com.sophia.tyrth.WorldRenderer

class WorldScreen2(val game: TyrthGame) : Screen {

    val worldViewport = ExtendViewport(20f, 20f)

    val worldRenderer = WorldRenderer(game.batch as SpriteBatch, worldViewport, game.world)
    val worldGUI = WorldGUI(game)

    override fun show() {
        val im = InputMultiplexer()
        im.addProcessor( worldGUI.stage)
        im.addProcessor(worldRenderer)
        Gdx.input.inputProcessor = im
    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.BLACK)
        update(delta)

        draw()
        worldGUI.update(delta)
    }

    private fun update(delta: Float) {
        GdxAI.getTimepiece().update(delta)

        var heroAction = HeroAction.NONE
        if (Gdx.input.isKeyJustPressed(Keys.W)){
            heroAction = HeroAction.MOVE_UP
        } else if (Gdx.input.isKeyJustPressed(Keys.S)){
            heroAction = HeroAction.MOVE_DOWN
        } else if (Gdx.input.isKeyJustPressed(Keys.A)){
            heroAction = HeroAction.MOVE_LEFT
        } else if (Gdx.input.isKeyJustPressed(Keys.D)){
            heroAction = HeroAction.MOVE_RIGHT
        }

        if (Gdx.input.isKeyJustPressed(Keys.SPACE)){
            worldRenderer.debug = !worldRenderer.debug
        }
        game.world.update(delta, heroAction)
    }

    private fun draw() {
        worldRenderer.render()
    }

    override fun resize(width: Int, height: Int) {
        worldViewport.update(width, height)
        game.UIViewport.update(width, height)
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
