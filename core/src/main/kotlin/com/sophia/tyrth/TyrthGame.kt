package com.sophia.tyrth

import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.kotcrab.vis.ui.VisUI
import com.sophia.tyrth.infrastructure.factory.monster.InMemoryMonsterFactory
import com.sophia.tyrth.infrastructure.factory.hero.HeroFactory
import com.sophia.tyrth.infrastructure.factory.item.InMemoryItemFactory
import com.sophia.tyrth.infrastructure.factory.terrain.InMemoryTerrainFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.DungeonTilemapFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.SimpleTilemapFactory
import com.sophia.tyrth.infrastructure.factory.tilemap.TilemapFactory
import com.sophia.tyrth.infrastructure.factory.world.WorldFactory
import com.sophia.tyrth.infrastructure.repository.item.ItemRepository
import com.sophia.tyrth.infrastructure.repository.monster.MonsterRepository
import com.sophia.tyrth.infrastructure.repository.terrain.TerrainRepository
import com.sophia.tyrth.model.World
import com.sophia.tyrth.screen.MainMenuScreen
import ktx.scene2d.Scene2DSkin
import kotlin.random.asKotlinRandom

class TyrthGame : Game() {

    val terrainRepository = TerrainRepository()
    val heroFactory = HeroFactory()
    val monsterRepository = MonsterRepository()
    val itemRepository = ItemRepository()

    lateinit var world : World

    lateinit var batch: Batch
    val UIViewport: Viewport = ExtendViewport(500f, 500f)

    val engine = PooledEngine()
    override fun create() {
        batch = SpriteBatch()

        VisUI.load()
        Scene2DSkin.defaultSkin = VisUI.getSkin()//Skin("ui/uiskin.json".toInternalFile())
        Assets.load()
        GameLog.clear()

        loadRepositories()

        setScreen(MainMenuScreen(this))
    }

    private fun loadRepositories() {
        InMemoryTerrainFactory(terrainRepository)
        InMemoryMonsterFactory(monsterRepository)
        InMemoryItemFactory(itemRepository)

    }

    fun randomWorld() {
        MathUtils.random.setSeed(995)

        val tilemapFactory = TilemapUtils.random()
        //val tilemapFactory = tilemapFactories.first()
        val worldFactory = WorldFactory(tilemapFactory, heroFactory, monsterRepository, itemRepository)

        world = worldFactory.build()

    }
}
