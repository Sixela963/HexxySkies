package io.github.techtastic.hexxyskies.platform.fabric

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.common.lib.HexRegistries
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexxyskies.HexxySkies

object RegistryUtilsImpl {
    private val IOTA_TYPES = DeferredRegister.create(HexxySkies.MOD_ID, HexRegistries.IOTA_TYPE)
    private val ACTIONS = DeferredRegister.create(HexxySkies.MOD_ID, HexRegistries.ACTION)

    @JvmStatic
    fun registerIota(name: String, supplier: Function0<IotaType<*>>) : Function0<IotaType<*>> {
        return IOTA_TYPES.register(name, supplier)::get
    }

    @JvmStatic
    fun registerPattern(name: String, actionSupplier: Function0<ActionRegistryEntry>) : Function0<ActionRegistryEntry> {
        return ACTIONS.register(name, actionSupplier)::get
    }

    fun register() {
        IOTA_TYPES.register()
        ACTIONS.register()
    }
}