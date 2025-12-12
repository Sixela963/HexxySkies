package io.github.techtastic.hexxyskies.casting.iota

import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.IotaType
import net.minecraft.ChatFormatting
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.core.api.ships.properties.ShipId
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.mod.api.getShipById

class ShipIota(val shipId: ShipId, val slug: String?) : Iota(TYPE, shipId) {
    fun getShip(level: ServerLevel) = level.getShipById(shipId)

    override fun isTruthy(): Boolean = true

    @OptIn(GameTickOnly::class)
    override fun toleratesOther(that: Iota?): Boolean = (that as? ShipIota)?.let { that.shipId == this.shipId } ?: false

    @OptIn(GameTickOnly::class)
    override fun serialize(): Tag {
        val tag = CompoundTag()
        tag.putLong("hexxyskies\$shipId", shipId)
        slug?.let { tag.putString("hexxyskies\$slug", it) }
        return tag
    }

    companion object {
        @OptIn(GameTickOnly::class)
        val TYPE = object : IotaType<ShipIota>() {
            override fun deserialize(
                tag: Tag,
                world: ServerLevel?
            ): ShipIota? {
                val tag = tag as CompoundTag
                val shipId = tag.getLong("hexxyskies\$shipId")
                return world?.let { level -> level.getShipById(shipId)?.let { ship -> ShipIota(ship.id, ship.slug) } }
                    ?: ShipIota(shipId, if (tag.contains("hexxyskies\$slug")) tag.getString("hexxyskies\$slug") else null)
            }

            override fun display(tag: Tag): Component = Component
                .translatable("hexxyskies.iota.ship", (tag as CompoundTag).getString("hexxyskies\$slug"))
                .withStyle(ChatFormatting.GOLD)

            override fun color(): Int = 0xcda638
        }
    }
}