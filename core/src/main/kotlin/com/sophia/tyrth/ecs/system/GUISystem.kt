package com.sophia.tyrth.ecs.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.EntitySystem
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
import com.sophia.tyrth.*
import com.sophia.tyrth.ecs.component.*
import com.sophia.tyrth.screen.MainMenuScreen
import ktx.actors.centerPosition
import ktx.actors.onClick
import ktx.actors.txt
import ktx.ashley.allOf
import ktx.ashley.plusAssign
import ktx.scene2d.*
import kotlin.math.min

class GUISystem(val game : TyrthGame, val viewport: Viewport, val batch: Batch) : EntitySystem() {

//    private var accumulator: Float = 0f



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
                            engine?.let { SaveGameService.saveGame(it) }
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

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine ?: return

        MessageManager.getInstance().addListener({msg : Telegram ->
            updateHealth(engine)
        }, Messages.HERO_HEALTH_CHANGED)

        MessageManager.getInstance().addListener({msg : Telegram ->
            updateMessages()
        }, Messages.LOG_CHANGED)

        MessageManager.getInstance().addListener({msg : Telegram ->
            updateInventory(engine)
        }, Messages.HERO_INVENTORY_CHANGED)

        MessageManager.getInstance().addListener({msg : Telegram ->
            updateEquipment(engine)
        }, Messages.HERO_EQUIPMENT_CHANGED)

        MessageManager.getInstance().addListener({msg : Telegram ->
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

        initializeStage(engine)
    }

    private fun initializeStage(engine: Engine) {
        updateHealth(engine)
        updateMessages()
        updateEquipment(engine)
        updateInventory(engine)
    }

    override fun update(deltaTime : Float) {
        super.update(deltaTime)

        viewport.apply()
        stage.act(deltaTime)
        stage.draw()

    }

    private fun updateEquipment(engine: Engine): Boolean {
        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
        val equipmentHold = EquipmentHolderComponent.ID[hero]

        equipmentTable.clear()
        for ((slot, item) in equipmentHold.slots) {
            item ?: continue
            val name = NameComponent.ID[item].name

            equipmentTable.add(name)
            equipmentTable.add(scene2d.textButton("Unequip") {
                onClick {
                    hero += WantsToUnequipItemComponent().apply { this.item = item }
                }
            })
            equipmentTable.row()
        }
        equipmentWindow.pack()
        return true
    }

    private fun updateInventory(engine: Engine): Boolean {
        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
        val backpack = BackpackComponent.ID[hero]

        inventoryTable.clear()
        for (item in backpack.items) {
    //            val backpackID = InBackpackComponent.ID[item].backpackID
    //            if (backpackID != backpack.ID) continue

            val name = NameComponent.ID[item].name
            inventoryTable.add(name)
            inventoryTable.add(scene2d.textButton("Use") {
                onClick {
                    hero += WantsToUseItemComponent().apply { this.item = item }
                }
            })
            inventoryTable.add(scene2d.textButton("Drop") {
                onClick {
                    hero += WantsToDropItemComponent().apply { this.item = item }
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

    private fun updateHealth(engine: Engine): Boolean {
        val hero = engine.getEntitiesFor(allOf(HeroComponent::class).get()).first()
        val health = HealthComponent.ID[hero]
        val hunger = HungerClockComponent.ID[hero]

        val regex = "(?<=[a-z])(?=[A-Z])".toRegex()
        hungerLabel.txt = hunger.state.name.replace(regex, " ")
        hungerLabel.color = when(hunger.state){
            HungerState.WellFed ->Color.GREEN
            HungerState.Normal -> Color.WHITE
            HungerState.Hungry -> Color.YELLOW
            HungerState.Starving -> Color.RED
        }
        hungerBar.value = hunger.duration.toFloat()/hunger.state.duration
        hpLabel.txt = hpStringFormat.format(health.hp, health.maxHP)
        hpBar.value = health.hp.toFloat() / health.maxHP.toFloat()
        return true
    }

}
