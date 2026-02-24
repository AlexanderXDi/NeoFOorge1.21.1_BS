package mopk.beer_snacks.worldgen;

import com.mojang.datafixers.util.Pair;
import mopk.beer_snacks.Beer_snacks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.Regions;

import java.util.function.Consumer;

public class ModTerrablender {
    public static void registerRegions() {
        Regions.register(new Region(ResourceLocation.fromNamespaceAndPath(Beer_snacks.MODID, "overworld"), RegionType.OVERWORLD, 5) {
            @Override
            public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
                this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
                    builder.replaceBiome(Biomes.BEACH, ResourceKey.create(Registries.BIOME,
                            ResourceLocation.fromNamespaceAndPath(Beer_snacks.MODID, "palm_beach")));
                });
            }
        });
    }
}