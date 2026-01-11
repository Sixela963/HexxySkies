package io.github.techtastic.hexxyskies.casting.mishaps

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

class MishapNotOnShip(val pos: Vec3) : Mishap() {
    override fun accentColor(ctx: CastingEnvironment, errorCtx: Context): FrozenPigment = ctx.pigment

    override fun errorMessage(ctx: CastingEnvironment, errorCtx: Context): Component = Component.translatable("hexxyskies.mishap.invalid.ship.pos", pos)

    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: MutableList<Iota>) {}
}