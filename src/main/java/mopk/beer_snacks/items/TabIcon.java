package mopk.beer_snacks.items;

import mopk.beer_snacks.Beer_snacks;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


public class TabIcon {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Beer_snacks.MODID);

    public static final DeferredItem<Item> TAB_ICON = ITEMS.register("tab_icon",
            () -> new Item(new Item.Properties()));
}