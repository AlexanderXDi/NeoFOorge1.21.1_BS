package mopk.beer_snacks.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {
    public static final ResourceKey<Biome> PALM_BEACH = ResourceKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath("beer_snacks", "palm_beach")
    );
}
