package com.sophia.tyrth

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.ai.msg.MessageManager
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.utils.viewport.Viewport
import com.sophia.tyrth.ecs.component.*
import com.sophia.tyrth.model.World
import com.sophia.tyrth.model.hero.Hero
import com.sophia.tyrth.model.item.ItemInstance
import com.sophia.tyrth.screen.MainMenuScreen
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.actors.txt
import ktx.ashley.allOf
import ktx.ashley.plusAssign
import ktx.scene2d.*
import kotlin.math.min


class WorldGUI(private val game : TyrthGame) {
    private val batch = game.batch
    private val world = game.world
    private val viewport = game.UIViewport

    private val depthLabel: Label

    private var inventoryWindow: Window
    private val inventoryTable: Table

    private var equipmentWindow: Window
    private val equipmentTable: Table

    private val messagesTable: Table

    private val hungerBar: ProgressBar
    private val hungerLabel: Label

    private val hpStringFormat = "%2d / %2d"
    private val hpLabel: Label
    private val hpBar: ProgressBar

    private val gameOverTable: Table

    val stage = Stage(viewport, batch)

    init {
        gameOverTable = scene2d.table {
            label("Your journey has ended!")
            row()
            label("One day, we'll tell you all about how you did.\n That day, sadly, is not in this chapter...")
            row()
            label("Click anywhere to return to the main menu")
        }
        gameOverTable.isVisible = false

        inventoryWindow = scene2d.window("Inventory"){
            this.padTop(10f)
            this.defaults().pad(5f)
            table {
                it.grow()
                this.top().pad(2f)
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

        equipmentWindow = scene2d.window("Equipment"){
            this.padTop(10f)
            this.defaults().pad(5f)
            table {
                it.grow()
                this.top().pad(2f)
                this.defaults().pad(2f)
                equipmentTable = this
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
                    label("Depth: ")
                    label("<undef>"){ depthLabel = this }
                    textButton(" Inventory"){
                        this.pad(5f)
                        onClick {
                            stage.addActor(inventoryWindow)
                            inventoryWindow.centerPosition(stage.width, stage.height)
                        }
                    }
                    textButton(" Equipment"){
                        this.pad(5f)
                        onClick {
                            stage.addActor(equipmentWindow)
                            equipmentWindow.centerPosition(stage.width, stage.height)
                        }
                    }
                    textButton("Save Game"){
                        this.pad(5f)
                        onClick {
                            SaveGameService.saveWorld(world)
                        }
                    }
                }
                row()
                table {
                    it.grow()
                }
                row()
                table {
                    it.pad(5f).growX().height(stage.height/4f)
                    background = skin.getDrawable("white")

                    table {
                        it.grow().pad(2f)
                        background = skin.newDrawable("white", Color.BLACK)
                        this.top().left()
                        table {
                            it.growX()
                            this.top().left()
                            this.defaults().pad(5f)
                            label("<No Hunger Info>"){ hungerLabel = this}
                            progressBar(0f, 1f, 0.1f) { hungerBar = this }
                            label("HP: ")
                            label(" <undefined>"){ hpLabel = this}
                            progressBar(0f, 1f, 0.1f) { hpBar = this }
                        }
                        row()
                        table {
                            it.grow().width(width).height(height)
                            this.top().left()
                            this.defaults().pad(1f).top().left()
                            messagesTable = this
                        }
                    }
                }
            }
        }

        MessageManager.getInstance().addListener({ msg : Telegram ->
            updateDepth()
        }, Messages.DEPTH_CHANGED)

        MessageManager.getInstance().addListener({ msg : Telegram ->
            if (msg.sender == world.hero)
                updateHealth()
            false
        }, Messages.HEALTH_CHANGED)



        MessageManager.getInstance().addListener({ msg : Telegram ->
            updateHealth()
        }, Messages.HERO_HEALTH_CHANGED)

        MessageManager.getInstance().addListener({ msg : Telegram ->
            updateMessages()
        }, Messages.LOG_CHANGED)

        MessageManager.getInstance().addListener({ msg : Telegram ->
            if (msg.sender == world.hero){
                updateInventory()
                true
            }
            false
        }, Messages.INVENTORY_CHANGED)

        MessageManager.getInstance().addListener({ msg : Telegram ->
            if (msg.sender == world.hero){
                updateEquipment()
                updateInventory()
            }
            true
        }, Messages.EQUIPMENT_CHANGED)

        MessageManager.getInstance().addListener({ msg : Telegram ->
            updateEquipment()
            updateInventory()
        }, Messages.HERO_EQUIPMENT_CHANGED)

        MessageManager.getInstance().addListener({ msg : Telegram ->
            stage.clear()
            stage.addActor(gameOverTable.apply {
                setFillParent(true)
                isVisible = true
                onClick {
                    game.screen = MainMenuScreen(game)
                }
            })
            true
        }, Messages.GAME_OVER)

        initializeStage()
    }

    private fun updateDepth() : Boolean{
        depthLabel.txt = world.currentDepth.toString()
        return true
    }

    private fun initializeStage() {
        updateDepth()
        updateHealth()
        updateMessages()
        updateEquipment()
        updateInventory()
    }

    fun update(deltaTime : Float) {

        viewport.apply()
        stage.act(deltaTime)
        stage.draw()

    }

    private fun updateEquipment(): Boolean {
        val hero = world.hero
        val equipmentSlot = hero.itemSlotMap

        equipmentTable.clear()
        for ((slot, item) in equipmentSlot) {
            item ?: continue
            val name = item.item.name

            equipmentTable.add(name)
            equipmentTable.add(scene2d.textButton("Unequip") {
                onClick {
                    hero.unequip(item, slot)
                }
            })
            equipmentTable.row()
        }
        equipmentWindow.pack()
        return true
    }

    private fun updateInventory(): Boolean {
        val hero = world.hero
        val backpack = hero.backpack ?: return false

        inventoryTable.clear()
        for (itemInstance in backpack.itemInstances) {

            val name = itemInstance.item.name
            inventoryTable.add(name)
            inventoryTable.add(scene2d.textButton("Use") {
                onClick {
                    itemInstance.applyEffectOn(hero)
                }
            })
            inventoryTable.add(scene2d.textButton("Drop") {
                onClick {
                    backpack.remove(itemInstance)
                    itemInstance.position = hero.position
                }
            })
            inventoryTable.row()
        }
        inventoryWindow.pack()
        return true
    }

    private fun updateMessages(): Boolean {
        messagesTable.clear()
        for (entry in GameLog.entries.takeLast(min(GameLog.entries.size, 3))) {
            messagesTable.add(entry)
            messagesTable.row()
        }
        return true
    }

    private fun updateHealth(): Boolean {
        val hero = world.hero
        val hp = hero.hp
        val maxHP = hero.maxHP
//        val hunger = HungerClockComponent.ID[hero]
//
//        val regex = "(?<=[a-z])(?=[A-Z])".toRegex()
//        hungerLabel.txt = hunger.state.name.replace(regex, " ")
//        hungerLabel.color = when(hunger.state){
//            HungerState.WellFed -> Color.GREEN
//            HungerState.Normal -> Color.WHITE
//            HungerState.Hungry -> Color.YELLOW
//            HungerState.Starving -> Color.RED
//        }
//        hungerBar.value = hunger.duration.toFloat()/hunger.state.duration
        hpLabel.txt = hpStringFormat.format(hp, maxHP)
        hpBar.value = hp.toFloat() / maxHP.toFloat()
        return true
    }
}
