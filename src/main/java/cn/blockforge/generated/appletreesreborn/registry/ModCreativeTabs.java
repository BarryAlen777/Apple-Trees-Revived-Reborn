package cn.blockforge.generated.appletreesreborn.registry;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * 独立创造模式标签页：对齐 1.19.4 原版（Apple Trees Revived 自带 apple_tab），
 * 模组全部物品集中放在自己的页签里，顺序也照原版的展示顺序排。
 * 图标用苹果树苗，和原版一致。
 */
public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AppleTreesMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> APPLE_TAB = TABS.register("apple_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.appletreesreborn.apple_tab"))
                    .icon(() -> new ItemStack(ModBlocks.APPLE_SAPLING_ITEM.get()))
                    .displayItems((parameters, output) -> {
                        // —— 果实与树苗（原版页签开头就是翡翠苹果）——
                        output.accept(new ItemStack(ModBlocks.EMERALD_APPLE_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_SAPLING_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.GOLD_APPLE_SAPLING_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.EMERALD_APPLE_SAPLING_ITEM.get()));
                        // —— 挂在树上的果实方块（1.19.4 原版页签没有这三项，这里补上方便创造取用）——
                        output.accept(new ItemStack(ModBlocks.APPLE_PLANT_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.GOLD_APPLE_PLANT_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.EMERALD_APPLE_PLANT_ITEM.get()));
                        // —— 树叶与原木系 ——
                        output.accept(new ItemStack(ModBlocks.APPLE_LEAVES_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_LOG_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.STRIPPED_APPLE_LOG_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_WOOD_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.STRIPPED_APPLE_WOOD_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_PLANKS_ITEM.get()));
                        // —— 木制品 ——
                        output.accept(new ItemStack(ModBlocks.APPLE_STAIRS_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_SLAB_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.PETRIFIED_APPLE_SLAB_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_FENCE_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_FENCE_GATE_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_DOOR_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_BUTTON_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_PRESSURE_PLATE_ITEM.get()));
                        output.accept(new ItemStack(ModBlocks.APPLE_TRAPDOOR_ITEM.get()));
                    })
                    .build());

    private ModCreativeTabs() {
    }

    public static void register(IEventBus bus) {
        TABS.register(bus);
    }
}
