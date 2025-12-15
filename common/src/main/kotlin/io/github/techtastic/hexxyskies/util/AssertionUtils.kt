package io.github.techtastic.hexxyskies.util

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.mishaps.MishapEntityTooFarAway
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipTooFarAway
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.api.toMinecraft

object AssertionUtils {
    @Throws(MishapEntityTooFarAway::class)
    fun CastingEnvironment.assertShipInRange(ship: Ship) {
        // Check Seal
        if (!this.isVecInWorld(ship.transform.positionInWorld.toMinecraft())) {
            throw MishapShipTooFarAway(ship)
        }
        if (!this.isVecInRange(ship.transform.positionInWorld.toMinecraft())) {
            throw MishapShipTooFarAway(ship)
        }
    }
}