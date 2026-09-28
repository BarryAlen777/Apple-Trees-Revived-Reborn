package cn.blockforge.generated.appletreesreborn.world;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.function.Supplier;

/**
 * 果实的两个品种：普通苹果和金苹果。
 * 沿用 1.12.2 源码里的 TreeType（APPLE / GOLDEN），这里只保留品种与掉落物。
 */
public enum FruitType {
    APPLE("apple", () -> Items.APPLE),
    GOLDEN("gold_apple", () -> Items.GOLDEN_APPLE);

    private final String name;
    private final Supplier<Item> fruit;

    FruitType(String name, Supplier<Item> fruit) {
        this.name = name;
        this.fruit = fruit;
    }

    public String getName() {
        return this.name;
    }

    public Item getFruit() {
        return this.fruit.get();
    }
}
