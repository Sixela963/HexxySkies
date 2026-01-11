package io.github.techtastic.hexxyskies.casting.patterns.spells

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDoubleBetween
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import io.github.techtastic.hexxyskies.util.AssertionUtils.assertShipInRange
import io.github.techtastic.hexxyskies.util.OperatorUtils.getShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.mod.common.util.toMinecraft

object OpShipSetScale: SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val ship = args.getShip(env.world, 0, argc)
        val scale = args.getDoubleBetween(1, 1/16.0, 16.0)
        env.assertShipInRange(ship)

        return SpellAction.Result(
            Spell(ship as ServerShip, scale),
            (MediaConstants.SHARD_UNIT * Math.abs(ship.transform.shipToWorldScaling.x() - scale)).toLong(),
            listOf(ParticleSpray.burst(ship.transform.positionInWorld.toMinecraft(), scale))
        )
    }

    @OptIn(GameTickOnly::class)
    private data class Spell(val ship: ServerShip, val scale: Double) : RenderedSpell {
        @OptIn(GameTickOnly::class)
        override fun cast(env: CastingEnvironment) {
            ValkyrienSkiesMod.vsCore.scaleShip(env.world.shipObjectWorld, ship, scale)
        }
    }
}