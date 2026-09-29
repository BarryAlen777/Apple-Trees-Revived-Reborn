package cn.blockforge.generated.appletreesreborn.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

import java.util.HashMap;
import java.util.Map;

/**
 * 模组的可配置项（生成率、掉落率、食物效果、是否自然生成等）。
 * 全部走 Forge 的 COMMON 配置，改完在 config/appletreesreborn-common.toml 里能直接看到。
 */
public final class AppleConfig {

    public static final ForgeConfigSpec SPEC;

    // ---- 世界生成 ----
    public static final BooleanValue NATURAL_GENERATION;
    public static final DoubleValue SPAWN_CHANCE;
    public static final BooleanValue REQUIRE_SOLID_GROUND;

    // ---- 生长与掉落 ----
    public static final IntValue SAPLING_GROW_CHANCE;
    public static final DoubleValue FRUIT_GROWTH_SPEED;
    public static final IntValue MAX_FRUITS_SMALL_TREE;
    public static final IntValue MAX_FRUITS_BIG_TREE;
    public static final IntValue FRUIT_DROP_COUNT;
    public static final BooleanValue EASY_HARVEST;
    public static final BooleanValue NATURAL_FALL;
    public static final IntValue NATURAL_FALL_CHANCE;

    // ---- 食用效果 ----
    public static final BooleanValue FOOD_EFFECTS;
    public static final IntValue APPLE_REGEN_SECONDS;
    public static final IntValue GOLDEN_APPLE_REGEN_SECONDS;
    public static final IntValue APPLE_EFFECT_AMPLIFIER;
    public static final IntValue GOLDEN_APPLE_EFFECT_AMPLIFIER;
    public static final BooleanValue GOLDEN_APPLE_ABSORPTION;

    // ---- 配方 ----
    public static final BooleanValue CRAFT_APPLE_SAPLING;
    public static final BooleanValue CRAFT_GOLD_APPLE_SAPLING;
    public static final BooleanValue CRAFT_EMERALD_SAPLING;
    public static final BooleanValue CRAFT_NOTCH_APPLE;

    /** 配方 JSON 里引用的开关，键名要和 data/.../recipes 里的 key 一致。 */
    public static final Map<String, BooleanValue> CONDITION_KEYS = new HashMap<>();

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.comment("自然生成设置 / Natural world generation").push("worldgen");
        NATURAL_GENERATION = b.comment("是否让苹果树自然生成在世界里。")
                .define("naturalGeneration", true);
        SPAWN_CHANCE = b.comment("每次生成尝试真正长出苹果树的概率（0~1）。",
                        "默认值已调低，苹果树比以前稀疏很多；想要成片树林就把这个值调大。")
                .defineInRange("spawnChance", 0.05D, 0.0D, 1.0D);
        REQUIRE_SOLID_GROUND = b.comment("只在成片的地面上长树：脚下两格必须是实心的，四周至少两格有地。",
                        "开着可以避免树长在悬崖尖、孤零零的土柱上，看起来像悬空。",
                        "关掉之后树会更多，但崖边可能出现半悬空的树。")
                .define("requireSolidGround", true);
        b.pop();

        b.comment("生长与掉落 / Growth and drops").push("growth");
        SAPLING_GROW_CHANCE = b.comment("树苗每个随机刻生长的概率为 1/N；原版是 7，越小长得越快。")
                .defineInRange("saplingGrowChance", 3, 1, 200);
        FRUIT_GROWTH_SPEED = b.comment("苹果成熟速度倍率；1.0 是原版速度，越大越快。")
                .defineInRange("fruitGrowthSpeed", 1.5D, 0.1D, 20.0D);
        MAX_FRUITS_SMALL_TREE = b.comment("小苹果树一次最多结几个苹果（原版 7）。")
                .defineInRange("maxFruitsSmallTree", 10, 0, 64);
        MAX_FRUITS_BIG_TREE = b.comment("大苹果树一次最多结几个苹果（原版 12）。")
                .defineInRange("maxFruitsBigTree", 16, 0, 64);
        FRUIT_DROP_COUNT = b.comment("采下一个成熟苹果时掉落几个（原版 1）。")
                .defineInRange("fruitDropCount", 2, 1, 8);
        EASY_HARVEST = b.comment("空手右键成熟的苹果即可采摘。")
                .define("easyHarvest", true);
        NATURAL_FALL = b.comment("成熟的苹果会自己从树叶上掉下来。")
                .define("naturalFall", true);
        NATURAL_FALL_CHANCE = b.comment("自然掉落的概率为 1/N；数值越小掉得越快。")
                .defineInRange("naturalFallChance", 8, 1, 1000);
        b.pop();

        b.comment("食用效果 / Eating effects").push("effects");
        FOOD_EFFECTS = b.comment("吃苹果时附加增益效果，让生存更轻松。")
                .define("enableFoodEffects", true);
        APPLE_REGEN_SECONDS = b.comment("吃苹果获得生命恢复的秒数；0 表示不给。")
                .defineInRange("appleRegenSeconds", 4, 0, 3600);
        GOLDEN_APPLE_REGEN_SECONDS = b.comment("吃金苹果获得生命恢复的秒数；0 表示不给。")
                .defineInRange("goldenAppleRegenSeconds", 16, 0, 3600);
        APPLE_EFFECT_AMPLIFIER = b.comment("苹果的效果等级；0 = 生命恢复 I。")
                .defineInRange("appleEffectAmplifier", 0, 0, 4);
        GOLDEN_APPLE_EFFECT_AMPLIFIER = b.comment("金苹果的效果等级；0 = 生命恢复 I。")
                .defineInRange("goldenAppleEffectAmplifier", 1, 0, 4);
        GOLDEN_APPLE_ABSORPTION = b.comment("金苹果额外给予伤害吸收。")
                .define("goldenAppleAbsorption", true);
        b.pop();

        b.comment("配方开关 / Recipe toggles").push("recipes");
        CRAFT_APPLE_SAPLING = b.comment("启用配方：橡树树苗 + 苹果 = 苹果树苗。")
                .define("craftAppleSapling", true);
        CRAFT_GOLD_APPLE_SAPLING = b.comment("启用配方：金锭围绕苹果树苗 = 金苹果树苗。")
                .define("craftGoldAppleSapling", true);
        CRAFT_EMERALD_SAPLING = b.comment("启用配方：绿宝石块围绕金苹果树苗 = 翡翠苹果树苗。")
                .define("craftEmeraldSapling", true);
        CRAFT_NOTCH_APPLE = b.comment("启用配方：金块围绕苹果 = 附魔金苹果。",
                        "默认开启，关掉之后这个配方就不会被数据包加载。",
                        "注意：配方是在进入世界／重载数据包时读取的，改完这个开关要退出重进世界（或按 F3+T）才生效。")
                .define("craftNotchApple", true);
        b.pop();

        SPEC = b.build();

        // 必须在所有配置值创建之后登记，条件求值时从这里取开关。
        CONDITION_KEYS.put("craftAppleSapling", CRAFT_APPLE_SAPLING);
        CONDITION_KEYS.put("craftGoldAppleSapling", CRAFT_GOLD_APPLE_SAPLING);
        CONDITION_KEYS.put("craftEmeraldSapling", CRAFT_EMERALD_SAPLING);
        CONDITION_KEYS.put("craftNotchApple", CRAFT_NOTCH_APPLE);
    }

    private AppleConfig() {
    }
}
