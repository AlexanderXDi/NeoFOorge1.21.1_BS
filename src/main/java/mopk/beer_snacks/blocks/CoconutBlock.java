package mopk.beer_snacks.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;


public class CoconutBlock extends FallingBlock {
    public static final MapCodec<CoconutBlock> CODEC = simpleCodec(CoconutBlock::new);

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    public static final IntegerProperty HITS = IntegerProperty.create("hits", 0, 2);

    public CoconutBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HITS, 0));
    }

    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            int currentHits = state.getValue(HITS);

            if (currentHits < 2) {
                // Если ударов меньше 2, просто увеличиваем счетчик
                level.setBlock(pos, state.setValue(HITS, currentHits + 1), 3);
                // Добавим звук удара для сочности
                level.playSound(null, pos, SoundEvents.BAMBOO_WOOD_HIT, SoundSource.BLOCKS, 1.0f, 1.0f);
            } else {
                // На 3-й удар (когда в блоке уже было 2) — ломаем
                level.destroyBlock(pos, true);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        // Обязательно регистрируем свойство в состоянии блока
        builder.add(HITS);
    }

    @Override
    protected void falling(FallingBlockEntity entity) {
        // 2.0f — это 1 сердце за блок, 40 — предел урона (20 сердец)
        entity.setHurtsEntities(2.0f, 40);
    }

    private void checkCoconutFall(Level level, BlockPos pos) {
        // 1. Условие: Сверху воздух?
        if (level.getBlockState(pos.above()).isAir() && !level.isClientSide) {

            // 2. Действие: Превращаемся в падающий объект!
            FallingBlockEntity entity = FallingBlockEntity.fall(level, pos, level.getBlockState(pos));

            // 3. Доп. эффект: Включаем урон по мобам
            this.falling(entity);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        level.scheduleTick(pos, this, 1);
    }
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Выполняем проверку падения через 1 тик после появления
        this.checkCoconutFall(level, pos);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide && level.getBlockState(pos.above()).isAir()) {
            FallingBlockEntity entity = FallingBlockEntity.fall(level, pos, level.getBlockState(pos));
            this.falling(entity);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState aboveState = level.getBlockState(pos.above());

        if (aboveState.is(BlockTags.AIR) != true && aboveState.is(BlockTags.AIR) != true) {
            return true;
        }
        return false;
    }
}
