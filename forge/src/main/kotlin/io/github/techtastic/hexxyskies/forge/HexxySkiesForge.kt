package io.github.techtastic.hexxyskies.forge

import dev.architectury.platform.forge.EventBuses
import io.github.techtastic.hexxyskies.HexxySkies
import net.minecraftforge.fml.common.Mod
import thedarkcolour.kotlinforforge.KotlinModLoadingContext

@Mod(HexxySkies.MOD_ID)
class HexxySkiesForge {
    init {
        EventBuses.registerModEventBus(HexxySkies.MOD_ID, KotlinModLoadingContext.get().getKEventBus())
        HexxySkies.init()
    }
}
