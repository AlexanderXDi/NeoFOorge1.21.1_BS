package mopk.beer_snacks.worldgen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;
import java.util.function.Consumer;


public class PalmBeachRegion extends Region {
    public PalmBeachRegion(ResourceLocation name, int weight) {
        super(name, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        Climate.ParameterPoint narrowBeach = Climate.parameters(
                Climate.Parameter.span(0.9F, 1.0F),   // Температура: только в очень жарких местах
                Climate.Parameter.span(0.7F, 0.8F),   // Влажность: очень специфическая влажность
                Climate.Parameter.span(-0.15F, -0.1F), // КОНТИНЕНТАЛЬНОСТЬ: Это сделает его узкой полоской у воды!
                Climate.Parameter.span(0.1F, 0.3F),   // Эрозия
                Climate.Parameter.point(0.0F),  // Глубина
                Climate.Parameter.point(0.0F),  // Странность
                0.0F                                  // Смещение
        );
        this.addBiome(mapper, narrowBeach, ModBiomes.PALM_BEACH);
    }
}