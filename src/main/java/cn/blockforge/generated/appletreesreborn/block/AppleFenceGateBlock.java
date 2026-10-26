package cn.blockforge.generated.appletreesreborn.block;

import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;

/**
 * 苹果木栅栏门：可燃（数值同橡木栅栏门）。
 */
public class AppleFenceGateBlock extends FenceGateBlock implements Flammability.Planks {

    public AppleFenceGateBlock(BlockBehaviour.Properties properties, WoodType woodType) {
        super(properties, woodType);
    }
}
