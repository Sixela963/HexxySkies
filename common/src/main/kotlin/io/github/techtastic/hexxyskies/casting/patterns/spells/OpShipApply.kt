package io.github.techtastic.hexxyskies.casting.patterns.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import io.github.techtastic.hexxyskies.util.AssertionUtils.assertShipInRange
import io.github.techtastic.hexxyskies.util.OperatorUtils
import io.github.techtastic.hexxyskies.util.OperatorUtils.getShip
import net.minecraft.nbt.CompoundTag
import org.joml.Vector3dc
import org.valkyrienskies.core.api.ships.properties.ShipId
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.util.GameToPhysicsAdapter
import org.valkyrienskies.mod.common.util.toJOML
import org.valkyrienskies.mod.common.util.toMinecraft

class OpShipApply(val type: Type, val reference: Reference): SpellAction {
    override val argc: Int
        get() = when (type) {
            Type.FORCE -> 3
            Type.TORQUE -> 2
        }

    override fun executeWithUserdata(
        args: List<Iota>,
        env: CastingEnvironment,
        userData: CompoundTag
    ): SpellAction.Result {
        val target = args.getShip(env.world, 0, argc)
        val motion = args.getVec3(1, argc)
        val pos = if (type == Type.FORCE) args.getVec3(2, argc) else null
        val gtpa = ValkyrienSkiesMod.getOrCreateGTPA(env.world.dimensionId)
        env.assertShipInRange(target)
        pos?.let(env::assertVecInRange)

        var motionForCost = motion.lengthSqr()
        if (OperatorUtils.checkAndMarkGivenForces(userData, target))
            motionForCost++
        return SpellAction.Result(
            when (type) {
                Type.FORCE -> ForceSpell(gtpa, reference, target.id, motion.toJOML(), pos!!.toJOML())
                Type.TORQUE -> TorqueSpell(gtpa, reference, target.id, motion.toJOML())
            },
            (motionForCost * MediaConstants.DUST_UNIT).toLong(),
            listOf(
                ParticleSpray(
                    target.transform.positionInWorld.toMinecraft(),
                    motion.normalize(),
                    0.0,
                    0.1
                )
            ),
        )
    }

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        throw IllegalStateException()
    }

    private data class ForceSpell(val gtpa: GameToPhysicsAdapter, val reference: Reference, val target: ShipId, val motion: Vector3dc, val pos: Vector3dc) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            when (reference) {
                Reference.WORLD -> gtpa.applyWorldForce(target, motion, pos)
                Reference.BODY -> gtpa.applyBodyForce(target, motion, pos)
                Reference.MODEL -> gtpa.applyModelForce(target, motion, pos)
            }
        }
    }

    private data class TorqueSpell(val gtpa: GameToPhysicsAdapter, val reference: Reference, val target: ShipId, val motion: Vector3dc) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            when (reference) {
                Reference.WORLD -> gtpa.applyWorldTorque(target, motion)
                Reference.BODY -> gtpa.applyBodyTorque(target, motion)
                Reference.MODEL -> gtpa.applyModelTorque(target, motion)
            }
        }
    }

    enum class Type {
        FORCE,
        TORQUE
    }

    enum class Reference {
        WORLD,
        BODY,
        MODEL
    }
}