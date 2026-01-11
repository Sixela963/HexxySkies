package io.github.techtastic.hexxyskies.interop

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import gravity_changer.GravityComponent
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipNotLoaded
import io.github.techtastic.hexxyskies.ship.GravityChanger
import io.github.techtastic.hexxyskies.util.OperatorUtils.getShipOrEntity
import miyucomics.hexodus.HexodusComponent
import net.minecraft.core.Direction
import org.valkyrienskies.core.api.VsBeta
import org.valkyrienskies.core.api.attachment.getOrPutAttachment
import org.valkyrienskies.core.api.ships.*
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.getLoadedShipManagingPos
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.mod.common.util.toMinecraft
import kotlin.jvm.optionals.getOrNull
import kotlin.math.abs

object HexodusInterop {
    @OptIn(GameTickOnly::class)
    fun changeShipGravity(args: List<Iota>, env: CastingEnvironment, argc: Int): SpellAction.Result? {
        val target = args.getShipOrEntity(env.world, 0, argc)

        val ship = target.left().getOrNull() ?: return null

        val serverShip = env.world.getLoadedShipManagingPos(ship.transform.positionInShip) ?: throw MishapShipNotLoaded()
        val offset = args.getBlockPos(1, argc)
        val direction = Direction.getNearest(offset.x.toDouble(), offset.y.toDouble(), offset.z.toDouble()) ?: throw MishapInvalidIota.of(args[1], 1, "axis_vector")
        val strength = args.getPositiveDoubleUnderInclusive(2, 5.0, argc)
        return SpellAction.Result(
            ShipGravitateSpell(serverShip, direction, strength),
            MediaConstants.CRYSTAL_UNIT,
            listOf(ParticleSpray.burst(ship.transform.positionInWorld.toMinecraft(), 10.0))
        )
    }

    @OptIn(GameTickOnly::class, VsBeta::class)
    fun getShipGravity(args: List<Iota>, env: CastingEnvironment, argc: Int, operate: (HexodusComponent) -> List<Iota>): List<Iota>? {
        val target = args.getShipOrEntity(env.world, 0, argc)

        val ship = target.left().getOrNull() ?: return null

        val serverShip = env.world.getLoadedShipManagingPos(ship.transform.positionInShip) ?: throw MishapShipNotLoaded()
        val changer = serverShip.getOrPutAttachment { GravityChanger() }
        return operate(HexodusComponent(Direction.entries[changer.direction.get()], changer.strength.get(), changer.duration.get()))
    }

    @OptIn(GameTickOnly::class, VsBeta::class)
    fun getTrueShipGravity(args: List<Iota>, env: CastingEnvironment, argc: Int, operate: (GravityComponent) -> List<Iota>): List<Iota>? {
        val target = args.getShipOrEntity(env.world, 0, argc)

        val ship = target.left().getOrNull() ?: return null

        val comp = GravityComponent(env.castingEntity)
        val gravity = env.world.shipObjectWorld.aerodynamicUtils.getAtmosphereForDimension(env.world.dimensionId).third
        val clazz = GravityComponent::class.java
        val directionField = clazz.getDeclaredField("currGravityDirection")
        directionField.isAccessible = true
        directionField.set(comp, if (gravity == abs(gravity)) Direction.DOWN else Direction.UP)
        val strengthField = clazz.getDeclaredField("currGravityStrength")
        strengthField.isAccessible = true
        strengthField.set(comp, abs(gravity))
        return operate(comp)
    }

    @OptIn(GameTickOnly::class)
    private data class ShipGravitateSpell(val ship: LoadedServerShip, val direction: Direction, val strength: Double) : RenderedSpell {
        @OptIn(VsBeta::class)
        override fun cast(env: CastingEnvironment) {
            ship.getOrPutAttachment { GravityChanger() }.change(direction, strength)
        }
    }
}