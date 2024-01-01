package com.sophia.tyrth.model

import com.badlogic.gdx.graphics.g2d.TextureRegion

object ParticleGenerator {
    fun generate(world: World, position: Pair<Int, Int>, textureRegion: TextureRegion) {
        val particle = Particle(position,200, textureRegion)
        world.particles.add(particle)
    }

}
