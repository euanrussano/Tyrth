package com.sophia.tyrth.model

import com.sophia.tyrth.model.manager.WorldManager

class ParticleVanishingManager(val world: World) : WorldManager {

    override fun update(delta: Float) {
        val iter = world.particles.iterator()
        while(iter.hasNext()){
            val particle = iter.next()
            if (particle.lifeTimeMs <= 0) iter.remove()
        }
    }

}
