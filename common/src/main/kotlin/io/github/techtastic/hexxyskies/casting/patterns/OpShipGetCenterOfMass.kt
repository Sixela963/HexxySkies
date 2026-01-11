package io.github.techtastic.hexxyskies.casting.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import io.github.techtastic.hexxyskies.util.AssertionUtils.assertShipInRange
import io.github.techtastic.hexxyskies.util.OperatorUtils.getShip
import org.valkyrienskies.mod.api.toMinecraft

class OpShipGetCenterOfMass(private val type: Type) : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): List<Iota> {
        val ship = args.getShip(env.world, 0, argc)
        env.assertShipInRange(ship)
        return listOf(Vec3Iota((when (type) {
            Type.WORLD -> ship.transform.positionInWorld
            Type.MODEL -> ship.transform.positionInModel
        }).toMinecraft()))
    }

    enum class Type {
        WORLD,
        MODEL
    }
}