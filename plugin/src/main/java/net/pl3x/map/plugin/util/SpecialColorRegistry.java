package net.pl3x.map.plugin.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.server.v1_16_R3.Block;
import net.minecraft.server.v1_16_R3.BlockCrops;
import net.minecraft.server.v1_16_R3.BlockStem;
import net.minecraft.server.v1_16_R3.Blocks;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.pl3x.map.plugin.configuration.Config;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;
import java.util.function.Function;

public final class SpecialColorRegistry {
    public static final int CLEAR_GLASS_COLOR = 0x88FFFFFF;

    private static volatile SpecialColorRegistry instance;

    public static @NonNull SpecialColorRegistry get() {
        if (instance == null) {
            synchronized (SpecialColorRegistry.class) {
                if (instance == null) {
                    instance = new SpecialColorRegistry();
                }
            }
        }
        return instance;
    }

    private final Map<Block, Integer> staticColorMap;
    private final Map<Block, Function<IBlockData, Integer>> dynamicColorMap;

    private SpecialColorRegistry() {
        this.staticColorMap = this.loadStaticColors();
        this.dynamicColorMap = this.loadDynamicColors();
    }

    private @NonNull Map<Block, Integer> loadStaticColors() {
        final ImmutableMap.Builder<Block, Integer> staticColorBuilder = ImmutableMap.builder();

        // Glass
        staticColorBuilder.put(Blocks.GLASS, CLEAR_GLASS_COLOR);

        // Flowers
        staticColorBuilder.put(Blocks.DANDELION, 0xFFEC4F);
        staticColorBuilder.put(Blocks.POPPY, 0xED302C);
        staticColorBuilder.put(Blocks.BLUE_ORCHID, 0x2ABFFD);
        staticColorBuilder.put(Blocks.ALLIUM, 0xB878ED);
        staticColorBuilder.put(Blocks.AZURE_BLUET, 0xF7F7F7);
        staticColorBuilder.put(Blocks.RED_TULIP, 0x9B221A);
        staticColorBuilder.put(Blocks.ORANGE_TULIP, 0xBD6A22);
        staticColorBuilder.put(Blocks.PINK_TULIP, 0xEBC5FD);
        staticColorBuilder.put(Blocks.WHITE_TULIP, 0xD6E8E8);
        staticColorBuilder.put(Blocks.OXEYE_DAISY, 0xD6E8E8);
        staticColorBuilder.put(Blocks.CORNFLOWER, 0x466AEB);
        staticColorBuilder.put(Blocks.LILY_OF_THE_VALLEY, 0xFFFFFF);
        staticColorBuilder.put(Blocks.WITHER_ROSE, 0x211A16);
        staticColorBuilder.put(Blocks.SUNFLOWER, 0xFFEC4F);
        staticColorBuilder.put(Blocks.LILAC, 0xB66BB2);
        staticColorBuilder.put(Blocks.ROSE_BUSH, 0x9B221A);
        staticColorBuilder.put(Blocks.PEONY, 0xEBC5FD);

        // Birch/Spruce leaves
        staticColorBuilder.put(Blocks.SPRUCE_LEAVES, Colors.mix(Colors.leavesMapColor().rgb, 0x619961, 0.5F));
        staticColorBuilder.put(Blocks.BIRCH_LEAVES, Colors.mix(Colors.leavesMapColor().rgb, 0x80A755, 0.5F));

        // Misc plants
        staticColorBuilder.put(Blocks.LILY_PAD, 0x208030);
        staticColorBuilder.put(Blocks.ATTACHED_MELON_STEM, 0xE0C71C);
        staticColorBuilder.put(Blocks.ATTACHED_PUMPKIN_STEM, 0xE0C71C);

        // Lava
        staticColorBuilder.put(Blocks.LAVA, 0xEA5C0F); // red was so ugly. lets go with orange

        // Load overrides
        staticColorBuilder.putAll(Config.COLOR_OVERRIDES);

        return staticColorBuilder.build();
    }

    private @NonNull Map<Block, Function<IBlockData, Integer>> loadDynamicColors() {
        final ImmutableMap.Builder<Block, Function<IBlockData, Integer>> dynamicColorBuilder = ImmutableMap.builder();

        dynamicColorBuilder.put(Blocks.MELON_STEM, SpecialColorRegistry::melonAndPumpkinStem);
        dynamicColorBuilder.put(Blocks.PUMPKIN_STEM, SpecialColorRegistry::melonAndPumpkinStem);
        dynamicColorBuilder.put(Blocks.WHEAT, SpecialColorRegistry::wheat);

        return dynamicColorBuilder.build();
    }

    /**
     * Get a special color for a IBlockData, it it exists. Will return -1 if there
     * is no special color for the provided IBlockData.
     *
     * @param state IBlockData to test
     * @return special color, or -1
     */
    public int getColor(final @NonNull IBlockData state) {
        final Block block = state.getBlock();

        final Integer staticColor = this.staticColorMap.get(block);
        if (staticColor != null) {
            return staticColor;
        }

        final Function<IBlockData, Integer> func = this.dynamicColorMap.get(block);
        if (func != null) {
            return func.apply(state);
        }

        return -1;
    }

    private static int melonAndPumpkinStem(final @NonNull IBlockData state) {
        int age = state.get(BlockStem.AGE);
        int k = age * 32;
        int l = 255 - age * 8;
        int m = age * 4;
        return k << 16 | l << 8 | m;
    }

    private static int wheat(final @NonNull IBlockData state) {
        float factor = (state.get(BlockCrops.AGE) + 1) / 8F;
        return Colors.mix(Colors.plantsMapColor().rgb, 0xDCBB65, factor);
    }

}
