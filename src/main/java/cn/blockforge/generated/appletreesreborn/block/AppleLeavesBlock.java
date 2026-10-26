package cn.blockforge.generated.appletreesreborn.block;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * 苹果树叶：可燃（数值同橡木树叶），也能丢进堆肥桶。
 * 堆肥概率在 {@code AppleTreesMod#commonSetup} 里统一登记。
 */
public class AppleLeavesBlock extends LeavesBlock implements Flammability.Leaves {

    public AppleLeavesBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}
