package mopk.beer_snacks.worldgen;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.VerticalAnchor;

public class ModSurfaceRules {
    private static final SurfaceRules.RuleSource SAND = SurfaceRules.state(Blocks.SAND.defaultBlockState());
    private static final SurfaceRules.RuleSource GRAVEL = SurfaceRules.state(Blocks.GRAVEL.defaultBlockState());
    private static final SurfaceRules.RuleSource SANDSTONE = SurfaceRules.state(Blocks.SANDSTONE.defaultBlockState());

    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.ConditionSource isPalmBeach = SurfaceRules.isBiome(ModBiomes.PALM_BEACH);

        SurfaceRules.RuleSource surfaceMix = SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(Noises.PATCH, 0.4D), GRAVEL),
                SAND
        );

        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(isPalmBeach,
                        SurfaceRules.sequence(
                                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR,
                                        SurfaceRules.sequence(
                                                SurfaceRules.ifTrue(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 0),
                                                        surfaceMix
                                                )
                                        )
                                ),

                                SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR,
                                        SurfaceRules.ifTrue(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(61), 0),
                                                SANDSTONE
                                        )
                                )
                        )
                )
        );
    }

}