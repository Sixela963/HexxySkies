package io.github.techtastic.hexxyskies.casting.patterns.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.CircleCastEnv
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import io.github.techtastic.hexxyskies.util.AssertionUtils.assertShipInRange
import io.github.techtastic.hexxyskies.util.OperatorUtils
import io.github.techtastic.hexxyskies.util.OperatorUtils.getLoadedShip
import io.github.techtastic.hexxyskies.util.OperatorUtils.getShip
import net.minecraft.nbt.CompoundTag
import org.joml.Vector3d
import org.joml.Vector3dc
import org.valkyrienskies.core.api.ships.properties.ShipId
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.util.GameToPhysicsAdapter
import org.valkyrienskies.mod.common.util.toJOML
import org.valkyrienskies.mod.common.util.toMinecraft
import kotlin.math.pow
import kotlin.math.sqrt

class OpShipApply(val type: Type, val reference: Reference): SpellAction {
    override val argc: Int
        get() = when (type) {
            Type.FORCE -> 3
            Type.TORQUE -> 2
        }

    @OptIn(GameTickOnly::class)
    override fun executeWithUserdata(
        args: List<Iota>,
        env: CastingEnvironment,
        userData: CompoundTag
    ): SpellAction.Result {
        println("Type: ${type.name}\nReference: ${reference.name}\nArguments: ${args.map { it.display().string }}")
        val target = args.getLoadedShip(env.world, 0, argc)
        val motion = args.getVec3(1, argc)
        val pos = if (type == Type.FORCE) args.getVec3(2, argc) else null
        val gtpa = ValkyrienSkiesMod.getOrCreateGTPA(env.world.dimensionId)
        env.assertShipInRange(target)
        //if (reference != Reference.BODY)
        //    pos?.let(env::assertVecInRange)

        //Spell cost function: calibrated to cost 10 dust to accelerate a 1000 kg block at 10 m/s in one cast
        //The idea is that for a given ship, doubling the speed costs quadruple (like impulse)
        //but the cost doesn't grow too fast with larger ships
        val motionForCost = 0.1+ 10*((motion.length() / (10*target.inertiaData.mass*60)).pow(2))* sqrt(target.inertiaData.mass/1000)
        //Detailed explanation:
        //0.1 is the base cost
        //The rest of the formula has 3 parts:
        //10 is simple the base cost when accelerating a 1000 kg block at 10 m/s in one cast: when doing that,the other two parts should equal one
        //(motion/(mass*60) is the velocity change from the spell: we divide by 10 to the part is equal to 1 when the change is 10 m/s. The 60 is because the spell is in m/s/kg /s, over a period of 1/60th of a second
        //sqrt(mass/1000) is the mass multiplier: to allow for larger ships, we only multiply by the sqrt mass. Divided by 1000 to have 1 when applying to a single 1000kg block.

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
                Reference.MODEL -> gtpa.applyModelForce(target, motion, pos)
                Reference.BODY -> gtpa.applyBodyForce(target, motion, pos)
            }
        }
    }

    private data class TorqueSpell(val gtpa: GameToPhysicsAdapter, val reference: Reference, val target: ShipId, val motion: Vector3dc) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            when (reference) {
                Reference.WORLD -> gtpa.applyWorldTorque(target, motion)
                Reference.MODEL -> gtpa.applyModelTorque(target, motion)
                Reference.BODY -> gtpa.applyBodyTorque(target, motion)
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