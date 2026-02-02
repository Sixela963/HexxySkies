package io.github.techtastic.hexxyskies.util

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import dev.architectury.platform.Platform
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipTooFarAway
import io.github.techtastic.hexxyskies.interop.HexalInterop
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.api.getShipManagingBlock
import org.valkyrienskies.mod.api.toJOML
import org.valkyrienskies.mod.api.toMinecraft

object AssertionUtils {
    fun CastingEnvironment.assertShipInRange(ship: Ship) {
        if (!this.isVecInWorld(ship.transform.positionInWorld.toMinecraft())) {
            throw MishapShipTooFarAway(ship)
        }

        if (this is CircleCastEnv) {
            this.impetus?.blockPos?.let { pos ->
                this.world.getShipManagingBlock(pos)?.let {
                    if (it.id == ship.id) return
                }
            }
        }

        if (Platform.isModLoaded("hexal") && HexalInterop.isWispOnShip(this, ship))
            return

        if (!this.isVecInRange(ship.transform.positionInWorld.toMinecraft())) {
            throw MishapShipTooFarAway(ship)
        }
    }
}