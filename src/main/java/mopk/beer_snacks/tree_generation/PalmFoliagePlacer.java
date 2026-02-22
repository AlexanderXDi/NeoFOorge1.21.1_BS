package mopk.beer_snacks.tree_generation;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mopk.beer_snacks.Beer_snacks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static mopk.beer_snacks.Beer_snacks.MODID;


public class PalmFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(i -> foliagePlacerParts(i).apply(i, PalmFoliagePlacer::new));

    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, MODID);
    public static final Supplier<FoliagePlacerType<PalmFoliagePlacer>> PALM_FOLIAGE = FOLIAGE.register("palm_foliage", () -> new FoliagePlacerType<>(PalmFoliagePlacer.CODEC));

    public PalmFoliagePlacer(IntProvider radius, IntProvider offset) { super(radius, offset); }

    @Override protected FoliagePlacerType<?> type() { return PALM_FOLIAGE.get(); }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter setter, RandomSource random, TreeConfiguration config, int maxFreeHeight, FoliageAttachment attach, int foliageHeight, int radius, int offset) {
        BlockPos center = attach.pos().above(offset);

        setter.set(center, config.foliageProvider.getState(random, center));

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int currentYOffset = 0;
            int totalSteps = 1 + random.nextInt(2);

            for (int i = 1; i <= radius; i++) {
                int targetYOffset = 0;

                if (totalSteps == 1) {
                    if (i >= radius - 1) targetYOffset = 1;
                } else {
                    if (i >= radius) targetYOffset = 2;
                    else if (i >= radius - 2) targetYOffset = 1;
                }

                if (targetYOffset > currentYOffset) {
                    BlockPos bridgePos = center.relative(dir, i).below(currentYOffset);
                    setter.set(bridgePos, config.foliageProvider.getState(random, bridgePos));
                }

                BlockPos leafPos = center.relative(dir, i).below(targetYOffset);
                setter.set(leafPos, config.foliageProvider.getState(random, leafPos));

                currentYOffset = targetYOffset;
            }
        }
    }

    @Override public int foliageHeight(RandomSource random, int height, TreeConfiguration config) { return 0; }
    @Override protected boolean shouldSkipLocation(RandomSource random, int x, int y, int z, int rad, boolean giant) { return false; }
}
