package com.sophia.tyrth.infrastructure.factory.hero

import com.sophia.tyrth.Assets
import com.sophia.tyrth.model.hero.Hero
import com.sophia.tyrth.model.hero.HeroTexturePreFab

class HeroFactory {
    fun createHero(): Hero {
        val heroPreFab = HeroTexturePreFab(Assets.hero)
        val hero = Hero(0 to 0, heroPreFab)
        return hero
    }

}
