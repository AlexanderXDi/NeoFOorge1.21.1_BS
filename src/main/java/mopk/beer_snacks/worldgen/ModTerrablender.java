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
        // Регистрируем регион. Убедись, что RegionType.OVERWORLD не горит красным.
        Regions.register(new Region(ResourceLocation.fromNamespaceAndPath(Beer_snacks.MODID, "overworld"), RegionType.OVERWORLD, 5) {

            @Override
            public void addBiomes(net.minecraft.core.Registry<net.minecraft.world.level.biome.Biome> registry,
                                  java.util.function.Consumer<com.mojang.datafixers.util.Pair<net.minecraft.world.level.biome.Climate.ParameterPoint, net.minecraft.resources.ResourceKey<net.minecraft.world.level.biome.Biome>>> mapper) {

                // Используем встроенный в TerraBlender метод для модификации ванильного мира
                this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
                    // Заменяем обычный пляж на наш пальмовый пляж
                    builder.replaceBiome(net.minecraft.world.level.biome.Biomes.BEACH,
                            net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.BIOME,
                                    ResourceLocation.fromNamespaceAndPath(Beer_snacks.MODID, "palm_beach")));
                });
            }
        });
    }
}