package io.github.techtastic.hexxyskies.fabric

import io.github.techtastic.hexxyskies.HexxySkies
import net.fabricmc.api.ModInitializer

class HexxySkiesFabric : ModInitializer {
    override fun onInitialize() {
        HexxySkies.init()
    }
}
