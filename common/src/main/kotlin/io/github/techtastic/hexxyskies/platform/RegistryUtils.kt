package io.github.techtastic.hexxyskies.platform

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.iota.IotaType
import dev.architectury.injectables.annotations.ExpectPlatform

object RegistryUtils {
    @JvmStatic
    @ExpectPlatform
    fun registerIota(name: String, supplier: Function0<IotaType<*>>) : Function0<IotaType<*>> {
        throw AssertionError()
    }

    @JvmStatic
    @ExpectPlatform
    fun registerPattern(name: String, actionSupplier: Function0<ActionRegistryEntry>) : Function0<ActionRegistryEntry> {
        throw AssertionError()
    }
}