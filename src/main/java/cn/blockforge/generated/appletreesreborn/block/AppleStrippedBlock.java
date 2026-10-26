package cn.blockforge.generated.appletreesreborn.block;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

/**
 * 去皮苹果原木／去皮苹果树木：不再能被斧头去皮，但和原木一样可燃。
 */
public class AppleStrippedBlock extends RotatedPillarBlock implements Flammability.Wood {

    public AppleStrippedBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }
}
