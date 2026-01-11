package io.github.techtastic.hexxyskies.casting.patterns.hexal

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.misc.MediaConstants
import io.github.techtastic.hexxyskies.util.AssertionUtils.assertShipInRange
import io.github.techtastic.hexxyskies.util.OperatorUtils.getShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.mod.common.entity.handling.DefaultShipyardEntityHandler
import ram.talia.hexal.api.casting.eval.env.WispCastEnv
import ram.talia.hexal.common.entities.BaseCastingWisp

class OpShipyardWisp(val toShipyard: Boolean) : SpellAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val ship = args.getShip(env.world, 0, argc)
        env.assertShipInRange(ship)

        if (env !is WispCastEnv) throw MishapBadCaster()

        return SpellAction.Result(
            Spell(env.wisp, ship, toShipyard),
            0,
            listOf(ParticleSpray.burst(env.wisp.eyePosition, .75))
        )
    }

    private data class Spell(val wisp: BaseCastingWisp, val ship: Ship, val toShipyard: Boolean) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            if (toShipyard)
                DefaultShipyardEntityHandler.moveEntityFromWorldToShipyard(wisp, ship)
            else
                DefaultShipyardEntityHandler.entityRemovedFromShipyard(wisp, ship)
        }
    }
}