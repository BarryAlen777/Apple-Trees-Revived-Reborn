package cn.blockforge.generated.appletreesreborn.world;

import cn.blockforge.generated.appletreesreborn.registry.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

/**
 * 果实的三个品种：普通苹果、金苹果、翡翠苹果（v2.1 起的新内容）。
 * 对应 1.19.4 参考版的 TreeType（APPLE / GOLDEN / EMERALD）。
 */
public enum FruitType implements StringRepresentable {
    APPLE("apple", () -> Items.APPLE, 1.0D),
    GOLDEN("gold_apple", () -> Items.GOLDEN_APPLE, 0.15D),
    EMERALD("emerald_apple", () -> ModBlocks.EMERALD_APPLE_ITEM.get(), 0.10D);

    private final String name;
    private final Supplier<Item> fruit;
    /** 自然生成权重：苹果树最常见，金树和翡翠树稀有。 */
    private final double naturalWeight;

    FruitType(String name, Supplier<Item> fruit, double naturalWeight) {
        this.name = name;
        this.fruit = fruit;
        this.naturalWeight = naturalWeight;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public Item getFruit() {
        return this.fruit.get();
    }

    public double getNaturalWeight() {
        return this.naturalWeight;
    }

    public static final Codec<FruitType> CODEC =
            StringRepresentable.fromEnum(FruitType::values);
}
