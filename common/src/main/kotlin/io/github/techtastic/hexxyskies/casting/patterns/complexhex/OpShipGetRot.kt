package io.github.techtastic.hexxyskies.casting.patterns.complexhex

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kineticcat.complexhex.api.casting.iota.QuaternionIota
import dev.kineticcat.complexhex.stuff.Quaternion
import io.github.techtastic.hexxyskies.util.AssertionUtils.assertShipInRange
import io.github.techtastic.hexxyskies.util.OperatorUtils.getShip

object OpShipGetRot : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): List<Iota> {
        val ship = args.getShip(env.world, 0, argc)
        env.assertShipInRange(ship)
        val q = ship.transform.shipToWorldRotation
        return listOf(QuaternionIota(Quaternion(q.w(), q.x(), q.y(), q.z())))
    }
}