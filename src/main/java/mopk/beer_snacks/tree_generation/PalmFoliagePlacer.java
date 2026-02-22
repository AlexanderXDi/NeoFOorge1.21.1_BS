package mopk.beer_snacks.tree_generation;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PalmFoliagePlacer {

    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, "beer_snacks");

    public static final Supplier<FoliagePlacerType<PalmFoliagePlacer>> PALM_FOLIAGE = FOLIAGE.register("palm_foliage_placer", () -> new FoliagePlacerType<>(PalmFoliagePlacer.CODEC));


}
