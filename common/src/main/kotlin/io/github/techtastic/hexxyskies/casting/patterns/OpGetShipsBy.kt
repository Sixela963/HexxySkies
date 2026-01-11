package io.github.techtastic.hexxyskies.casting.patterns

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import io.github.techtastic.hexxyskies.casting.iota.ShipIota
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.valkyrienskies.core.api.ships.LoadedServerShip
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.mod.api.toMinecraft
import org.valkyrienskies.mod.common.getShipsIntersecting

object OpGetShipsBy : ConstMediaAction {
    override val argc: Int
        get() = 2

    @OptIn(GameTickOnly::class)
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getVec3(0, argc)
        val radius = args.getPositiveDouble(1, argc)
        env.assertVecInRange(pos)

        val aabb = AABB(pos.add(Vec3(-radius, -radius, -radius)), pos.add(Vec3(radius, radius, radius)))
        val entitiesGot = env.world.getShipsIntersecting(aabb)
            .filterIsInstance<LoadedServerShip>()
            .sortedBy { it.transform.positionInWorld.toMinecraft().distanceToSqr(pos) }
        return entitiesGot.map { ShipIota(it.id, it.slug) }.asActionResult
    }
}