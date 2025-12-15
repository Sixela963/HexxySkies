package io.github.techtastic.hexxyskies.nonmixin

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.utils.getOrCreateCompound
import at.petrak.hexcasting.api.utils.putCompound
import io.github.techtastic.hexxyskies.HexxySkies
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.phys.Vec3
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.api.toJOML
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.dimensionId

@JvmRecord
data class ShipAddMotionSpell(val target: Ship, val motion: Vec3) : RenderedSpell {
    override fun cast(env: CastingEnvironment) {
        ValkyrienSkiesMod.getOrCreateGTPA(env.world.dimensionId).applyWorldForceToBodyPos(target.id, motion.toJOML())
    }

    companion object {
        val MARKED_MOVED_SHIP_USERDATA: String = ResourceLocation(HexxySkies.MOD_ID, "impulsed").toString()

        @JvmStatic
        fun checkAndMarkGivenMotion(userData: CompoundTag, ship: Ship): Boolean {
            val marked = userData.getOrCreateCompound(MARKED_MOVED_SHIP_USERDATA)
            return if (marked.contains(ship.id.toString())) {
                true
            } else {
                marked.putBoolean(ship.id.toString(), true)
                userData.putCompound(MARKED_MOVED_SHIP_USERDATA, marked)
                false
            }
        }
    }
}