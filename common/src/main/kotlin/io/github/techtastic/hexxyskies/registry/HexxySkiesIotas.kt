package io.github.techtastic.hexxyskies.registry

import at.petrak.hexcasting.api.casting.iota.IotaType
import at.petrak.hexcasting.xplat.IXplatAbstractions
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexxyskies.HexxySkies.MOD_ID
import io.github.techtastic.hexxyskies.casting.iota.ShipIota
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

object HexxySkiesIotas {
    private val IOTA_TYPES = DeferredRegister.create(MOD_ID, IXplatAbstractions.INSTANCE.iotaTypeRegistry.key() as ResourceKey<Registry<IotaType<*>>>)

    val SHIP = IOTA_TYPES.register("ship", ShipIota::TYPE)

    fun register() {
        IOTA_TYPES.register()
    }
}