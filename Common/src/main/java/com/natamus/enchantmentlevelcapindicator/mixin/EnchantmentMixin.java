package com.natamus.enchantmentlevelcapindicator.mixin;

import com.natamus.enchantmentlevelcapindicator.util.Util;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = Enchantment.class, priority = 1001)
public class EnchantmentMixin {
    @Inject(method = "getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void getFullname(Holder<Enchantment> enchantmentHolder, int level, CallbackInfoReturnable<Component> cir, MutableComponent mutableComponent) {
        Enchantment enchantment = enchantmentHolder.value();
        int maxLevel = enchantment.getMaxLevel();
        if (level >= maxLevel) {
            mutableComponent.append(Util.getStarComponent());
        }
    }
}
