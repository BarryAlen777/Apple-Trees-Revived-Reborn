package cn.blockforge.generated.appletreesreborn;

import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import cn.blockforge.generated.appletreesreborn.craft.ConfigCondition;
import cn.blockforge.generated.appletreesreborn.registry.ModBlocks;
import cn.blockforge.generated.appletreesreborn.registry.ModCreativeTabs;
import cn.blockforge.generated.appletreesreborn.registry.ModFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

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
        ModCreativeTabs.register(modBus);
        modBus.addListener(this::commonSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AppleConfig.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // 注册“配方按配置开关启用/禁用”的条件类型，必须在数据包加载前完成。
        event.enqueueWork(() -> {
            CraftingHelper.register(new ConfigCondition.Serializer());
            registerAxeStrippables();
        });
    }

    /**
     * 斧头右键去皮：1.20.1 的“原木→去皮原木”对照表写死在 AxeItem 里，
     * 1.19.4 参考版用 AT 开放字段达到同样目的；这里反射取那张表把自己的木头加进去。
     * 找不到字段就跳过，最多去皮不生效，不影响其他功能。
     */
    @SuppressWarnings("unchecked")
    private static void registerAxeStrippables() {
        Logger log = LogManager.getLogger(MOD_ID);
        try {
            Map<Block, Block> strippables = null;
            for (Field field : AxeItem.class.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Map.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    Object value = field.get(null);
                    if (value instanceof Map<?, ?> map && map.containsKey(Blocks.OAK_LOG)) {
                        strippables = (Map<Block, Block>) map;
                        break;
                    }
                }
            }
            if (strippables == null) {
                log.warn("在 AxeItem 里没找到去皮对照表，苹果木斧头去皮不可用（配方不受影响）。");
                return;
            }
            strippables.put(ModBlocks.APPLE_LOG.get(), ModBlocks.STRIPPED_APPLE_LOG.get());
            strippables.put(ModBlocks.APPLE_WOOD.get(), ModBlocks.STRIPPED_APPLE_WOOD.get());
            log.info("苹果木斧头去皮已接线：apple_log→stripped_apple_log, apple_wood→stripped_apple_wood。");
        } catch (Throwable t) {
            log.warn("注册苹果木去皮对照失败：{}", t.getMessage());
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
