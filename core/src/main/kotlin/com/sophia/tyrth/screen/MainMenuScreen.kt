package com.sophia.tyrth.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.ScreenUtils
import com.sophia.tyrth.EntityFactory
import com.sophia.tyrth.LoadGameService
//import com.sophia.tyrth.MapCreator
import com.sophia.tyrth.TyrthGame
import com.sophia.tyrth.map.MapUtils
import ktx.actors.onClick
import ktx.scene2d.actors
import ktx.scene2d.table
import ktx.scene2d.textButton

class MainMenuScreen(val game: TyrthGame) : Screen {

    val stage = Stage(game.UIViewport, game.batch)

    override fun show() {
        stage.actors {
            table {
                setFillParent(true)
                this.defaults().pad(10f)
                textButton("New Game"){
                    onClick {
                        game.engine.removeAllEntities()
                        game.engine.removeAllSystems()
                        val builder = MapUtils.randomBuilder(1)
                        builder.build()
                        builder.spawnEntities(game.engine
                        )
                        val (x, y) = builder.heroPosition
                        EntityFactory.hero(game.engine, x, y)

                        game.screen = WorldScreen(game)
                    }
                }
                row()
                textButton("New Game OOP"){
                    onClick {
                        game.randomWorld()
                        game.screen = WorldScreen2(game)
                    }
                }
                row()
                textButton("Load Game"){
                    onClick {
                        LoadGameService.loadGame(game.engine)
                        game.screen = WorldScreen(game)

                    }
                }
                row()
                textButton("Editor"){
                    onClick {
                        game.screen = EditorScreen(game)

                    }
                }
                row()
                textButton("Quit"){
                    onClick {
                        Gdx.app.exit()
                    }
                }
            }
        }

        Gdx.input.inputProcessor = stage

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(Color.BLACK)

        game.UIViewport.apply()
        stage.act(delta)
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        game.UIViewport.update(width, height)
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
