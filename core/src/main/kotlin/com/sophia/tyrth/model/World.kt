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
    val monsterInstances : MutableList<MonsterInstance>,
    val itemInstances : MutableList<ItemInstance>,
    var currentDepth : Int = 0,
) {

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
        DepthManager(this)
    )

    fun update(delta: Float, heroAction: HeroAction) {
        hero.update(delta, heroAction)
        monsterInstances.forEach { it.update(delta) }

        managers.forEach { it.update(delta) }

        entities.forEach { it.velocity = 0 to 0 }
    }

}
