package io.github.techtastic.hexxyskies.util

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.core.util.pollUntilEmpty
import org.valkyrienskies.mod.common.assembly.ShipAssembler
import java.util.concurrent.ConcurrentLinkedQueue

object DelayedAssemblyHelper {
    private val toAssemble = ConcurrentLinkedQueue<Set<BlockPos>>()

    fun addNew(positions: Set<BlockPos>) {
        this.toAssemble.add(positions)
    }

    fun onTick(level: ServerLevel) {
        this.toAssemble.pollUntilEmpty {
            ShipAssembler.assembleToShip(level, it.toList(), true)
        }
    }
}