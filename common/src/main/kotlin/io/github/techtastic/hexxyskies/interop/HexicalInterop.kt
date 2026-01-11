package io.github.techtastic.hexxyskies.interop

import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadEntity
import miyucomics.hexical.features.specklikes.mesh.MeshEntity
import net.minecraft.world.phys.Vec3

object HexicalInterop {
    fun List<Iota>.getMeshPoints(idx: Int, argc: Int): List<Vec3> {
        val mesh = this.getEntity(idx, argc)
        if (mesh !is MeshEntity) throw MishapBadEntity.of(mesh, "mesh")
        return mesh.getShape().map { it.vec3 }
    }
}