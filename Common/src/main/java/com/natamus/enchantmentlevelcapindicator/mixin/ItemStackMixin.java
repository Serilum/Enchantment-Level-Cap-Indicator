package com.natamus.enchantmentlevelcapindicator.mixin;

import com.mojang.datafixers.util.Pair;
import com.natamus.enchantmentlevelcapindicator.config.ConfigHandler;
import com.natamus.enchantmentlevelcapindicator.util.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mixin(value = ItemStack.class, priority = 1001)
public class ItemStackMixin {
    @ModifyVariable(method = "getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;enabledFeatures()Lnet/minecraft/world/flag/FeatureFlagSet;"))
    public List<Component> getTooltipLines(List<Component> list, Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag) {
        if (!ConfigHandler.showSymbolInItemTooltip) {
            return list;
        }

        if (player == null) {
            return list;
        }

        boolean updatedList = false;

        List<Component> newList = new ArrayList<>();
        for (Component component : list) {
            String rawText = component.toString();
            if (rawText.contains("enchantment.")) {
                Registry<Enchantment> enchantmentRegistry = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

                Pair<Enchantment, Integer> enchantmentPair = Util.parseRawComponentText(rawText, enchantmentRegistry);
                if (enchantmentPair != null) {
                    Enchantment enchantment = enchantmentPair.getFirst();
                    int level = enchantmentPair.getSecond();

                    int maxLevel = enchantment.getMaxLevel();
                    if (level >= maxLevel) {
                        newList.add(component.copy().append(Util.getStarComponent()));

                        updatedList = true;

                        continue;
                    }
                }
            }

            newList.add(component);
        }

        if (updatedList) {
            return newList;
        }

        return list;
    }
}
