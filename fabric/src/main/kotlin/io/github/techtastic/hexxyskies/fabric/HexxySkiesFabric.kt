package io.github.techtastic.hexxyskies.fabric

import net.fabricmc.api.ModInitializer
import io.github.techtastic.hexxyskies.HexxySkies
import io.github.techtastic.hexxyskies.platform.fabric.RegistryUtilsImpl

/**
 * The fabric-side initializer for the mod. Used for fabric-platform-specific code.
 */
class HexxySkiesFabric : ModInitializer {
    override fun onInitialize() {
        RegistryUtilsImpl.register()

        // Put anything initialized on fabric-side here, such as platform-specific registries.
        HexxySkies.init()
    }
}
