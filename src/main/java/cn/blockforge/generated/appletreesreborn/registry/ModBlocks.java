package cn.blockforge.generated.appletreesreborn.registry;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.block.AppleFenceBlock;
import cn.blockforge.generated.appletreesreborn.block.AppleFenceGateBlock;
import cn.blockforge.generated.appletreesreborn.block.AppleLeavesBlock;
import cn.blockforge.generated.appletreesreborn.block.ApplePlantBlock;
import cn.blockforge.generated.appletreesreborn.block.ApplePlanksBlock;
import cn.blockforge.generated.appletreesreborn.block.AppleSaplingBlock;
import cn.blockforge.generated.appletreesreborn.block.AppleSlabBlock;
import cn.blockforge.generated.appletreesreborn.block.AppleStairsBlock;
import cn.blockforge.generated.appletreesreborn.block.AppleStrippableLogBlock;
import cn.blockforge.generated.appletreesreborn.block.AppleStrippedBlock;
import cn.blockforge.generated.appletreesreborn.world.FruitType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 方块与对应物品的注册，内容对齐原 Forge 版：
 * 三种果实（苹果 / 金苹果 / 翡翠苹果）、一整套苹果木木材（原木、去皮原木、
 * 树木、去皮树木、木板、台阶、石化台阶、楼梯、栅栏、栅栏门、门、按钮、
 * 压力板、活板门）以及苹果树叶。
 */
public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(AppleTreesMod.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(AppleTreesMod.MOD_ID);

    // ------------------------------------------------------------------
    // 树苗与果实方块
    // ------------------------------------------------------------------

    public static final DeferredBlock<Block> APPLE_SAPLING = BLOCKS.register("apple_sapling",
            () -> new AppleSaplingBlock(FruitType.APPLE, saplingProperties()));
    public static final DeferredBlock<Block> GOLD_APPLE_SAPLING = BLOCKS.register("gold_apple_sapling",
            () -> new AppleSaplingBlock(FruitType.GOLDEN, saplingProperties()));
    public static final DeferredBlock<Block> EMERALD_APPLE_SAPLING = BLOCKS.register("emerald_apple_sapling",
            () -> new AppleSaplingBlock(FruitType.EMERALD, saplingProperties()));

    public static final DeferredBlock<Block> APPLE_PLANT = BLOCKS.register("apple_plant",
            () -> new ApplePlantBlock(FruitType.APPLE, plantProperties()));
    public static final DeferredBlock<Block> GOLD_APPLE_PLANT = BLOCKS.register("gold_apple_plant",
            () -> new ApplePlantBlock(FruitType.GOLDEN, plantProperties()));
    public static final DeferredBlock<Block> EMERALD_APPLE_PLANT = BLOCKS.register("emerald_apple_plant",
            () -> new ApplePlantBlock(FruitType.EMERALD, plantProperties()));

    // ------------------------------------------------------------------
    // 翡翠苹果：吃下给幸运 3 分钟 + 速度 80 秒 + 夜视 3 分钟。
    // 1.21 起原版没有 EnchantedGoldenAppleItem 了，改成普通 Item +
    // 强制附魔闪光组件，堆叠上限保持 64（和 1.20.1 版一致，原版附魔金苹果也是 64）。
    // ------------------------------------------------------------------

    private static final FoodProperties EMERALD_APPLE_FOOD = new FoodProperties.Builder()
            .nutrition(4)
            .saturationModifier(1.2F)
            .effect(() -> new MobEffectInstance(MobEffects.LUCK, 3600, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 1600, 0), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 3600, 0), 1.0F)
            .alwaysEdible()
            .build();

    public static final DeferredItem<Item> EMERALD_APPLE_ITEM = ITEMS.register("emerald_apple",
            () -> new Item(new Item.Properties()
                    .food(EMERALD_APPLE_FOOD)
                    .rarity(Rarity.EPIC)
                    .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));

    // ------------------------------------------------------------------
    // 苹果木全套
    // ------------------------------------------------------------------

    public static final DeferredBlock<Block> APPLE_LEAVES = BLOCKS.register("apple_leaves",
            () -> new AppleLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));
    // 原木和树木走 AppleStrippableLogBlock，斧头右键才会变成对应的去皮方块。
    public static final DeferredBlock<Block> APPLE_LOG = BLOCKS.register("apple_log",
            () -> new AppleStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG),
                    () -> ModBlocks.STRIPPED_APPLE_LOG.get()));
    public static final DeferredBlock<Block> STRIPPED_APPLE_LOG = BLOCKS.register("stripped_apple_log",
            () -> new AppleStrippedBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredBlock<Block> APPLE_WOOD = BLOCKS.register("apple_wood",
            () -> new AppleStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD),
                    () -> ModBlocks.STRIPPED_APPLE_WOOD.get()));
    public static final DeferredBlock<Block> STRIPPED_APPLE_WOOD = BLOCKS.register("stripped_apple_wood",
            () -> new AppleStrippedBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    public static final DeferredBlock<Block> APPLE_PLANKS = BLOCKS.register("apple_planks",
            () -> new ApplePlanksBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    // 注意：StairBlock 在 1.21 直接要 BlockState（不再收 Supplier）。
    // 台阶/楼梯的注册 supplier 在木板之后执行（DeferredRegister 按注册顺序处理），取木板没问题。
    public static final DeferredBlock<Block> APPLE_STAIRS = BLOCKS.register("apple_stairs",
            () -> new AppleStairsBlock(() -> APPLE_PLANKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredBlock<Block> APPLE_SLAB = BLOCKS.register("apple_slab",
            () -> new AppleSlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredBlock<Block> PETRIFIED_APPLE_SLAB = BLOCKS.register("petrified_apple_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PETRIFIED_OAK_SLAB)));
    public static final DeferredBlock<Block> APPLE_FENCE = BLOCKS.register("apple_fence",
            () -> new AppleFenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredBlock<Block> APPLE_FENCE_GATE = BLOCKS.register("apple_fence_gate",
            () -> new AppleFenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE),
                    WoodType.OAK));
    public static final DeferredBlock<Block> APPLE_DOOR = BLOCKS.register("apple_door",
            () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredBlock<Block> APPLE_BUTTON = BLOCKS.register("apple_button",
            () -> new ButtonBlock(BlockSetType.OAK, 30,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    // 1.21 里压力板的灵敏度并入 BlockSetType，OAK 就是原来的 EVERYTHING。
    public static final DeferredBlock<Block> APPLE_PRESSURE_PLATE = BLOCKS.register("apple_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.OAK,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredBlock<Block> APPLE_TRAPDOOR = BLOCKS.register("apple_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));

    // ------------------------------------------------------------------
    // 对应的方块物品
    // ------------------------------------------------------------------

    public static final DeferredItem<Item> APPLE_SAPLING_ITEM =
            ITEMS.register("apple_sapling", () -> new BlockItem(APPLE_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<Item> GOLD_APPLE_SAPLING_ITEM =
            ITEMS.register("gold_apple_sapling",
                    () -> new BlockItem(GOLD_APPLE_SAPLING.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> EMERALD_APPLE_SAPLING_ITEM =
            ITEMS.register("emerald_apple_sapling",
                    () -> new BlockItem(EMERALD_APPLE_SAPLING.get(), new Item.Properties().rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> APPLE_PLANT_ITEM =
            ITEMS.register("apple_plant", () -> new BlockItem(APPLE_PLANT.get(), new Item.Properties()));
    public static final DeferredItem<Item> GOLD_APPLE_PLANT_ITEM =
            ITEMS.register("gold_apple_plant",
                    () -> new BlockItem(GOLD_APPLE_PLANT.get(), new Item.Properties().rarity(Rarity.RARE)));
    public static final DeferredItem<Item> EMERALD_APPLE_PLANT_ITEM =
            ITEMS.register("emerald_apple_plant",
                    () -> new BlockItem(EMERALD_APPLE_PLANT.get(), new Item.Properties().rarity(Rarity.EPIC)));

    public static final DeferredItem<Item> APPLE_LEAVES_ITEM = blockItem(APPLE_LEAVES);
    public static final DeferredItem<Item> APPLE_LOG_ITEM = blockItem(APPLE_LOG);
    public static final DeferredItem<Item> STRIPPED_APPLE_LOG_ITEM = blockItem(STRIPPED_APPLE_LOG);
    public static final DeferredItem<Item> APPLE_WOOD_ITEM = blockItem(APPLE_WOOD);
    public static final DeferredItem<Item> STRIPPED_APPLE_WOOD_ITEM = blockItem(STRIPPED_APPLE_WOOD);
    public static final DeferredItem<Item> APPLE_PLANKS_ITEM = blockItem(APPLE_PLANKS);
    public static final DeferredItem<Item> APPLE_STAIRS_ITEM = blockItem(APPLE_STAIRS);
    public static final DeferredItem<Item> APPLE_SLAB_ITEM = blockItem(APPLE_SLAB);
    public static final DeferredItem<Item> PETRIFIED_APPLE_SLAB_ITEM = blockItem(PETRIFIED_APPLE_SLAB);
    public static final DeferredItem<Item> APPLE_FENCE_ITEM = blockItem(APPLE_FENCE);
    public static final DeferredItem<Item> APPLE_FENCE_GATE_ITEM = blockItem(APPLE_FENCE_GATE);
    public static final DeferredItem<Item> APPLE_DOOR_ITEM = blockItem(APPLE_DOOR);
    public static final DeferredItem<Item> APPLE_BUTTON_ITEM = blockItem(APPLE_BUTTON);
    public static final DeferredItem<Item> APPLE_PRESSURE_PLATE_ITEM = blockItem(APPLE_PRESSURE_PLATE);
    public static final DeferredItem<Item> APPLE_TRAPDOOR_ITEM = blockItem(APPLE_TRAPDOOR);

    private ModBlocks() {
    }

    private static DeferredItem<Item> blockItem(DeferredBlock<Block> block) {
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
