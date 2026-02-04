package io.github.techtastic.hexxyskies.casting

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3
import org.valkyrienskies.mod.compat.hexcasting.ShipAmbit
import ram.talia.hexal.api.casting.eval.env.WispCastEnv

class WispShipAmbit(env: CastingEnvironment) : ShipAmbit(env) {
    override fun onHasEditPermissionsAt(pos: BlockPos, current: Boolean): Boolean {
        if (env !is WispCastEnv) return current
        return super.onHasEditPermissionsAt(pos, current)
    }

    override fun onIsVecInRange(vec: Vec3, current: Boolean): Boolean {
        if (env !is WispCastEnv) return current
        return super.onIsVecInRange(vec, current)
    }

    override fun getCasterPosition(): Vec3? {
        val env = env
        if (env is WispCastEnv)
            return env.wisp.position()
        return null
    }
}