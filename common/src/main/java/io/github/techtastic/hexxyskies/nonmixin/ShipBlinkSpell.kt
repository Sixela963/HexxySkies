package io.github.techtastic.hexxyskies.nonmixin

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.utils.getOrCreateCompound
import at.petrak.hexcasting.api.utils.putCompound
import io.github.techtastic.hexxyskies.HexxySkies
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import org.joml.Vector3d
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.core.impl.game.ShipTeleportDataImpl
import org.valkyrienskies.mod.api.toJOML
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.shipObjectWorld

@JvmRecord
data class ShipBlinkSpell(val level: ServerLevel, val target: ServerShip, val delta: Double) : RenderedSpell {
    @OptIn(GameTickOnly::class)
    override fun cast(env: CastingEnvironment) {
        ValkyrienSkiesMod.vsCore.teleportShip(level.shipObjectWorld, target, ShipTeleportDataImpl(getEndPosition(target, delta)))
    }

    companion object {
        fun getEndPosition(ship: Ship, delta: Double): Vector3d =
            ship.transform.positionInWorld.add(Vector3d(0.0,0.0,1.0).mul(delta), Vector3d())
    }
}