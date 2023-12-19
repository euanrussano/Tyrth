package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.Viewport
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.ecs.component.HealthComponent
import com.sophia.tyrth.ecs.component.HeroComponent
import ktx.actors.txt
import ktx.ashley.allOf
import ktx.scene2d.*
import kotlin.math.min

class GUISystem(val viewport: Viewport, val batch: SpriteBatch) : EntitySystem() {

    private var messagesTable: Table
    private val hpStringFormat = "%2d / %2d"
    private val hpLabel: Label
    private val hpBar: ProgressBar

    val stage = Stage(viewport, batch)

    init {
        stage.actors {
            table {
                setFillParent(true)
                table {
                    it.grow()
                }
                row()
                table {
                    it.pad(5f).growX().height(stage.height/5f)
                    background = skin.getDrawable("white")

                    table {
                        it.grow().pad(2f)
                        background = skin.getDrawable("black")
                        this.top().left()
                        table {
                            it.growX()
                            this.top().left()
                            this.defaults().pad(5f)
                            label("HP: ")
                            label(" <undefined>"){ hpLabel = this}
                            progressBar(0f, 1f, 0.1f) { hpBar = this }
                        }
                        row()
                        table {
                            it.grow().width(width).height(height)
                            this.top().left()
                            this.defaults().pad(5f).top().left()
                            messagesTable = this
                        }
                    }
                }
            }
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        updateStage()

        viewport.apply()
        stage.act(deltaTime)
        stage.draw()


    }

    private fun updateStage() {
        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
        val health = HealthComponent.ID[hero]

        hpLabel.txt = hpStringFormat.format(health.hp, health.maxHP)
        hpBar.value = health.hp.toFloat()/health.maxHP.toFloat()

        messagesTable.clear()
        for (entry in GameLog.entries.takeLast(min(GameLog.entries.size, 3))){
            messagesTable.add(entry)
            messagesTable.row()
        }
    }

}
