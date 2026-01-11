package io.github.techtastic.hexxyskies.casting.patterns.moreiotas

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import io.github.techtastic.hexxyskies.util.AssertionUtils.assertShipInRange
import io.github.techtastic.hexxyskies.util.OperatorUtils.getLoadedShip
import org.jblas.DoubleMatrix
import org.joml.Matrix3dc
import org.joml.Matrix4dc
import org.valkyrienskies.core.api.util.GameTickOnly
import ram.talia.moreiotas.api.casting.iota.MatrixIota

class OpShipGetMatrix(private val type: Type) : ConstMediaAction {
    override val argc: Int
        get() = 1

    @OptIn(GameTickOnly::class)
    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): List<Iota> {
        val ship = args.getLoadedShip(env.world, 0, argc)
        env.assertShipInRange(ship)
        return listOf(MatrixIota(when (type) {
            Type.WORLD_TO_SHIP -> ship.worldToShip.toDoubleMatrix()
            Type.SHIP_TO_WORLD -> ship.shipToWorld.toDoubleMatrix()
            Type.MOMENT_OF_INERTIA_TENSOR -> ship.inertiaData.inertiaTensor.toDoubleMatrix()
        }))
    }

    private fun Matrix4dc.toDoubleMatrix(): DoubleMatrix = DoubleMatrix(4, 4, *this[DoubleArray(16)])
    private fun Matrix3dc.toDoubleMatrix(): DoubleMatrix = DoubleMatrix(3, 3, *this[DoubleArray(9)])

    enum class Type {
        WORLD_TO_SHIP,
        SHIP_TO_WORLD,
        MOMENT_OF_INERTIA_TENSOR
    }
}