package io.github.techtastic.hexxyskies.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.DoubleIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.interop.pehkui.OpGetScale;
import io.github.techtastic.hexxyskies.casting.iota.ShipIota;
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipNotLoaded;
import io.github.techtastic.hexxyskies.util.AssertionUtils;
import io.github.techtastic.hexxyskies.util.MixinHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ServerShip;

import java.util.List;

@Mixin(OpGetScale.class)
public class MixinOpGetScale {
    @Shadow
    @Final
    private static int argc;

    @Inject(method = "execute", at= @At("HEAD"), remap = false, cancellable = true)
    private void hexxyskies$useShip(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
        if (MixinHelper.INSTANCE.getShipOrEntityIota(args, 0, argc) instanceof ShipIota iota) {
            if (iota.getShip(env.getWorld()) instanceof ServerShip ship) {
                AssertionUtils.INSTANCE.assertShipInRange(env, ship);
                cir.setReturnValue(List.of(new DoubleIota(ship.getTransform().getShipToWorldScaling().x())));
            }
            else throw new MishapShipNotLoaded();
        }
    }
}
