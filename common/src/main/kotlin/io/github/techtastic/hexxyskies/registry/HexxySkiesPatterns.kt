package io.github.techtastic.hexxyskies.registry

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import io.github.techtastic.hexxyskies.casting.patterns.OpShipFromPos
import io.github.techtastic.hexxyskies.platform.RegistryUtils

object HexxySkiesPatterns {
    val SHIP_FROM_POS = RegistryUtils.registerPattern("ship_from_pos") { ActionRegistryEntry(
        HexPattern.fromAngles("wawwwa", HexDir.EAST),
        OpShipFromPos
    ) }

    fun init() {}
}