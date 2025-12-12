package io.github.techtastic.hexxyskies.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
import io.github.techtastic.hexxyskies.HexxySkies
import io.github.techtastic.hexxyskies.forge.client.HexxySkiesForgeClient
import io.github.techtastic.hexxyskies.platform.RegistryUtils
import io.github.techtastic.hexxyskies.platform.forge.RegistryUtilsImpl
import thedarkcolour.kotlinforforge.KotlinModLoadingContext

@Mod(HexxySkies.MOD_ID)
object HexxySkiesForge {
    init {
        val modEventBus = KotlinModLoadingContext.get().getKEventBus()
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(HexxySkies.MOD_ID, modEventBus)

        if (FMLEnvironment.dist.isClient) {
            modEventBus.addListener(HexxySkiesForgeClient::clientInit)
        }

        RegistryUtilsImpl.register(modEventBus)

        // Run our common setup.
        HexxySkies.init();
    }
}
