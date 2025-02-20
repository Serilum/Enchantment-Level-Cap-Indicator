package com.natamus.enchantmentlevelcapindicator.mixin;

import com.mojang.datafixers.util.Pair;
import com.natamus.enchantmentlevelcapindicator.config.ConfigHandler;
import com.natamus.enchantmentlevelcapindicator.util.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(value = EnchantmentScreen.class, priority = 1001)
public class EnchantmentScreenMixin {
    @ModifyVariable(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE_ASSIGN", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0))
    public List<Component> render_list(List<Component> list, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!ConfigHandler.showSymbolInEnchantmentTable) {
            return list;
        }

        Component component = list.getFirst();

        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {
            return list;
        }

        Registry<Enchantment> enchantmentRegistry = localPlayer.connection.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        Pair<Enchantment, Integer> enchantmentPair = Util.parseRawComponentText(component, enchantmentRegistry);
        if (enchantmentPair == null) {
            return list;
        }

        Enchantment enchantment = enchantmentPair.getFirst();
        int level = enchantmentPair.getSecond();

        int maxLevel = enchantment.getMaxLevel();
        if (level < maxLevel) {
            return list;
        }

        MutableComponent clueComponent = Enchantment.getFullname(enchantmentRegistry.wrapAsHolder(enchantment), level).copy();

        return new ArrayList<>(Arrays.asList(Component.translatable("container.enchant.clue", clueComponent.append(Util.getStarComponent())).withStyle(ChatFormatting.WHITE)));
    }
}
