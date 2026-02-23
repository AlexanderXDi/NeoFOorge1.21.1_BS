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
        // Определяем центральную точку шапки (позиция из TrunkPlacer + смещение из JSON)
        BlockPos center = attach.pos().above(offset);

        // Ставим самый центральный блок листвы (макушка)
        setter.set(center, config.foliageProvider.getState(random, center));

        //3 на 3
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setter.set(center.offset(x, 0, z), config.foliageProvider.getState(random, center));
            }
        }

        // Проходимся циклом по 4 направлениям (Север, Юг, Восток, Запад) для создания креста
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int currentYOffset = 0; // Текущее смещение вниз для этой ветки

            // Рандомим, сколько раз ветка "нырнет" вниз: 1 или 2 раза
            int totalSteps = 1 + random.nextInt(2);

            // Строим ветку от центра до края (заданного радиусом)
            for (int i = 1; i <= radius; i++) {
                int targetYOffset = 0; // Целевая высота для текущего блока i

                // Логика определения высоты блока в зависимости от количества ступенек
                if (totalSteps == 1) {
                    // Если ступенька одна — опускаем только самый конец ветки
                    if (i >= radius - 1) targetYOffset = 1;
                } else {
                    // Если две ступеньки — опускаем в середине и еще раз в конце
                    if (i >= radius) targetYOffset = 2; // Конец ветки на 2 блока ниже
                    else if (i >= radius - 2) targetYOffset = 1; // Середина на 1 блок ниже
                }

                // ЛОГИКА МОСТИКА: Если на этом шаге высота падает, ставим соединительный блок
                // Это нужно, чтобы листва касалась гранями и не исчезала (Anti-Decay)
                if (targetYOffset > currentYOffset) {
                    // Ставим блок на старой высоте, но на новой дистанции i
                    BlockPos bridgePos = center.relative(dir, i).below(currentYOffset);
                    setter.set(bridgePos, config.foliageProvider.getState(random, bridgePos));
                }

                // Ставим основной блок листвы текущей ветки
                BlockPos leafPos = center.relative(dir, i).below(targetYOffset);
                setter.set(leafPos, config.foliageProvider.getState(random, leafPos));

                // Запоминаем текущую высоту для следующей итерации (чтобы знать, когда ставить мостик)
                currentYOffset = targetYOffset;
            }
        }
    }

    @Override public int foliageHeight(RandomSource random, int height, TreeConfiguration config) { return 0; }
    @Override protected boolean shouldSkipLocation(RandomSource random, int x, int y, int z, int rad, boolean giant) { return false; }
}
