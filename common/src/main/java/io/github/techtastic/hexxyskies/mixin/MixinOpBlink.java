package io.github.techtastic.hexxyskies.mixin;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.mod.HexConfig;
import at.petrak.hexcasting.common.casting.actions.spells.OpBlink;
import io.github.techtastic.hexxyskies.casting.iota.ShipIota;
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipNotLoaded;
import io.github.techtastic.hexxyskies.nonmixin.ShipBlinkSpell;
import io.github.techtastic.hexxyskies.util.AssertionUtils;
import io.github.techtastic.hexxyskies.util.MixinHelper;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

import java.util.List;

@Mixin(OpBlink.class)
public class MixinOpBlink {
    @Shadow
    @Final
    private static int argc;

    @Inject(method = "execute", at= @At("HEAD"), remap = false, cancellable = true)
    private void hexxyskies$useShip(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<SpellAction.Result> cir) {
        if (MixinHelper.INSTANCE.getShipOrEntityIota(args, 0, argc) instanceof ShipIota iota) {
            double delta = OperatorUtils.getDouble(args, 1, argc);
            if (iota.getShip(env.getWorld()) instanceof ServerShip ship) {
                AssertionUtils.INSTANCE.assertShipInRange(env, ship);

                Vec3 endPos = VectorConversionsMCKt.toMinecraft(ShipBlinkSpell.Companion.getEndPosition(ship, delta));
                if (!HexConfig.server().canTeleportInThisDimension(env.getWorld().dimension()))
                    throw new MishapBadLocation(endPos, "bad_dimension");

                env.assertVecInRange(endPos);
                if (!env.isVecInWorld(endPos.subtract(0.0, 1.0, 0.0)))
                    throw new MishapBadLocation(endPos, "too_close_to_out");

                Vec3 targetMiddlePos = VectorConversionsMCKt.toMinecraft(ship.getTransform().getPositionInWorld()).add(0.0, MixinHelper.INSTANCE.getHeight(ship) / 2.0, 0.0);

                cir.setReturnValue(new SpellAction.Result(
                        new ShipBlinkSpell(env.getWorld(), ship, delta),
                        (long) (MediaConstants.SHARD_UNIT * Math.abs(delta) * 0.5),
                        List.of(
                                ParticleSpray.cloud(targetMiddlePos, 2.0, 50),
                                ParticleSpray.burst(targetMiddlePos.add(VectorConversionsMCKt.toMinecraft(ship.getTransform().getShipToWorld().transformDirection(new Vector3d(0.0, 0.0, 1.0)))), 2.0, 100)
                        ),
                        1
                ));
            } else throw new MishapShipNotLoaded();
        }
    }
}
