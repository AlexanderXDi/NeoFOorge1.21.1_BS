package mopk.beer_snacks.tree_generation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mopk.beer_snacks.Beer_snacks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static mopk.beer_snacks.Beer_snacks.MODID;

public class PalmTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<PalmTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(i -> trunkPlacerParts(i).apply(i, PalmTrunkPlacer::new));

    public static final DeferredRegister<TrunkPlacerType<?>> TRUNKS = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, MODID);
    public static final Supplier<TrunkPlacerType<PalmTrunkPlacer>> PALM_TRUNK = TRUNKS.register("palm_trunk", () -> new TrunkPlacerType<>(PalmTrunkPlacer.CODEC));

    public PalmTrunkPlacer(int base, int rA, int rB) { super(base, rA, rB); }

    @Override protected TrunkPlacerType<?> type() { return PALM_TRUNK.get(); }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> setter, RandomSource random, int height, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, setter, random, pos.below(), config);
        BlockPos current = pos;
        Direction leanDir = Direction.from2DDataValue(random.nextInt(4));

        int firstBendAt = 2 + random.nextInt(2);

        for (int i = 0; i < height; i++) {
            placeLog(level, setter, random, current, config);

            if (i < height - 1) {
                if (i == firstBendAt) {
                    BlockPos bridgePos = current.relative(leanDir);
                    placeLog(level, setter, random, bridgePos, config);
                    current = bridgePos;
                }
                current = current.above();
            }
        }
        return List.of(new FoliagePlacer.FoliageAttachment(current, 0, false));
    }
}
