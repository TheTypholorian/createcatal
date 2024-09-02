package net.xander.createcatal;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue ENABLE_CALCIFYING = general("Calcifying");
    public static final ForgeConfigSpec.BooleanValue ENABLE_ROARING = general("Roaring");
    public static final ForgeConfigSpec.BooleanValue ENABLE_ROOTING = general("Rooting");
    public static final ForgeConfigSpec.BooleanValue ENABLE_SAVORING = general("Savoring");
    public static final ForgeConfigSpec.BooleanValue ENABLE_SHRIEKING = general("Shrieking");
    public static final ForgeConfigSpec.BooleanValue ENABLE_SINNING = general("Sinning");
    public static final ForgeConfigSpec.BooleanValue ENABLE_STICKING = general("Sticking");

    private static ForgeConfigSpec.BooleanValue general(String name) {
        return BUILDER
                .comment("Enable " + name + "?")
                .define("enable" + name, true);
    }

    /*
    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::validateItemName);
     */

    static final ForgeConfigSpec SPEC = BUILDER.build();
}
