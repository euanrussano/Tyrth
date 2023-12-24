package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.viewport.Viewport
import com.sophia.tyrth.GameLog
import com.sophia.tyrth.PersistenceService
import com.sophia.tyrth.ecs.component.*
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.actors.txt
import ktx.ashley.allOf
import ktx.ashley.plusAssign
import ktx.scene2d.*
import kotlin.math.min

class GUISystem(val viewport: Viewport, val batch: Batch) : EntitySystem() {

    private var accumulator: Float = 0f

    private var inventoryWindow: Window
    private val inventoryTable: Table

    private val messagesTable: Table

    private val hpStringFormat = "%2d / %2d"
    private val hpLabel: Label
    private val hpBar: ProgressBar

    val stage = Stage(viewport, batch)

    init {
        inventoryWindow = scene2d.window("Inventory"){
            this.padTop(10f)
            this.defaults().pad(5f)
            table {
                it.grow()
                this.top()
                this.defaults().pad(2f)
                inventoryTable = this
            }
            row()
            textButton("Close"){
                onClick {
                    this@window.remove()
                }
            }
            pack()
        }

        stage.actors {
            table {
                setFillParent(true)
                table {
                    it.growX()
                    this.defaults().pad(10f)
                    textButton(" Inventory"){
                        this.pad(5f)
                        onClick {
                            stage.addActor(inventoryWindow)
                            inventoryWindow.centerPosition(stage.width, stage.height)
                        }
                    }
                    textButton("Save Game"){
                        this.pad(5f)
                        onClick {
                            engine?.let { PersistenceService.saveGame(it) }
                        }
                    }
                }
                row()
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

        val im = Gdx.input.inputProcessor as InputMultiplexer
        im.addProcessor(0, stage)
    }

    override fun update(deltaTime : Float) {
        super.update(deltaTime)

        // update the stage "only" 5 times per second (instead of theoretical 60x)
        accumulator += deltaTime
        if (accumulator >= 0.2f){
            updateStage()
            accumulator = 0f
        }

        viewport.apply()
        stage.act(deltaTime)
        stage.draw()


    }

    private fun updateStage() {
        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
        val health = HealthComponent.ID[hero]
        val backpack = BackpackComponent.ID[hero]

        hpLabel.txt = hpStringFormat.format(health.hp, health.maxHP)
        hpBar.value = health.hp.toFloat()/health.maxHP.toFloat()

        messagesTable.clear()
        for (entry in GameLog.entries.takeLast(min(GameLog.entries.size, 3))){
            messagesTable.add(entry)
            messagesTable.row()
        }

        inventoryTable.clear()
        for(item in engine.getEntitiesFor(allOf(InBackpackComponent::class).get())){
            val backpackID = InBackpackComponent.ID[item].backpackID
            if (backpackID != backpack.ID) continue

            val name = NameComponent.ID[item].name
            inventoryTable.add(name)
            inventoryTable.add(scene2d.textButton("Use"){
                onClick {
                    hero += WantsToUseItemComponent().apply{ this.item = item}
                }
            })
            inventoryTable.add(scene2d.textButton("Drop"){
                onClick {
                    hero += WantsToDropItemComponent().apply{ this.item = item}
                }
            })
            inventoryTable.row()
        }
        inventoryWindow.pack()

    }

}
