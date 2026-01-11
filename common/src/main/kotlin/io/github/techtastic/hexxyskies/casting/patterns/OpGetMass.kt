package io.github.techtastic.hexxyskies.casting.patterns

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Iota
import io.github.techtastic.hexxyskies.casting.iota.ShipIota
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipNotLoaded
import io.github.techtastic.hexxyskies.util.AssertionUtils.assertShipInRange
import org.valkyrienskies.core.api.ships.LoadedServerShip
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.mod.common.BlockStateInfo

object OpGetMass : ConstMediaAction {
    override val argc: Int
        get() = 1

    @OptIn(GameTickOnly::class)
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val i = args[0]
        val mass = if (i is ShipIota)
                (i.getShip(env.world) as? LoadedServerShip)?.let {
                    env.assertShipInRange(it)
                    it.inertiaData.mass
                } ?: throw MishapShipNotLoaded()
        else {
            val pos = args.getBlockPos(0, argc)
            env.assertPosInRange(pos)
            val state = env.world.getBlockState(pos)
            BlockStateInfo.get(state)?.first ?: 0.0
        }
        return listOf(DoubleIota(mass))
    }
}