package io.github.techtastic.hexxyskies.interop

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.api.getShipManagingBlock
import ram.talia.hexal.api.casting.eval.env.WispCastEnv

object HexalInterop {
    fun isWispOnShip(env: CastingEnvironment, ship: Ship): Boolean {
        if (env is WispCastEnv)
            env.world.getShipManagingBlock(env.wisp.position())?.let { return it == ship }
        return false
    }
}