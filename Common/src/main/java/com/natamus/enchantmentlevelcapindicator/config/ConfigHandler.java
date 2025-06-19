package com.natamus.enchantmentlevelcapindicator.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.enchantmentlevelcapindicator.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static String maxLevelSymbol = "⭐";
	@Entry(min = 0, max = 15) public static int maxLevelSymbolColourIndex = 6;

	public static void initConfig() {
		configMetaData.put("maxLevelSymbol", Arrays.asList(
			"The symbol used to show that an enchantment is the maximum level. By default a star."
		));
		configMetaData.put("maxLevelSymbolColourIndex", Arrays.asList(
			"The colour index value used for the max level symbol. Possible values; 0: black, 1: dark_blue, 2: dark_green, 3: dark_aqua, 4: dark_red, 5: dark_purple, 6: gold, 7: gray, 8: dark_gray, 9: blue, 10: green, 11: aqua, 12: red, 13: light_purple, 14: yellow, 15: white"
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}