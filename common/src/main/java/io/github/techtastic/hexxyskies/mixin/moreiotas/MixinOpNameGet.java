package io.github.techtastic.hexxyskies.mixin.moreiotas;

import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.iota.Iota;
import com.mojang.datafixers.util.Either;
import io.github.techtastic.hexxyskies.util.OperatorUtils;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.Ship;
import ram.talia.moreiotas.api.casting.iota.StringIota;
import ram.talia.moreiotas.common.casting.actions.strings.OpNameGet;

import java.util.List;

@Mixin(OpNameGet.class)
public class MixinOpNameGet {
    @Inject(method = "execute", at = @At("HEAD"), cancellable = true, remap = false)
    private void hexxyskies$executeWithShip(List<? extends Iota> args, CastingEnvironment env, CallbackInfoReturnable<List<Iota>> cir) {
        Either<Ship, Entity> target = OperatorUtils.INSTANCE.getShipOrEntity(args, env.getWorld(), 0, 1);

        if (target.left().isPresent()) {
            cir.setReturnValue(List.of(StringIota.make(target.left().get().getSlug())));
        }
    }
}
