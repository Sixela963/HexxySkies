package io.github.techtastic.hexxyskies.mixin.hexodus;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import gravity_changer.GravityComponent;
import io.github.techtastic.hexxyskies.interop.HexodusInterop;
import kotlin.jvm.functions.Function1;
import miyucomics.hexodus.HexodusComponent;
import miyucomics.hexodus.actions.OpGetGravity;
import miyucomics.hexodus.actions.OpGetTrueGravity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(OpGetTrueGravity.class)
public class MixinOpGetTrueGravity {
    @Shadow @Final private int argc;
    @Shadow @Final private Function1<GravityComponent, List<Iota>> operate;

    @Inject(method = "execute", at = @At("HEAD"), cancellable = true, remap = false)
    private void hexxyskies$executeWithShip(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
        List<Iota> result = HexodusInterop.INSTANCE.getTrueShipGravity(args, env, argc, operate);
        if (result != null) cir.setReturnValue(result);
    }
}
