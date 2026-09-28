package cn.blockforge.generated.appletreesreborn;

import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import cn.blockforge.generated.appletreesreborn.craft.ConfigCondition;
import cn.blockforge.generated.appletreesreborn.registry.ModBlocks;
import cn.blockforge.generated.appletreesreborn.registry.ModFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * 主类：把苹果树玩法（树苗、树叶上的苹果、世界生成、食用增益）移植到 Forge 1.20.1。
 * 玩法逻辑以 1.12.2 源码为主，模型贴图沿用原模组文件。
 */
@Mod(AppleTreesMod.MOD_ID)
public final class AppleTreesMod {

    public static final String MOD_ID = "appletreesreborn";

    public AppleTreesMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.register(modBus);
        ModFeatures.register(modBus);
        modBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AppleConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // 注册“配方按配置开关启用/禁用”的条件类型，必须在数据包加载前完成。
        event.enqueueWork(() -> CraftingHelper.register(new ConfigCondition.Serializer()));
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
