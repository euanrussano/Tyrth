package com.sophia.tyrth.model

import com.sophia.tyrth.GameLog
import com.sophia.tyrth.HeroAction
import com.sophia.tyrth.ecs.system.MeeleCombatSystem
import com.sophia.tyrth.model.hero.Hero
import com.sophia.tyrth.model.item.ItemInstance
import com.sophia.tyrth.model.manager.*
import com.sophia.tyrth.model.tilemap.Tilemap
import com.sophia.tyrth.model.monster.MonsterInstance

class World(
    val tilemaps: MutableList<Tilemap>,
    val hero : Hero,
    val monsterInstancesByDepth : MutableList<MutableList<MonsterInstance>>,
    val itemInstancesByDepth : MutableList<MutableList<ItemInstance>>,
    val particles : MutableList<Particle> = mutableListOf(),
    var currentDepth : Int = 0,
) {

    var isGameOver: Boolean = false

    val monsterInstances : List<MonsterInstance>
        get() = monsterInstancesByDepth[currentDepth]
    val itemInstances : List<ItemInstance>
        get() = itemInstancesByDepth[currentDepth]
    val tilemap: Tilemap
        get() = tilemaps[currentDepth]
    val entities: List<Entity>
        get() = listOf(hero).plus(monsterInstances)

    val managers : List<WorldManager> = listOf(
        MeeleCombatManager(this),
        CollisionManager(this),
        MovementManager(this),
        VisibilityManager(this),
        DeathManager(this),
        ItemPickupManager(this),
        ParticleVanishingManager(this),
        DepthManager(this),
        GameOverManager(this)
    )


    fun update(delta: Float, heroAction: HeroAction) {
        if (isGameOver) return

        hero.update(delta, heroAction)
        monsterInstances.forEach { it.update(delta) }
        particles.forEach { it.update(delta) }

        managers.forEach { it.update(delta) }

        entities.forEach { it.velocity = 0 to 0 }
    }

}
