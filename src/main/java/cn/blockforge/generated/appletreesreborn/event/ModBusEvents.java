package cn.blockforge.generated.appletreesreborn.event;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.registry.ModBlocks;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 模组总线事件：把苹果树的方块放进原版创造模式物品栏。
 */
@Mod.EventBusSubscriber(modid = AppleTreesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBusEvents {

    private ModBusEvents() {
    }

    @SubscribeEvent
    public static void onBuildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (!event.getTabKey().equals(CreativeModeTabs.NATURAL_BLOCKS)) {
            return;
        }
        event.accept(ModBlocks.APPLE_SAPLING_ITEM);
        event.accept(ModBlocks.GOLD_APPLE_SAPLING_ITEM);
        event.accept(ModBlocks.APPLE_PLANT_ITEM);
        event.accept(ModBlocks.GOLD_APPLE_PLANT_ITEM);
    }
}
