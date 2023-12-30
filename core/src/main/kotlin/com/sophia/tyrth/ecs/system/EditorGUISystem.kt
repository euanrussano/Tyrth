package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.Viewport
import com.kotcrab.vis.ui.widget.VisTextField
import com.sophia.tyrth.TyrthGame
import com.sophia.tyrth.ecs.component.WantsToMoveCamera
import com.sophia.tyrth.ecs.component.WantsToZoomIn
import com.sophia.tyrth.ecs.component.WantsToZoomOut
import com.sophia.tyrth.map.MapUtils
import ktx.actors.onClick
import ktx.ashley.plusAssign
import ktx.scene2d.Scene2dDsl
import ktx.scene2d.actors
import ktx.scene2d.table
import ktx.scene2d.textButton
import ktx.scene2d.vis.*
import kotlin.math.roundToInt
import com.badlogic.gdx.utils.Array as GdxArray

class EditorGUISystem(val game: TyrthGame, val UIViewport: Viewport, val worldViewport : Viewport, val batch: Batch) : EntitySystem(), InputProcessor {

    private lateinit var depthText: VisTextField
    val stage = Stage(UIViewport, batch)
    val current = Vector2()
    val delta = Vector2()
    val last = Vector2(-1f, -1f)

    init {
        stage.actors{
            table {
                setFillParent(true)
                visTable {
                    it.growX()
                    menuBar {
                        it.top().growX().expandY()
                        menu("New Map"){
                            val builders = MapUtils.builders(1)
                            for ((name, builder) in builders){
                                menuItem(name){
                                    onClick {
                                        engine.removeAllEntities()
                                        builder.depth = depthText.text.toInt()
                                        builder.build()
                                        builder.spawnEntities(engine)
                                    }
                                }
                            }
                        }
                    }
                    visLabel("Depth:")
                    visTextField {
                        depthText = this
                        text = "1"
                    }
                }
                row()
                table {
                    it.growX()
                    background = skin.newDrawable("white", Color.DARK_GRAY)
                    textButton("-"){
                        onClick {
                            engine.entities[0] += WantsToZoomOut()
                        }
                    }
                    textButton("+"){
                        onClick {
                            engine.entities[0] += WantsToZoomIn()
                        }
                    }

                }
                row()
                table {
                    it.grow()
                }
            }
        }

        val im = Gdx.input.inputProcessor as InputMultiplexer
        im.addProcessor(0, stage)
        im.addProcessor(1, this)

    }

    override fun update(deltaTime : Float) {
        super.update(deltaTime)

        UIViewport.apply()
        stage.act(deltaTime)
        stage.draw()

    }

    override fun keyDown(keycode: Int): Boolean {
        val entity = engine.entities[0]
        when(keycode){
            Input.Keys.RIGHT -> entity += WantsToMoveCamera().apply{dx = 1}
            Input.Keys.LEFT -> entity += WantsToMoveCamera().apply{dx = -1}
            Input.Keys.UP -> entity += WantsToMoveCamera().apply{dy = 1}
            Input.Keys.DOWN -> entity += WantsToMoveCamera().apply{dy = -1}
        }
        return true
    }

    override fun keyUp(keycode: Int): Boolean {
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        current.set(screenX.toFloat(), screenY.toFloat())
        worldViewport.unproject(current)
        return true
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        last.set(-1f, -1f)
        return true
    }

    override fun touchCancelled(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        current.set(screenX.toFloat(), screenY.toFloat())
        worldViewport.unproject(current)
        if (last.x != -1f && last.y != 1f){
            worldViewport.unproject(delta.set(last.x, last.y))
            delta.sub(current)
            engine.entities[0] += WantsToMoveCamera().apply { dx = MathUtils.round(delta.x);  dy = MathUtils.round(delta.y) }
        }
        last.set(screenX.toFloat(), screenY.toFloat())
        return true
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        if (amountY > 0) engine.entities[0] += WantsToZoomOut()
        else  engine.entities[0] += WantsToZoomIn()

        return true
    }

}
