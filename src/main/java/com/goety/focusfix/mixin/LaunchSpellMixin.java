package com.goety.focusfix.mixin;

import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.wind.LaunchSpell;
import com.Polarice3.Goety.utils.WandUtil;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LaunchSpell.class)
public abstract class LaunchSpellMixin {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method = "defaultCastDuration", at = @At("HEAD"), cancellable = true, remap = false)
    private void goety_revertCastDuration(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(0);
    }

    @Inject(method = "SpellResult", at = @At("HEAD"), cancellable = true, remap = false)
    private void goety_revertLaunchVelocity(ServerLevel level, LivingEntity caster, ItemStack wand, SpellStat stat, CallbackInfo ci) {
        int potency = stat.getPotency();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
        }
        boolean staff = ((Spell) (Object) this).rightStaff(wand);
        double mult = staff ? 2.5D : 1.5D;
        double speed = mult + potency / 2.0D;
        LOGGER.info("[LaunchSpellMixin] staff={} potency={} mult={} speed={}", staff, potency, mult, speed);

        caster.hurtMarked = true;
        caster.setOnGround(false);
        Vec3 vec = caster.getLookAngle();
        caster.setDeltaMovement(vec.x * speed, vec.y * speed, vec.z * speed);
        caster.hasImpulse = true;
        caster.fallDistance = 0.0F;
        ((Spell) (Object) this).playSound(level, caster, 2.0F, 1.0F);
        ci.cancel();
    }
}
