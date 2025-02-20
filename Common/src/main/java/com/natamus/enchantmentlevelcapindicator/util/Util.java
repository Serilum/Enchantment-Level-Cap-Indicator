package com.natamus.enchantmentlevelcapindicator.util;

import com.mojang.datafixers.util.Pair;
import com.natamus.collective.functions.NumberFunctions;
import com.natamus.enchantmentlevelcapindicator.config.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.Nullable;

public class Util {
	public static @Nullable Pair<Enchantment, Integer> parseRawComponentText(Component component, Registry<Enchantment> enchantmentRegistry) {
		return parseRawComponentText(component.toString(), enchantmentRegistry);
	}
	public static @Nullable Pair<Enchantment, Integer> parseRawComponentText(String rawText, Registry<Enchantment> enchantmentRegistry) {
		String[] rawEnchSpl = rawText.split("enchantment.");
        if (rawEnchSpl.length < 2) {
            return null;
        }

        String rawEnchantment = rawEnchSpl[1].split("'")[0];
		ResourceLocation enchantmentResourceLocation = ResourceLocation.parse(rawEnchantment.replace(".", ":"));

		int level = 1;
		if (rawEnchSpl.length > 2) {
			String rawEnchantmentLevel = rawEnchSpl[2].split("'")[0].replace("level.", "");

			if (!NumberFunctions.isNumeric(rawEnchantmentLevel)) {
				return null;
			}

			level = Integer.parseInt(rawEnchantmentLevel);
		}

        Enchantment enchantment = enchantmentRegistry.get(enchantmentResourceLocation);
        if (enchantment == null) {
            return null;
        }

		return Pair.of(enchantment, level);
	}

	public static Component getStarComponent() {
		ChatFormatting symbolColour = ChatFormatting.getById(ConfigHandler.maxLevelSymbolColourIndex);
		if (symbolColour == null) {
			symbolColour = ChatFormatting.GOLD;
		}

		return Component.literal(" " + ConfigHandler.maxLevelSymbol).withStyle(symbolColour);
	}
}
