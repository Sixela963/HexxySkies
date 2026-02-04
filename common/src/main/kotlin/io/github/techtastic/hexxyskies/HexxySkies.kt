package io.github.techtastic.hexxyskies

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.interop.HexInterop
import dev.architectury.event.events.common.TickEvent
import dev.architectury.platform.Platform
import io.github.techtastic.hexxyskies.casting.WispShipAmbit
import io.github.techtastic.hexxyskies.registry.HexxySkiesIotas
import io.github.techtastic.hexxyskies.registry.HexxySkiesPatterns
import io.github.techtastic.hexxyskies.ship.GravityChanger
import io.github.techtastic.hexxyskies.util.DelayedAssemblyHelper
import org.valkyrienskies.core.api.VsBeta
import org.valkyrienskies.core.api.attachment.getOrPutAttachment
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.mod.common.ValkyrienSkiesMod
import org.valkyrienskies.mod.common.vsCore
import vazkii.patchouli.api.PatchouliAPI

object HexxySkies {
    const val MOD_ID: String = "hexxyskies"

    @OptIn(VsBeta::class, GameTickOnly::class)
    fun init() {
        HexxySkiesIotas.register()
        HexxySkiesPatterns.register()

        if (Platform.isModLoaded("complexhex") || Platform.isModLoaded("moreiotas") || Platform.isModLoaded("hexal") || Platform.isModLoaded("hexodus") || Platform.isModLoaded("hexical"))
            PatchouliAPI.get().setConfigFlag(HexInterop.PATCHOULI_ANY_INTEROP_FLAG, true)

        CastingEnvironment.addCreateEventListener { env, _ -> if (Platform.isModLoaded("hexal")) env.addExtension(WispShipAmbit(env)) }

        TickEvent.SERVER_LEVEL_POST.register(DelayedAssemblyHelper::onTick)

        vsCore.registerAttachment(GravityChanger::class.java)
        ValkyrienSkiesMod.api.tickEndEvent.on { event -> event.world.loadedShips.forEach { ship ->
            ship.getOrPutAttachment { GravityChanger() }.tick()
        } }
    }
}
