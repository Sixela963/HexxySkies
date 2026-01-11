package io.github.techtastic.hexxyskies.casting.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import io.github.techtastic.hexxyskies.util.AssertionUtils.assertShipInRange
import io.github.techtastic.hexxyskies.util.OperatorUtils.getShip
import net.minecraft.world.phys.Vec3

object OpShipGetAABB : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): List<Iota> {
        val ship = args.getShip(env.world, 0, argc)
        env.assertShipInRange(ship)
        return ship.shipAABB?.let {
            listOf(
                Vec3Iota(Vec3(
                    it.minX().toDouble(),
                    it.minY().toDouble(),
                    it.minZ().toDouble()
                )),
                Vec3Iota(Vec3(
                    it.maxX().toDouble(),
                    it.maxY().toDouble(),
                    it.maxZ().toDouble()
                ))
            )
        } ?: listOf()
    }
}