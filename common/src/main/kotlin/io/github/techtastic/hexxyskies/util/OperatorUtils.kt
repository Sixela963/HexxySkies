package io.github.techtastic.hexxyskies.util

import at.petrak.hexcasting.api.HexAPI
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.EntityIota
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.ListIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.casting.mishaps.MishapNotEnoughArgs
import at.petrak.hexcasting.api.utils.getOrCreateCompound
import at.petrak.hexcasting.api.utils.putCompound
import com.mojang.datafixers.util.Either
import dev.architectury.platform.Platform
import io.github.techtastic.hexxyskies.casting.iota.ShipIota
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipNotLoaded
import io.github.techtastic.hexxyskies.interop.HexicalInterop.getMeshPoints
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import org.valkyrienskies.core.api.ships.LoadedServerShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.mod.common.getLoadedShipManagingPos

object OperatorUtils {
    fun List<Iota>.getShip(level: ServerLevel, idx: Int, argc: Int = 0) : Ship {
        val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
        if (x is ShipIota) {
            return x.getShip(level) ?: throw MishapShipNotLoaded()
        } else {
            throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "ship")
        }
    }

    @OptIn(GameTickOnly::class)
    fun List<Iota>.getLoadedShip(level: ServerLevel, idx: Int, argc: Int = 0) : LoadedServerShip {
        val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
        if (x is ShipIota) {
            return x.getShip(level)?.transform?.positionInShip?.let { level.getLoadedShipManagingPos(it) } ?: throw MishapShipNotLoaded()
        } else {
            throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "ship")
        }
    }

    fun List<Iota>.getShipOrEntity(level: ServerLevel, idx: Int, argc: Int = 0) : Either<Ship, Entity> {
        val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
        if (x is ShipIota) {
            return x.getShip(level)?.let { Either.left(it) } ?: throw MishapShipNotLoaded()
        } else if (x is EntityIota)
            return Either.right(this.getEntity(idx, argc))
        else {
            throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "entity.ship")
        }
    }

    fun List<Iota>.getListOfVecs(idx: Int, argc: Int) : List<Vec3> {
        val x = this.getOrElse(idx) { throw MishapNotEnoughArgs(idx + 1, this.size) }
        if (x is ListIota) {
            return x.list.map {
                if (it !is Vec3Iota) throw MishapInvalidIota.ofType(this[0], 0, "list.vec")
                it.vec3
            }
        } else if (x is EntityIota && Platform.isModLoaded("hexical")) {
            return this.getMeshPoints(idx, argc)
        } else {
            throw MishapInvalidIota.ofType(x, if (argc == 0) idx else argc - (idx + 1), "list.vec")
        }
    }

    @JvmStatic
    fun checkAndMarkGivenForces(userData: CompoundTag, ship: Ship): Boolean {
        val marked = userData.getOrCreateCompound(HexAPI.MARKED_MOVED_USERDATA)
        val name = "Ship#${ship.id}"
        return if (marked.contains(name)) {
            true
        } else {
            marked.putBoolean(name, true)
            userData.putCompound(HexAPI.MARKED_MOVED_USERDATA, marked)
            false
        }
    }
}