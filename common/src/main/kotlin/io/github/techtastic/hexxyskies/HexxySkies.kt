package io.github.techtastic.hexxyskies

import io.github.techtastic.hexxyskies.registry.HexxySkiesIotaTypes
import io.github.techtastic.hexxyskies.registry.HexxySkiesPatterns

/**
 * The common static object that represents the mod. Referenced by both fabric and forge for initialization.
 */
object HexxySkies {
    const val MOD_ID = "hexxyskies"

    @JvmStatic
    fun init() {
        HexxySkiesIotaTypes.init()
        HexxySkiesPatterns.init()
    }
}
