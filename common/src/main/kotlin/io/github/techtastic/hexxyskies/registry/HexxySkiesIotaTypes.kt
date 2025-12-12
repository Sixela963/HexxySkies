package io.github.techtastic.hexxyskies.registry

import io.github.techtastic.hexxyskies.casting.iota.ShipIota
import io.github.techtastic.hexxyskies.platform.RegistryUtils

object HexxySkiesIotaTypes {
    val SHIP = RegistryUtils.registerIota("ship", ShipIota::TYPE)

    fun init() {}
}