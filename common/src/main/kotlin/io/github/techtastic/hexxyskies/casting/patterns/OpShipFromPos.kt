package io.github.techtastic.hexxyskies.casting.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import io.github.techtastic.hexxyskies.casting.iota.ShipIota
import io.github.techtastic.hexxyskies.casting.mishaps.MishapNotOnShip
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.mod.common.getLoadedShipManagingPos
import org.valkyrienskies.mod.common.util.toJOMLD

object OpShipFromPos : ConstMediaAction {
    override val argc: Int
        get() = 1

    @OptIn(GameTickOnly::class)
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getBlockPos(0, argc)
        env.assertPosInRange(pos)

        return env.world.getLoadedShipManagingPos(pos.toJOMLD())
            ?.let { ShipIota(it.id, it.slug) }?.let { listOf(it) } ?: throw MishapNotOnShip(pos.center)
    }
}