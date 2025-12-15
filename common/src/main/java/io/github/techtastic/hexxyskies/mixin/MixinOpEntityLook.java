package io.github.techtastic.hexxyskies.mixin;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import io.github.techtastic.hexxyskies.casting.iota.ShipIota;
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipNotLoaded;
import io.github.techtastic.hexxyskies.util.MixinHelper;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import at.petrak.hexcasting.common.casting.actions.queryentity.OpEntityLook;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.List;

@Pseudo
@Mixin(OpEntityLook.class)
public class MixinOpEntityLook {
    @Shadow
    @Final
    private static int argc;

    @Inject(method = "execute", at= @At("HEAD"), remap = false, cancellable = true)
    private void hexxyskies$useShip(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
        if (MixinHelper.INSTANCE.getShipOrEntityIota(args, 0, argc) instanceof ShipIota iota) {
            if (iota.getShip(env.getWorld()) instanceof ServerShip ship) {
                Vector3d look = ship.getTransform().getShipToWorld().transformDirection(new Vector3d(0, 0, 1));
                cir.setReturnValue(List.of(new Vec3Iota(VectorConversionsMCKt.toMinecraft(look))));
            }
            else throw new MishapShipNotLoaded();
        }
    }
}
