package mopk.beer_snacks.events;

import mopk.beer_snacks.Beer_snacks;
import mopk.beer_snacks.blocks.ModBlocks;
import mopk.beer_snacks.items.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Beer_snacks.MODID)
public class ModEvents {

    @SubscribeEvent
    public static void onItemClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        boolean hasShears = mainHand.is(Items.SHEARS) || offHand.is(Items.SHEARS);
        boolean hasCoconut = mainHand.is(ModBlocks.COCONUT_BLOCK.get().asItem()) || offHand.is(ModBlocks.COCONUT_BLOCK.get().asItem());

        if (hasShears && hasCoconut) {
            ItemStack coconutStack = mainHand.is(ModBlocks.COCONUT_BLOCK.get().asItem()) ? mainHand : offHand;
            //кулдаун
            if (player.getCooldowns().isOnCooldown(coconutStack.getItem())) {
                event.setCancellationResult(InteractionResult.FAIL);
                return;
            }

            event.setCancellationResult(InteractionResult.SUCCESS);

            if (!event.getLevel().isClientSide) {
                CustomData customData = coconutStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag tag = customData.copyTag();
                int clicks = tag.getInt("shave_clicks");

                if (clicks < 2) {
                    tag.putInt("shave_clicks", clicks + 1);
                    coconutStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

                    player.level().playSound(null, player.blockPosition(), SoundEvents.SHEEP_SHEAR, SoundSource.PLAYERS, 1.0F, 1.0F);
                    player.getCooldowns().addCooldown(coconutStack.getItem(), 10);
                } else if (clicks == 2) {

                    coconutStack.shrink(1);

                    ItemStack shavedResult = new ItemStack(ModItems.SHAVED_COCONUT.get());
                    if (!player.getInventory().add(shavedResult)) {
                        player.drop(shavedResult, false); // Если инвентарь полон - выкидываем на землю
                    }

                    ItemStack shearsStack = mainHand.is(Items.SHEARS) ? mainHand : offHand;
                    shearsStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(mainHand.is(Items.SHEARS) ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND));

                    player.level().playSound(null, player.blockPosition(), SoundEvents.BAMBOO_STEP, SoundSource.PLAYERS, 1.0F, 1.2F);

                    tag.putInt("shave_clicks", -1);
                    coconutStack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
                }
            }

            // Машем рукой визуально
            player.swing(event.getHand(), true);
        }
    }
}