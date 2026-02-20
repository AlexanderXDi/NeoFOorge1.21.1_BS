package mopk.beer_snacks.blocks;

import mopk.beer_snacks.Beer_snacks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;
import java.util.function.Supplier;

import static mopk.beer_snacks.items.ModItems.ITEMS;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Beer_snacks.MODID);

    public static final ResourceKey<ConfiguredFeature<?, ?>> PALM_TREE_KEY = ResourceKey.create(
            Registries.CONFIGURED_FEATURE,
            ResourceLocation.fromNamespaceAndPath(Beer_snacks.MODID, "palm")
    );

    public static final DeferredBlock<Block> PALM_TREE_LOG_BLOCK = registerBlock("palm_tree_log_block",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));

    public static final DeferredBlock<Block> PALM_TREE_PLANK_BLOCK = registerBlock("palm_tree_plank_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

    public static final DeferredBlock<Block> PALM_TREE_LEAVES_BLOCK = registerBlock("palm_tree_leaves_block",
            () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

    public static final TreeGrower PALM_TREE_GROWER = new TreeGrower(
            "palm",                         // ID для отладки
            Optional.of(PALM_TREE_KEY),           // Дерево для 1x1 (например, дуб)
            Optional.empty(),                     // Дерево для 2x2 (если есть)
            Optional.empty()                      // Редкий вариант дерева
    );

    public static final DeferredBlock<Block> PALM_TREE_SAPLING_BLOCK = registerBlock("palm_tree_sapling_block",
            () -> new SaplingBlock(PALM_TREE_GROWER, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> DeferredItem<BlockItem> registerBlockItem(String name, DeferredBlock<T> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}