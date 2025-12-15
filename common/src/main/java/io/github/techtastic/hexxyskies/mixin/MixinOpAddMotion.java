package io.github.techtastic.hexxyskies.mixin;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.common.casting.actions.spells.OpAddMotion;
import io.github.techtastic.hexxyskies.casting.iota.ShipIota;
import io.github.techtastic.hexxyskies.casting.mishaps.MishapShipNotLoaded;
import io.github.techtastic.hexxyskies.nonmixin.ShipAddMotionSpell;
import io.github.techtastic.hexxyskies.util.AssertionUtils;
import io.github.techtastic.hexxyskies.util.MixinHelper;
import net.minecraft.nbt.CompoundTag;
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

@Mixin(OpAddMotion.class)
public abstract class MixinOpAddMotion {
    @Shadow
    public abstract int getArgc();

    @Shadow
    @Final
    private static double MAX_MOTION;

    @Inject(method = "executeWithUserdata", at= @At("HEAD"), remap = false, cancellable = true)
    private void hexxyskies$useShip(List<? extends Iota> args, CastingEnvironment env, CompoundTag userData, CallbackInfoReturnable<SpellAction.Result> cir) {
        if (MixinHelper.INSTANCE.getShipOrEntityIota(args, 0, getArgc()) instanceof ShipIota iota) {
            Vec3 motion = OperatorUtils.getVec3(args, 1, getArgc());
            if (iota.getShip(env.getWorld()) instanceof ServerShip ship) {
                AssertionUtils.INSTANCE.assertShipInRange(env, ship);

                double motionForCost = motion.lengthSqr();
                if (ShipAddMotionSpell.checkAndMarkGivenMotion(userData, ship))
                    motionForCost++;

                motion = motion.lengthSqr() > Math.pow(MAX_MOTION, 3) ? motion.normalize().scale(MAX_MOTION) : motion;
                cir.setReturnValue(new SpellAction.Result(
                        new ShipAddMotionSpell(ship, motion),
                        (long) motionForCost * MediaConstants.DUST_UNIT,
                        List.of(
                                new ParticleSpray(
                                        VectorConversionsMCKt.toMinecraft(ship.getTransform().getPositionInWorld().add(0.0, MixinHelper.INSTANCE.getHeight(ship), 0.0, new Vector3d())),
                                        motion.normalize(),
                                        0.0,
                                        0.1,
                                        20
                                )
                        ),
                        1
                ));
            }
            else throw new MishapShipNotLoaded();
        }
    }



    /*private data class Spell(val target: Entity, val motion: Vec3) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            target.push(motion.x, motion.y, motion.z)
            target.hurtMarked = true // Whyyyyy
        }
    }*/
}
