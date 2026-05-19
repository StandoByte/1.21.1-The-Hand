package com.yourname.ripples_addon.init;

import com.yourname.ripples_addon.RipplesAddon;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.github.standobyte.jojo.init.ModItems.MAIN_TAB;

@EventBusSubscriber(modid = RipplesAddon.MOD_ID)
public final class AddonItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RipplesAddon.MOD_ID);

	public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerItem("example_item", Item::new, new Item.Properties());

    @SubscribeEvent
    public static void addToTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == MAIN_TAB.getKey()) {
            event.accept(EXAMPLE_ITEM);
        }
    }
}