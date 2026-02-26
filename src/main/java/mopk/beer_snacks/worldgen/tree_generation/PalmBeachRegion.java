package mopk.beer_snacks.worldgen.tree_generation;

import com.mojang.datafixers.util.Pair;
import mopk.beer_snacks.worldgen.ModBiomes;
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
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            // Создаем точку параметров климата
            Climate.ParameterPoint parameters = Climate.parameters(
                    0.95F,   // Temperature
                    0.8F,    // Humidity
                    -0.11F,  // Continentalness (берег)
                    0.1F,    // Erosion
                    0.0F,    // Depth
                    0.0F,    // Weirdness
                    0.0F     // Offset
            );

            // ВАЖНО: Добавляем 'mapper' первым аргументом, как показано на вашем скриншоте
            addBiome(mapper, parameters, ModBiomes.PALM_BEACH);
        });
    }

}
