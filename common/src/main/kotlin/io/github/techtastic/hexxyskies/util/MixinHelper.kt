package io.github.techtastic.hexxyskies.util

import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import io.github.techtastic.hexxyskies.casting.iota.ShipIota
import org.valkyrienskies.core.api.ships.Ship

object MixinHelper {
    fun List<Iota>.getShipOrEntityIota(idx: Int, argc: Int = 0) : Iota {
        val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
        if (x is EntityIota || x is ShipIota) {
            return x
        } else {
            throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "entity.ship")
        }
    }

    fun Ship.getHeight(): Double = this.shipAABB?.let { (it.maxY() - it.minY()).toDouble() } ?: 0.0
}