package cn.blockforge.generated.appletreesreborn.registry;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.block.ApplePlantBlock;
import cn.blockforge.generated.appletreesreborn.block.AppleSaplingBlock;
import cn.blockforge.generated.appletreesreborn.world.FruitType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 方块与对应物品的注册。苹果方块沿用 1.19.4 参考版的分块方式：
 * 苹果树苗 / 金苹果树苗 / 苹果 / 金苹果 各一个方块。
 */
public final class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, AppleTreesMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AppleTreesMod.MOD_ID);

    public static final RegistryObject<Block> APPLE_SAPLING = BLOCKS.register("apple_sapling",
            () -> new AppleSaplingBlock(FruitType.APPLE, saplingProperties()));
    public static final RegistryObject<Block> GOLD_APPLE_SAPLING = BLOCKS.register("gold_apple_sapling",
            () -> new AppleSaplingBlock(FruitType.GOLDEN, saplingProperties()));
    public static final RegistryObject<Block> APPLE_PLANT = BLOCKS.register("apple_plant",
            () -> new ApplePlantBlock(FruitType.APPLE, plantProperties()));
    public static final RegistryObject<Block> GOLD_APPLE_PLANT = BLOCKS.register("gold_apple_plant",
            () -> new ApplePlantBlock(FruitType.GOLDEN, plantProperties()));

    public static final RegistryObject<Item> APPLE_SAPLING_ITEM =
            ITEMS.register("apple_sapling", () -> new BlockItem(APPLE_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> GOLD_APPLE_SAPLING_ITEM =
            ITEMS.register("gold_apple_sapling", () -> new BlockItem(GOLD_APPLE_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> APPLE_PLANT_ITEM =
            ITEMS.register("apple_plant", () -> new BlockItem(APPLE_PLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> GOLD_APPLE_PLANT_ITEM =
            ITEMS.register("gold_apple_plant", () -> new BlockItem(GOLD_APPLE_PLANT.get(), new Item.Properties()));

    private ModBlocks() {
    }

    public static Block plantFor(FruitType type) {
        return (type == FruitType.GOLDEN ? GOLD_APPLE_PLANT : APPLE_PLANT).get();
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }

    private static BlockBehaviour.Properties saplingProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties plantProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion();
    }
}
