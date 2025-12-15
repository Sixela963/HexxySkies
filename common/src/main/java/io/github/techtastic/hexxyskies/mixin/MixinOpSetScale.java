package io.github.techtastic.hexxyskies.mixin;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.interop.pehkui.OpSetScale;
import io.github.techtastic.hexxyskies.casting.iota.ShipIota;
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipNotLoaded;
import io.github.techtastic.hexxyskies.nonmixin.ShipScaleSpell;
import io.github.techtastic.hexxyskies.util.AssertionUtils;
import io.github.techtastic.hexxyskies.util.MixinHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.List;

@Mixin(OpSetScale.class)
public class MixinOpSetScale {
    @Shadow
    @Final
    private static int argc;

    @Inject(method = "execute", at= @At("HEAD"), remap = false, cancellable = true)
    private void hexxyskies$useShip(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<SpellAction.Result> cir) {
        if (MixinHelper.INSTANCE.getShipOrEntityIota(args, 0, argc) instanceof ShipIota iota) {
            if (iota.getShip(env.getWorld()) instanceof ServerShip ship) {
                double scale = OperatorUtils.getDoubleBetween(args, 1, 1/10d, 10, argc);
                AssertionUtils.INSTANCE.assertShipInRange(env, ship);

                cir.setReturnValue(new SpellAction.Result(
                        new ShipScaleSpell(env.getWorld(), ship, scale),
                        50000L,
                        List.of(ParticleSpray.burst(VectorConversionsMCKt.toMinecraft(ship.getTransform().getPositionInWorld()), scale, 40)),
                        1
                ));
            }
            else throw new MishapShipNotLoaded();
        }
    }
}
