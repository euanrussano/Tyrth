package com.sophia.tyrth.infrastructure.factory.hero

import com.sophia.tyrth.Assets
import com.sophia.tyrth.model.Backpack
import com.sophia.tyrth.model.hero.Hero
import com.sophia.tyrth.model.hero.HeroTexturePreFab

class HeroFactory {
    fun createHero(): Hero {
        val heroPreFab = HeroTexturePreFab(Assets.hero)
        val hero = Hero("Scott", 0 to 0, heroPreFab, Backpack(3))
        return hero
    }

}
