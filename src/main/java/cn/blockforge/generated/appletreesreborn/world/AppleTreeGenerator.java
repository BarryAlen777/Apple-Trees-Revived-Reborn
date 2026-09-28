package cn.blockforge.generated.appletreesreborn.world;

import cn.blockforge.generated.appletreesreborn.block.ApplePlantBlock;
import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import cn.blockforge.generated.appletreesreborn.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 苹果树的生成算法，移植自 1.12.2 的 AppleTreeGen / BigAppleTreeGen。
 * 树干和树叶用原版橡木（和原模组一致），果实是模组自己的苹果方块。
 */
public final class AppleTreeGenerator {

    private static final BlockState LOG = Blocks.OAK_LOG.defaultBlockState();
    /**
     * 普通（非永久）橡木树叶。原版只要树叶的 distance 变成 7 就会自然消失，
     * 所以树干被砍掉后树叶会跟着掉，不会一直挂在天上。
     */
    private static final BlockState LEAVES = Blocks.OAK_LEAVES.defaultBlockState();

    private AppleTreeGenerator() {
    }

    /**
     * 长一棵苹果树。1/10 概率是大树，其余是小树（对应原版 1.12.2 的随机分支）。
     *
     * @param natural true = 自然生成（果实从半熟开始），false = 树苗长成（果实从 0 开始）
     * @param worldGen true = 世界生成阶段（用更温和的更新标志）
     */
    public static boolean growTree(LevelAccessor level, RandomSource random, BlockPos pos,
                                   FruitType fruit, boolean natural, boolean worldGen) {
        // 兜底落地：正常情况调用方给的位置脚下就是土；万一不是（会悬空），
        // 就往下找一块真正的地面，找不到这棵树就不长。
        BlockPos base = isSoil(level.getBlockState(pos.below())) ? pos : findGroundBase(level, pos);
        if (base == null) {
            return false;
        }
        boolean grown = random.nextInt(10) == 0
                ? placeBig(level, random, base, fruit, natural, worldGen)
                : placeSmall(level, random, base, fruit, natural, worldGen);
        if (grown) {
            connectTrunkToGround(level, base, worldGen);
        }
        return grown;
    }

    /**
     * 兜底：从树干根部往下补，直到踩到实心方块为止。
     * 世界生成用的高度图反映的是"地形表面"，不含后来种上去的高草、花、雪。
     * 所以根部那几格常常被草占着——树干必须把它们替换掉（见 canHoldTrunk），
     * 这里再补一遍，保证树干的底端一定落地，不会再出现悬空的树。
     */
    private static void connectTrunkToGround(LevelAccessor level, BlockPos base, boolean worldGen) {
        BlockPos p = base;
        for (int i = 0; i <= GROUND_SEARCH_DEPTH; i++) {
            if (!canHoldTrunk(level.getBlockState(p))) {
                return; // 碰到树干或者地面，已经接上了
            }
            set(level, p, LOG, worldGen);
            p = p.below();
        }
    }

    /**
     * 给刚种好的树叶算好"离树干几格"（原版的 distance）。
     * 树叶是非永久的，distance=7 的树叶会被原版随机刻清掉；树干被砍断后，
     * 原版会顺着 distance 一层层往上更新，够不着的树叶自然掉落。
     * 不先手算的话，世界生成阶段放下的树叶 distance 全是 7，一进游戏就集体消失。
     */
    private static void applyLeafDistances(LevelAccessor level, List<BlockPos> leaves, boolean worldGen) {
        if (leaves.isEmpty()) {
            return;
        }
        Set<BlockPos> leafSet = new HashSet<>(leaves);
        Map<BlockPos, Integer> distance = new HashMap<>();
        Deque<BlockPos> queue = new ArrayDeque<>();
        // 贴着树干的树叶作为起点。
        for (BlockPos p : leaves) {
            for (Direction dir : Direction.values()) {
                if (level.getBlockState(p.relative(dir)).is(BlockTags.LOGS)) {
                    distance.put(p, 1);
                    queue.add(p);
                    break;
                }
            }
        }
        // 从树干往外一层层推，最多推到 6（再远就是 7 = 会掉）。
        while (!queue.isEmpty()) {
            BlockPos p = queue.poll();
            int next = distance.get(p) + 1;
            if (next >= 7) {
                continue;
            }
            for (Direction dir : Direction.values()) {
                BlockPos n = p.relative(dir);
                if (leafSet.contains(n) && !distance.containsKey(n)) {
                    distance.put(n, next);
                    queue.add(n);
                }
            }
        }
        for (BlockPos p : leaves) {
            if (!level.getBlockState(p).is(BlockTags.LEAVES)) {
                continue; // 这一格后来被树干占了，别覆盖回去
            }
            int d = Math.min(7, distance.getOrDefault(p, 7));
            set(level, p, LEAVES.setValue(LeavesBlock.DISTANCE, d), worldGen);
        }
    }

    private static void set(LevelAccessor level, BlockPos pos, BlockState state, boolean worldGen) {
        level.setBlock(pos, state, worldGen ? 2 : 3);
    }

    /**
     * 这一格能不能被树叶／树干替换掉。
     * 除了空气和树叶，高草、花、雪这类"可以替换的方块"也算数——
     * 原版树也是这么做的；不替换它们，树干底部就会被草顶掉一两格，树看起来悬空。
     * 水和岩浆不算（有液体的一律跳过），免得树长到水里。
     */
    private static boolean replaceable(BlockState state) {
        return state.getFluidState().isEmpty()
                && (state.isAir() || state.is(BlockTags.LEAVES)
                || state.is(BlockTags.REPLACEABLE_BY_TREES) || state.canBeReplaced());
    }

    // ------------------------------------------------------------------
    // 落地检查：让树一定长在地上，不会悬空
    // ------------------------------------------------------------------

    /** 向上找地面最多往下找几格。 */
    private static final int GROUND_SEARCH_DEPTH = 8;

    /** 能托住树苗的土，和原版树苗一致。 */
    public static boolean isSoil(BlockState state) {
        return state.is(BlockTags.DIRT) || state.is(Blocks.FARMLAND);
    }

    /** 实心地面：不是空气、不是液体、不是树叶，也不是草这种一踩就没的方块。 */
    private static boolean isSturdy(BlockState state) {
        return !state.isAir() && state.getFluidState().isEmpty()
                && !state.is(BlockTags.LEAVES) && !state.canBeReplaced();
    }

    /** 树干能不能摆在这一格（空气、树叶、杂草、花都能被树干替掉，水和岩浆不行）。 */
    private static boolean canHoldTrunk(BlockState state) {
        return state.getFluidState().isEmpty()
                && (state.isAir() || state.is(BlockTags.LEAVES) || state.canBeReplaced());
    }

    /** 数一数树干脚下这一层，四面有多少格是实心地面。 */
    private static int solidGroundAround(LevelAccessor level, BlockPos base) {
        BlockPos ground = base.below();
        int count = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (isSturdy(level.getBlockState(ground.relative(direction)))) {
                count++;
            }
        }
        return count;
    }

    /**
     * 从世界生成给出的位置往下找一块真正能站住脚的地面，找不到就返回 null（这棵树就不长了）。
     * 判断条件：这一格能放树干、下面一格是土、再下面一格是实心方块、四周至少两格有地。
     * 这样既不会出现"树干底下一段空气"，也不会长在悬崖尖或孤零零一根土柱上。
     */
    public static BlockPos findGroundBase(LevelAccessor level, BlockPos origin) {
        for (int depth = 0; depth <= GROUND_SEARCH_DEPTH; depth++) {
            BlockPos base = origin.below(depth);
            if (base.getY() <= level.getMinBuildHeight() + 1) {
                return null;
            }
            if (!canHoldTrunk(level.getBlockState(base))) {
                continue;
            }
            if (!isSoil(level.getBlockState(base.below()))) {
                continue;
            }
            if (!isSturdy(level.getBlockState(base.below(2)))) {
                continue;
            }
            if (AppleConfig.REQUIRE_SOLID_GROUND.get() && solidGroundAround(level, base) < 2) {
                continue;
            }
            return base;
        }
        return null;
    }

    private static void placePlant(LevelAccessor level, BlockPos pos, FruitType fruit,
                                   boolean natural, RandomSource random, boolean worldGen) {
        if (!level.getBlockState(pos).isAir() && !level.getBlockState(pos).is(BlockTags.LEAVES)) {
            return;
        }
        if (!level.getBlockState(pos.above()).is(BlockTags.LEAVES)) {
            return;
        }
        Block plant = ModBlocks.plantFor(fruit);
        BlockState state = plant.defaultBlockState();
        if (natural) {
            state = state.setValue(ApplePlantBlock.AGE, 2 + random.nextInt(4));
        }
        set(level, pos, state, worldGen);
    }

    // ------------------------------------------------------------------
    // 小树（1.12.2 AbstractBaseTree + AppleTreeGen）
    // ------------------------------------------------------------------

    private static boolean placeSmall(LevelAccessor level, RandomSource random, BlockPos pos,
                                      FruitType fruit, boolean natural, boolean worldGen) {
        int height = random.nextInt(2) + 5;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        if (y < level.getMinBuildHeight() + 1 || y + height + 1 > level.getMaxBuildHeight()) {
            return false;
        }

        for (int j = y; j <= y + 1 + height; ++j) {
            int k = 1;
            if (j == y) {
                k = 0;
            }
            if (j >= y + 1 + height - 2) {
                k = 2;
            }
            for (int l = x - k; l <= x + k; ++l) {
                for (int i1 = z - k; i1 <= z + k; ++i1) {
                    if (j >= level.getMinBuildHeight() && j < level.getMaxBuildHeight()) {
                        if (!replaceable(level.getBlockState(new BlockPos(l, j, i1)))) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        BlockPos down = pos.below();
        if (!isSoil(level.getBlockState(down))) {
            return false;
        }
        if (y >= level.getMaxBuildHeight() - height - 1) {
            return false;
        }

        List<BlockPos> placedLeaves = new ArrayList<>();
        for (int i2 = y - 3 + height; i2 <= y + height; ++i2) {
            int k2 = i2 - (y + height);
            int l2 = 1 - k2 / 2;
            for (int i3 = x - l2; i3 <= x + l2; ++i3) {
                int j1 = i3 - x;
                for (int k1 = z - l2; k1 <= z + l2; ++k1) {
                    int l1 = k1 - z;
                    if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || random.nextInt(2) != 0 && k2 != 0) {
                        BlockPos leafPos = new BlockPos(i3, i2, k1);
                        BlockState existing = level.getBlockState(leafPos);
                        if (replaceable(existing)) {
                            set(level, leafPos, LEAVES, worldGen);
                            placedLeaves.add(leafPos);
                        }
                    }
                }
            }
        }

        // 关键：根部那几格可能被高草／花占着（世界生成的高度图看不到它们），
        // 这里必须用 canHoldTrunk 把它们替换成树干，否则树干底部会缺一两格，树就成了"悬空树"。
        for (int j2 = 0; j2 < height; ++j2) {
            BlockPos trunkPos = pos.above(j2);
            BlockState existing = level.getBlockState(trunkPos);
            if (canHoldTrunk(existing)) {
                set(level, trunkPos, LOG, worldGen);
            }
        }

        // 树干摆好后再算树叶的 distance，否则会被树干覆盖掉。
        applyLeafDistances(level, placedLeaves, worldGen);
        placeFruitSmall(level, random, pos.offset(0, height - 4, 0), fruit, natural, worldGen);
        return true;
    }

    private static void placeFruitSmall(LevelAccessor level, RandomSource random, BlockPos center,
                                        FruitType fruit, boolean natural, boolean worldGen) {
        List<BlockPos> locations = new ArrayList<>(25);
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                BlockPos p = center.offset(x, 0, z);
                BlockState here = level.getBlockState(p);
                if ((here.isAir() || here.is(BlockTags.LEAVES))
                        && level.getBlockState(p.above()).is(BlockTags.LEAVES)) {
                    locations.add(p);
                }
            }
        }
        if (locations.isEmpty()) {
            return;
        }
        Collections.shuffle(locations, new java.util.Random(random.nextLong()));
        int max = AppleConfig.MAX_FRUITS_SMALL_TREE.get();
        int placed = 0;
        for (int i = 0; i < locations.size() && placed < max; i++) {
            if (i < 2 || random.nextInt(5) == 0) {
                placePlant(level, locations.get(i), fruit, natural, random, worldGen);
                placed++;
            }
        }
    }

    // ------------------------------------------------------------------
    // 大树（1.12.2 BigAppleTreeGen，原版 WorldGenBigTree 的移植）
    // ------------------------------------------------------------------

    private static final int HEIGHT_LIMIT_LIMIT = 12;
    private static final double HEIGHT_ATTENUATION = 0.618D;
    private static final double BRANCH_SLOPE = 0.381D;
    private static final double SCALE_WIDTH = 1.0D;
    private static final double LEAF_DENSITY = 1.0D;
    private static final int LEAF_DISTANCE_LIMIT = 4;

    private static boolean placeBig(LevelAccessor level, RandomSource random, BlockPos pos,
                                    FruitType fruit, boolean natural, boolean worldGen) {
        return new BigTree(level, random, pos, fruit, natural, worldGen).generate();
    }

    private static final class FoliageCoordinates {
        final BlockPos pos;
        final int branchBase;

        FoliageCoordinates(BlockPos pos, int branchBase) {
            this.pos = pos;
            this.branchBase = branchBase;
        }
    }

    private static final class BigTree {
        private final LevelAccessor level;
        private final RandomSource rand;
        private final BlockPos basePos;
        private final FruitType fruit;
        private final boolean natural;
        private final boolean worldGen;

        private int heightLimit;
        private int height;
        private final List<FoliageCoordinates> foliageCoords = new ArrayList<>();
        private final List<BlockPos> leavesPos = new ArrayList<>();

        BigTree(LevelAccessor level, RandomSource rand, BlockPos basePos, FruitType fruit,
                boolean natural, boolean worldGen) {
            this.level = level;
            this.rand = rand;
            this.basePos = basePos;
            this.fruit = fruit;
            this.natural = natural;
            this.worldGen = worldGen;
        }

        boolean generate() {
            this.heightLimit = 5 + this.rand.nextInt(HEIGHT_LIMIT_LIMIT);
            if (!this.validTreeLocation()) {
                return false;
            }
            this.generateLeafNodeList();
            this.generateLeaves();
            this.generateTrunk();
            this.generateLeafNodeBases();
            // 树干、树枝都摆完之后再算树叶的 distance，树干被砍掉后树叶才会正常掉落。
            AppleTreeGenerator.applyLeafDistances(this.level, this.leavesPos, this.worldGen);
            this.generateFruit();
            return true;
        }

        private void setBlock(BlockPos pos, BlockState state) {
            AppleTreeGenerator.set(this.level, pos, state, this.worldGen);
        }

        private boolean replaceable(BlockPos pos) {
            return AppleTreeGenerator.replaceable(this.level.getBlockState(pos));
        }

        private void generateLeafNodeList() {
            this.height = (int) ((double) this.heightLimit * HEIGHT_ATTENUATION);
            if (this.height >= this.heightLimit) {
                this.height = this.heightLimit - 1;
            }
            int i = (int) (1.382D + Math.pow(LEAF_DENSITY * (double) this.heightLimit / 13.0D, 2.0D));
            if (i < 1) {
                i = 1;
            }
            int j = this.basePos.getY() + this.height;
            int k = this.heightLimit - LEAF_DISTANCE_LIMIT;
            this.foliageCoords.add(new FoliageCoordinates(this.basePos.above(k), j));

            for (; k >= 0; --k) {
                float f = this.layerSize(k);
                if (f >= 0.0F) {
                    for (int l = 0; l < i; ++l) {
                        double d0 = SCALE_WIDTH * (double) f * ((double) this.rand.nextFloat() + 0.328D);
                        double d1 = (double) (this.rand.nextFloat() * 2.0F) * Math.PI;
                        double d2 = d0 * Math.sin(d1) + 0.5D;
                        double d3 = d0 * Math.cos(d1) + 0.5D;
                        BlockPos blockpos = this.basePos.offset(Mth.floor(d2), k - 1, Mth.floor(d3));
                        BlockPos blockpos1 = blockpos.above(LEAF_DISTANCE_LIMIT);

                        if (this.checkBlockLine(blockpos, blockpos1) == -1) {
                            int i1 = this.basePos.getX() - blockpos.getX();
                            int j1 = this.basePos.getZ() - blockpos.getZ();
                            double d4 = (double) blockpos.getY()
                                    - Math.sqrt((double) (i1 * i1 + j1 * j1)) * BRANCH_SLOPE;
                            int k1 = d4 > (double) j ? j : (int) d4;
                            BlockPos blockpos2 = new BlockPos(this.basePos.getX(), k1, this.basePos.getZ());
                            if (this.checkBlockLine(blockpos2, blockpos) == -1) {
                                this.foliageCoords.add(new FoliageCoordinates(blockpos, blockpos2.getY()));
                            }
                        }
                    }
                }
            }
        }

        private void crossSection(BlockPos pos, float nodeSize) {
            int i = (int) ((double) nodeSize + 0.618D);
            for (int j = -i; j <= i; ++j) {
                for (int k = -i; k <= i; ++k) {
                    if (Math.pow((double) Math.abs(j) + 0.5D, 2.0D)
                            + Math.pow((double) Math.abs(k) + 0.5D, 2.0D)
                            <= (double) (nodeSize * nodeSize)) {
                        BlockPos p = pos.offset(j, 0, k);
                        BlockState state = this.level.getBlockState(p);
                        if (AppleTreeGenerator.replaceable(state)) {
                            this.leavesPos.add(p);
                            this.setBlock(p, LEAVES);
                        }
                    }
                }
            }
        }

        private float layerSize(int y) {
            if ((float) y < (float) this.heightLimit * 0.3F) {
                return -1.0F;
            }
            float f = (float) this.heightLimit / 2.0F;
            float f1 = f - (float) y;
            float f2 = Mth.sqrt(f * f - f1 * f1);
            if (f1 == 0.0F) {
                f2 = f;
            } else if (Math.abs(f1) >= f) {
                return 0.0F;
            }
            return f2 * 0.5F;
        }

        private float leafSize(int y) {
            if (y >= 0 && y < LEAF_DISTANCE_LIMIT) {
                return y != 0 && y != LEAF_DISTANCE_LIMIT - 1 ? 3.0F : 2.0F;
            }
            return -1.0F;
        }

        private void generateLeafNode(BlockPos pos) {
            for (int i = 0; i < LEAF_DISTANCE_LIMIT; ++i) {
                this.crossSection(pos.above(i), this.leafSize(i));
            }
        }

        private void limb(BlockPos initPos, BlockPos finalPos) {
            BlockPos blockpos = finalPos.offset(-initPos.getX(), -initPos.getY(), -initPos.getZ());
            int i = getGreatestDistance(blockpos);
            if (i <= 0) {
                return;
            }
            float f = (float) blockpos.getX() / (float) i;
            float f1 = (float) blockpos.getY() / (float) i;
            float f2 = (float) blockpos.getZ() / (float) i;
            for (int j = 0; j <= i; ++j) {
                BlockPos p = initPos.offset(Mth.floor(0.5F + (float) j * f),
                        Mth.floor(0.5F + (float) j * f1), Mth.floor(0.5F + (float) j * f2));
                Direction.Axis axis = getLogAxis(initPos, p);
                this.setBlock(p, LOG.setValue(RotatedPillarBlock.AXIS, axis));
            }
        }

        private static int getGreatestDistance(BlockPos pos) {
            int i = Math.abs(pos.getX());
            int j = Math.abs(pos.getY());
            int k = Math.abs(pos.getZ());
            if (k > i && k > j) {
                return k;
            }
            return Math.max(j, i);
        }

        private static Direction.Axis getLogAxis(BlockPos from, BlockPos to) {
            int i = Math.abs(to.getX() - from.getX());
            int j = Math.abs(to.getZ() - from.getZ());
            int k = Math.max(i, j);
            if (k > 0) {
                return i == k ? Direction.Axis.X : Direction.Axis.Z;
            }
            return Direction.Axis.Y;
        }

        private void generateLeaves() {
            for (FoliageCoordinates coordinates : this.foliageCoords) {
                this.generateLeafNode(coordinates.pos);
            }
        }

        private boolean leafNodeNeedsBase(int y) {
            return (double) y >= (double) this.heightLimit * 0.2D;
        }

        private void generateTrunk() {
            this.limb(this.basePos, this.basePos.above(this.height));
        }

        private void generateLeafNodeBases() {
            for (FoliageCoordinates coordinates : this.foliageCoords) {
                int i = coordinates.branchBase;
                BlockPos p = new BlockPos(this.basePos.getX(), i, this.basePos.getZ());
                if (!p.equals(coordinates.pos) && this.leafNodeNeedsBase(i - this.basePos.getY())) {
                    this.limb(p, coordinates.pos);
                }
            }
        }

        private int checkBlockLine(BlockPos posOne, BlockPos posTwo) {
            BlockPos blockpos = posTwo.offset(-posOne.getX(), -posOne.getY(), -posOne.getZ());
            int i = getGreatestDistance(blockpos);
            if (i == 0) {
                return -1;
            }
            float f = (float) blockpos.getX() / (float) i;
            float f1 = (float) blockpos.getY() / (float) i;
            float f2 = (float) blockpos.getZ() / (float) i;
            for (int j = 0; j <= i; ++j) {
                BlockPos p = posOne.offset(Mth.floor(0.5F + (float) j * f),
                        Mth.floor(0.5F + (float) j * f1), Mth.floor(0.5F + (float) j * f2));
                if (!this.replaceable(p)) {
                    return j;
                }
            }
            return -1;
        }

        private void generateFruit() {
            List<BlockPos> applePos = new ArrayList<>();
            for (BlockPos leafPos : this.leavesPos) {
                if (this.level.getBlockState(leafPos.below()).isAir()) {
                    applePos.add(leafPos.below());
                }
            }
            if (applePos.isEmpty()) {
                return;
            }
            Collections.shuffle(applePos, new java.util.Random(this.rand.nextLong()));
            int max = AppleConfig.MAX_FRUITS_BIG_TREE.get();
            int placed = 0;
            for (int i = 0; i < applePos.size() && placed < max; i++) {
                if (i < 3 || this.rand.nextInt(5) == 0) {
                    placePlant(this.level, applePos.get(i), this.fruit, this.natural, this.rand, this.worldGen);
                    placed++;
                }
            }
        }

        private boolean validTreeLocation() {
            BlockPos down = this.basePos.below();
            if (!AppleTreeGenerator.isSoil(this.level.getBlockState(down))) {
                return false;
            }
            int i = this.checkBlockLine(this.basePos, this.basePos.above(this.heightLimit - 1));
            if (i == -1) {
                return true;
            }
            if (i < 6) {
                return false;
            }
            this.heightLimit = i;
            return true;
        }
    }
}
