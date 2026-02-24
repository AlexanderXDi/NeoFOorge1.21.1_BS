package mopk.beer_snacks;

import static mopk.beer_snacks.CreativeTab.CREATIVE_MODE_TABS;
import static mopk.beer_snacks.CreativeTab.MOD_TAB;
import static mopk.beer_snacks.blocks.ModBlocks.*;
import static mopk.beer_snacks.items.ModItems.*;
import static mopk.beer_snacks.tree_generation.PalmFoliagePlacer.*;
import static mopk.beer_snacks.tree_generation.PalmTrunkPlacer.*;
import mopk.beer_snacks.worldgen.ModTerrablender;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;


@Mod(Beer_snacks.MODID)
public class Beer_snacks {
    public static final String MODID = "beer_snacks";

    public Beer_snacks(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);

        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        FOLIAGE.register(modEventBus);
        TRUNKS.register(modEventBus);

        modEventBus.addListener(this::buildCreativeTabs);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModTerrablender.registerRegions();
    }

    private void buildCreativeTabs(final BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == MOD_TAB.getKey()) {
            event.accept(TAB_ICON.get());
            event.accept(PALM_TREE_LOG_BLOCK.get());
            event.accept(PALM_TREE_PLANK_BLOCK.get());
            event.accept(PALM_TREE_LEAVES_BLOCK.get());
            event.accept(PALM_TREE_SAPLING_BLOCK.get());
            event.accept(COCONUT_BLOCK.get());
            event.accept(SHAVED_COCONUT.get());
        }
    }
}