package io.github.techtastic.hexxyskies.platform.forge

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.HexRegistries
import io.github.techtastic.hexxyskies.HexxySkies
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister

object RegistryUtilsImpl {
    private val IOTA_TYPES = DeferredRegister.create(HexRegistries.IOTA_TYPE, HexxySkies.MOD_ID)
    private val ACTIONS = DeferredRegister.create(HexRegistries.ACTION, HexxySkies.MOD_ID)

    @JvmStatic
    fun registerIota(name: String, supplier: Function0<IotaType<*>>) : Function0<IotaType<*>> {
        return IOTA_TYPES.register(name, supplier)::get
    }

    @JvmStatic
    fun registerPattern(name: String, actionSupplier: Function0<ActionRegistryEntry>) : Function0<ActionRegistryEntry> {
        return ACTIONS.register(name, actionSupplier)::get
    }

    fun register(bus: IEventBus) {
        IOTA_TYPES.register(bus)
        ACTIONS.register(bus)
    }
}