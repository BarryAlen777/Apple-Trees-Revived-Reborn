package cn.blockforge.generated.appletreesreborn;

import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import cn.blockforge.generated.appletreesreborn.registry.ModBlocks;
import cn.blockforge.generated.appletreesreborn.registry.ModConditions;
import cn.blockforge.generated.appletreesreborn.registry.ModCreativeTabs;
import cn.blockforge.generated.appletreesreborn.registry.ModFeatures;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

/**
 * 主类：把苹果树玩法（树苗、树叶上的苹果、世界生成、食用增益）移植到 NeoForge 1.21.1。
 * 玩法逻辑以 1.20.1 Forge 版为基准，物品、方块、配方、生成和成就都对齐原版行为。
 */
@Mod(AppleTreesMod.MOD_ID)
public final class AppleTreesMod {

    public static final String MOD_ID = "appletreesreborn";

    public AppleTreesMod(IEventBus modBus, ModContainer modContainer) {
        ModBlocks.register(modBus);
        ModFeatures.register(modBus);
        ModCreativeTabs.register(modBus);
        ModConditions.register(modBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, AppleConfig.SPEC);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
