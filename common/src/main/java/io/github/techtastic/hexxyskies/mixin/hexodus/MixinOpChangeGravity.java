package io.github.techtastic.hexxyskies.mixin.hexodus;

import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import io.github.techtastic.hexxyskies.interop.HexodusInterop;
import miyucomics.hexodus.actions.OpChangeGravity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Pseudo
@Mixin(OpChangeGravity.class)
public class MixinOpChangeGravity {
    @Shadow @Final private static int argc;

    @Inject(method = "execute", at = @At("HEAD"), cancellable = true, remap = false)
    private void hexxyskies$executeWithShip(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<SpellAction.Result> cir) {
        SpellAction.Result result = HexodusInterop.INSTANCE.changeShipGravity(args, env, argc);
        if (result != null)
            cir.setReturnValue(result);
    }
}
