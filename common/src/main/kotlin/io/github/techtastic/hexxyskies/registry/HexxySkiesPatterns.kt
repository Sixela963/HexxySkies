package io.github.techtastic.hexxyskies.registry

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.xplat.IXplatAbstractions
import dev.architectury.platform.Platform
import dev.architectury.registry.registries.DeferredRegister
import io.github.techtastic.hexxyskies.HexxySkies.MOD_ID
import io.github.techtastic.hexxyskies.casting.patterns.*
import io.github.techtastic.hexxyskies.casting.patterns.complexhex.OpShipGetId
import io.github.techtastic.hexxyskies.casting.patterns.complexhex.OpShipGetRot
import io.github.techtastic.hexxyskies.casting.patterns.hexal.OpShipyardWisp
import io.github.techtastic.hexxyskies.casting.patterns.moreiotas.OpShipGetMatrix
import io.github.techtastic.hexxyskies.casting.patterns.moreiotas.OpShipGetSlug
import io.github.techtastic.hexxyskies.casting.patterns.spells.OpAssemble
import io.github.techtastic.hexxyskies.casting.patterns.spells.OpShipApply
import io.github.techtastic.hexxyskies.casting.patterns.spells.OpShipSetScale
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

object HexxySkiesPatterns {
    private val ACTIONS = DeferredRegister.create(MOD_ID, IXplatAbstractions.INSTANCE.actionRegistry.key() as ResourceKey<Registry<ActionRegistryEntry>>)

    val SHIP_FROM_POS = ACTIONS.register("pos") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedw", HexDir.EAST),
        OpShipFromPos
    ) }

    val ZONE_SHIP = ACTIONS.register("zone_ship") { ActionRegistryEntry(
        HexPattern.fromAngles("qqqqqwdeddwwwaqww", HexDir.SOUTH_EAST),
        OpGetShipsBy
    ) }

    val COM_WORLD = ACTIONS.register("com/world") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedwqqaq", HexDir.EAST),
        OpShipGetCenterOfMass(OpShipGetCenterOfMass.Type.WORLD)
    ) }

    val COM_MODEL = ACTIONS.register("com/model") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedwdqaq", HexDir.EAST),
        OpShipGetCenterOfMass(OpShipGetCenterOfMass.Type.MODEL)
    ) }

    val MASS = ACTIONS.register("mass") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedweeaa", HexDir.EAST),
        OpGetMass
    ) }

    val ROT_EULER = ACTIONS.register("rot/euler") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedwqwa", HexDir.EAST),
        OpShipGetRotEuler
    ) }

    val GET_SCALE = ACTIONS.register("scale/get") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewed", HexDir.EAST),
        OpShipGetScale
    ) }

    val LINEAR_VELOCITY = ACTIONS.register("velocity/linear") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedwqwq", HexDir.EAST),
        OpShipGetVelocity(true)
    ) }

    val ANGULAR_VELOCITY = ACTIONS.register("velocity/angular") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedwawe", HexDir.EAST),
        OpShipGetVelocity(false)
    ) }

    val AABB = ACTIONS.register("aabb") { ActionRegistryEntry(
        HexPattern.fromAngles("dewwedaeadewwedae", HexDir.EAST),
        OpShipGetAABB
    ) }

    val ASSEMBLE = ACTIONS.register("assemble") { ActionRegistryEntry(
        HexPattern.fromAngles("wewewewewewqdwwdwqqwdwwdwqqwdwwdwqq", HexDir.EAST),
        OpAssemble
    ) }

    val SET_SCALE = ACTIONS.register("scale/set") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedwadwwd", HexDir.EAST),
        OpShipSetScale
    ) }

    val APPLY_WORLD_FORCE = ACTIONS.register("force/world") { ActionRegistryEntry(
        HexPattern.fromAngles("qwwqawwwweqwaeawqaw", HexDir.SOUTH_EAST),
        OpShipApply(OpShipApply.Type.FORCE, OpShipApply.Reference.WORLD)
    ) }

    val APPLY_WORLD_TORQUE = ACTIONS.register("torque/world") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedwqqqdaqqqa", HexDir.EAST),
        OpShipApply(OpShipApply.Type.TORQUE, OpShipApply.Reference.WORLD)
    ) }

    val APPLY_BODY_FORCE = ACTIONS.register("force/body") { ActionRegistryEntry(
        HexPattern.fromAngles("qwwqawwweqwaeawqaw", HexDir.SOUTH_EAST),
        OpShipApply(OpShipApply.Type.FORCE, OpShipApply.Reference.BODY)
    ) }

    val APPLY_BODY_TORQUE = ACTIONS.register("torque/body") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedwqqqadeeed", HexDir.EAST),
        OpShipApply(OpShipApply.Type.TORQUE, OpShipApply.Reference.BODY)
    ) }

    val APPLY_MODEL_FORCE = ACTIONS.register("force/model") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedwaqwaea", HexDir.EAST),
        OpShipApply(OpShipApply.Type.FORCE, OpShipApply.Reference.MODEL)
    ) }

    val APPLY_MODEL_TORQUE = ACTIONS.register("torque/model") { ActionRegistryEntry(
        HexPattern.fromAngles("wdewwedweweedaqqqa", HexDir.EAST),
        OpShipApply(OpShipApply.Type.TORQUE, OpShipApply.Reference.MODEL)
    ) }

    // ComplexHex
    val ID = if (!Platform.isModLoaded("complexhex")) null else
        ACTIONS.register("id") { ActionRegistryEntry(
            HexPattern.fromAngles("wqawwwaqqwdedwaq", HexDir.EAST),
            OpShipGetId
        ) }

    val ROT_QUATERNION = if (!Platform.isModLoaded("complexhex")) null else
        ACTIONS.register("rot/quaternion") { ActionRegistryEntry(
            HexPattern.fromAngles("waqqqqqeqwdewwedw", HexDir.SOUTH_EAST),
            OpShipGetRot
        ) }

    // HexStruction
    // TODO: Fix crash
    /*val SHIP_FROM_STRUCTURE = if (!Platform.isModLoaded("hexstruction")) null else
        ACTIONS.register("ship/structure") { ActionRegistryEntry(
            HexPattern.fromAngles("aeqdqawwwaqdqea", HexDir.EAST),
            OpStructureToShip
        ) }*/

    // MoreIotas
    val WORLD_SHIP_MATRIX = if (!Platform.isModLoaded("moreiotas")) null else
        ACTIONS.register("matrix/world_ship") { ActionRegistryEntry(
            HexPattern.fromAngles("wdwaedewwedww", HexDir.NORTH_EAST),
            OpShipGetMatrix(OpShipGetMatrix.Type.WORLD_TO_SHIP)
        ) }

    val SHIP_WORLD_MATRIX = if (!Platform.isModLoaded("moreiotas")) null else
        ACTIONS.register("matrix/ship_world") { ActionRegistryEntry(
            HexPattern.fromAngles("awdwdwwedwwwd", HexDir.SOUTH_EAST),
            OpShipGetMatrix(OpShipGetMatrix.Type.SHIP_TO_WORLD)
        ) }

    val MOMENT_OF_INERTIA_TENSOR = if (!Platform.isModLoaded("moreiotas")) null else
        ACTIONS.register("matrix/moment_of_inertia_tensor") { ActionRegistryEntry(
            HexPattern.fromAngles("ewwedwwwqdawdw", HexDir.SOUTH_WEST),
            OpShipGetMatrix(OpShipGetMatrix.Type.MOMENT_OF_INERTIA_TENSOR)
        ) }

    val SLUG = if (!Platform.isModLoaded("moreiotas")) null else
        ACTIONS.register("slug") { ActionRegistryEntry(
            HexPattern.fromAngles("wdewwedwqeqawqa", HexDir.EAST),
            OpShipGetSlug
        ) }

    // Hexal
    val EMBARK = if (!Platform.isModLoaded("hexal")) null else
        ACTIONS.register("embark") { ActionRegistryEntry(
            HexPattern.fromAngles("wdewwedwawwdewdwewd", HexDir.EAST),
            OpShipyardWisp(true)
        ) }

    val DISEMBARK = if (!Platform.isModLoaded("hexal")) null else
        ACTIONS.register("disembark") { ActionRegistryEntry(
            HexPattern.fromAngles("wdewwedwawwqawqwawq", HexDir.EAST),
            OpShipyardWisp(false)
        ) }

    fun register() {
        ACTIONS.register()
    }
}