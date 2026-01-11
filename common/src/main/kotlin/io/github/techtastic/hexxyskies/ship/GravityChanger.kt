package io.github.techtastic.hexxyskies.ship

import com.google.common.util.concurrent.AtomicDouble
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import org.joml.Vector3d
import org.valkyrienskies.core.api.VsBeta
import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.api.ships.ShipPhysicsListener
import org.valkyrienskies.core.api.util.PhysTickOnly
import org.valkyrienskies.core.api.world.PhysLevel
import org.valkyrienskies.mod.common.util.toJOMLD
import java.util.concurrent.atomic.AtomicInteger

class GravityChanger: ShipPhysicsListener {
    val direction = AtomicInteger()
    val strength = AtomicDouble(-1.0)
    val duration = AtomicInteger(0)

    @VsBeta
    @PhysTickOnly
    override fun physTick(physShip: PhysShip, physLevel: PhysLevel) {
        if (duration.get() > 0) {
            val oldGravity = physLevel.aerodynamicUtils.getAtmosphereForDimension(physLevel.dimension).third
            physShip.applyWorldForce(Vector3d(0.0, 1.0, 0.0).mul(oldGravity).mul(physShip.mass))
            physShip.applyWorldForce(Direction.entries[direction.get()].normal.toJOMLD().mul(strength.get()).mul(physShip.mass))
        }
    }

    fun change(direction: Direction, strength: Double) {
        this.direction.set(Direction.entries.indexOf(direction))
        this.strength.set(strength)
        this.duration.set(50)
    }

    fun tick() {
        if (this.duration.get() > 0)
            this.duration.decrementAndGet()
    }
}