package cn.blockforge.generated.appletreesreborn.registry;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.block.ApplePlantBlock;
import cn.blockforge.generated.appletreesreborn.block.AppleSaplingBlock;
import cn.blockforge.generated.appletreesreborn.world.FruitType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnchantedGoldenAppleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 方块与对应物品的注册，内容对齐 1.19.4 参考版（Apple Trees Revived v2.x）：
 * 三种果实（苹果 / 金苹果 / 翡翠苹果）、一整套苹果木木材（原木、去皮原木、
 * 树木、去皮树木、木板、台阶、石化台阶、楼梯、栅栏、栅栏门、门、按钮、
 * 压力板、活板门）以及苹果树叶。
 */
public final class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, AppleTreesMod.MOD_ID);
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, AppleTreesMod.MOD_ID);

    // ------------------------------------------------------------------
    // 树苗与果实方块
    // ------------------------------------------------------------------

    public static final RegistryObject<Block> APPLE_SAPLING = BLOCKS.register("apple_sapling",
            () -> new AppleSaplingBlock(FruitType.APPLE, saplingProperties()));
    public static final RegistryObject<Block> GOLD_APPLE_SAPLING = BLOCKS.register("gold_apple_sapling",
            () -> new AppleSaplingBlock(FruitType.GOLDEN, saplingProperties()));
    public static final RegistryObject<Block> EMERALD_APPLE_SAPLING = BLOCKS.register("emerald_apple_sapling",
            () -> new AppleSaplingBlock(FruitType.EMERALD, saplingProperties()));

    public static final RegistryObject<Block> APPLE_PLANT = BLOCKS.register("apple_plant",
            () -> new ApplePlantBlock(FruitType.APPLE, plantProperties()));
    public static final RegistryObject<Block> GOLD_APPLE_PLANT = BLOCKS.register("gold_apple_plant",
            () -> new ApplePlantBlock(FruitType.GOLDEN, plantProperties()));
    public static final RegistryObject<Block> EMERALD_APPLE_PLANT = BLOCKS.register("emerald_apple_plant",
            () -> new ApplePlantBlock(FruitType.EMERALD, plantProperties()));

    // ------------------------------------------------------------------
    // 翡翠苹果（v2.1 新果实）：吃下给幸运 + 急迫，参考 1.19.4 的数值
    // ------------------------------------------------------------------

    private static final FoodProperties EMERALD_APPLE_FOOD = new FoodProperties.Builder()
            .nutrition(4)
            .saturationMod(1.2F)
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, 3600, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 1600, 0), 1.0F)
            .alwaysEat()
            .build();

    public static final RegistryObject<Item> EMERALD_APPLE_ITEM = ITEMS.register("emerald_apple",
            () -> new EnchantedGoldenAppleItem(new Item.Properties()
                    .food(EMERALD_APPLE_FOOD)
                    .rarity(Rarity.EPIC)));

    // ------------------------------------------------------------------
    // 苹果木全套（对应 1.19.4 BlockInit 的木材部分）
    // ------------------------------------------------------------------

    public static final RegistryObject<Block> APPLE_LEAVES = BLOCKS.register("apple_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
    public static final RegistryObject<Block> APPLE_LOG = BLOCKS.register("apple_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> STRIPPED_APPLE_LOG = BLOCKS.register("stripped_apple_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<Block> APPLE_WOOD = BLOCKS.register("apple_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<Block> STRIPPED_APPLE_WOOD = BLOCKS.register("stripped_apple_wood",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> APPLE_PLANKS = BLOCKS.register("apple_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> APPLE_STAIRS = BLOCKS.register("apple_stairs",
            () -> new StairBlock(() -> APPLE_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> APPLE_SLAB = BLOCKS.register("apple_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> PETRIFIED_APPLE_SLAB = BLOCKS.register("petrified_apple_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.PETRIFIED_OAK_SLAB)));
    public static final RegistryObject<Block> APPLE_FENCE = BLOCKS.register("apple_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> APPLE_FENCE_GATE = BLOCKS.register("apple_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE),
                    WoodType.OAK));
    public static final RegistryObject<Block> APPLE_DOOR = BLOCKS.register("apple_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), BlockSetType.OAK));
    public static final RegistryObject<Block> APPLE_BUTTON = BLOCKS.register("apple_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON),
                    BlockSetType.OAK, 30, true));
    public static final RegistryObject<Block> APPLE_PRESSURE_PLATE = BLOCKS.register("apple_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), BlockSetType.OAK));
    public static final RegistryObject<Block> APPLE_TRAPDOOR = BLOCKS.register("apple_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR),
                    BlockSetType.OAK));

    // ------------------------------------------------------------------
    // 对应的方块物品
    // ------------------------------------------------------------------

    public static final RegistryObject<Item> APPLE_SAPLING_ITEM =
            ITEMS.register("apple_sapling", () -> new BlockItem(APPLE_SAPLING.get(), new Item.Properties()));
    public static final RegistryObject<Item> GOLD_APPLE_SAPLING_ITEM =
            ITEMS.register("gold_apple_sapling",
                    () -> new BlockItem(GOLD_APPLE_SAPLING.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> EMERALD_APPLE_SAPLING_ITEM =
            ITEMS.register("emerald_apple_sapling",
                    () -> new BlockItem(EMERALD_APPLE_SAPLING.get(), new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> APPLE_PLANT_ITEM =
            ITEMS.register("apple_plant", () -> new BlockItem(APPLE_PLANT.get(), new Item.Properties()));
    public static final RegistryObject<Item> GOLD_APPLE_PLANT_ITEM =
            ITEMS.register("gold_apple_plant",
                    () -> new BlockItem(GOLD_APPLE_PLANT.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> EMERALD_APPLE_PLANT_ITEM =
            ITEMS.register("emerald_apple_plant",
                    () -> new BlockItem(EMERALD_APPLE_PLANT.get(), new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> APPLE_LEAVES_ITEM = blockItem(APPLE_LEAVES);
    public static final RegistryObject<Item> APPLE_LOG_ITEM = blockItem(APPLE_LOG);
    public static final RegistryObject<Item> STRIPPED_APPLE_LOG_ITEM = blockItem(STRIPPED_APPLE_LOG);
    public static final RegistryObject<Item> APPLE_WOOD_ITEM = blockItem(APPLE_WOOD);
    public static final RegistryObject<Item> STRIPPED_APPLE_WOOD_ITEM = blockItem(STRIPPED_APPLE_WOOD);
    public static final RegistryObject<Item> APPLE_PLANKS_ITEM = blockItem(APPLE_PLANKS);
    public static final RegistryObject<Item> APPLE_STAIRS_ITEM = blockItem(APPLE_STAIRS);
    public static final RegistryObject<Item> APPLE_SLAB_ITEM = blockItem(APPLE_SLAB);
    public static final RegistryObject<Item> PETRIFIED_APPLE_SLAB_ITEM = blockItem(PETRIFIED_APPLE_SLAB);
    public static final RegistryObject<Item> APPLE_FENCE_ITEM = blockItem(APPLE_FENCE);
    public static final RegistryObject<Item> APPLE_FENCE_GATE_ITEM = blockItem(APPLE_FENCE_GATE);
    public static final RegistryObject<Item> APPLE_DOOR_ITEM = blockItem(APPLE_DOOR);
    public static final RegistryObject<Item> APPLE_BUTTON_ITEM = blockItem(APPLE_BUTTON);
    public static final RegistryObject<Item> APPLE_PRESSURE_PLATE_ITEM = blockItem(APPLE_PRESSURE_PLATE);
    public static final RegistryObject<Item> APPLE_TRAPDOOR_ITEM = blockItem(APPLE_TRAPDOOR);

    private ModBlocks() {
    }

    private static RegistryObject<Item> blockItem(RegistryObject<Block> block) {
        String name = block.getId().getPath();
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static Block plantFor(FruitType type) {
        return switch (type) {
            case APPLE -> APPLE_PLANT.get();
            case GOLDEN -> GOLD_APPLE_PLANT.get();
            case EMERALD -> EMERALD_APPLE_PLANT.get();
        };
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
                .sound(net.minecraft.world.level.block.SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY);
    }

    private static BlockBehaviour.Properties plantProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .noCollission()
                .randomTicks()
                .instabreak()
                .sound(net.minecraft.world.level.block.SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY)
                .noOcclusion();
    }
}
