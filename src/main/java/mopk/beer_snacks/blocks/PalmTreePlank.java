package mopk.beer_snacks.blocks;

import mopk.beer_snacks.Beer_snacks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;


public class PalmTreePlank {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(Beer_snacks.MODID);

    public static final DeferredBlock<Block> PALM_TREE_PLANK = BLOCKS.register("dummy_block",
            () -> new Block(Block.Properties.ofFullCopy(Blocks.STONE)));
}