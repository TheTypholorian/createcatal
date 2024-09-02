package net.xander.createcatal;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class RootingType implements FanProcessingType {
    private static final RecipeWrapper RECIPE_WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

    public static Color createParticleColor(RandomSource random) {
        if (random.nextInt(2) == 0) {
            return Color.mixColors(new Color(158, 80, 136), new Color(208, 123, 227), random.nextFloat());
        } else {
            return Color.mixColors(new Color(80, 105, 44), new Color(112, 146, 45), random.nextFloat());
        }
    }

    public static DustParticleOptions createParticle(RandomSource random) {
        return new DustParticleOptions(createParticleColor(random).asVectorF(), random.nextFloat());
    }

    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(Blocks.AZALEA) || state.is(Blocks.FLOWERING_AZALEA);
    }

    /**
     * blasting is 100, haunting is 300, smoking is 200, and splashing (prob washing) is 400
     */
    @Override
    public int getPriority() {
        return 1100;
    }

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        RECIPE_WRAPPER.setItem(0, stack);
        return RecipeTypes.ROOTING.find(RECIPE_WRAPPER, level).isPresent();
    }

    @Override
    @Nullable
    public List<ItemStack> process(ItemStack stack, Level level) {
        RECIPE_WRAPPER.setItem(0, stack);
        Optional<ShriekingRecipe> recipe = RecipeTypes.ROOTING.find(RECIPE_WRAPPER, level);
        return recipe.map(r -> RecipeApplier.applyRecipeOn(level, stack, r)).orElse(null);
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(8) != 0)
            return;
        level.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, pos.x, pos.y + .25f, pos.z, 0, 1 / 16f, 0);
    }

    @Override
    public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
        particleAccess.setColor(createParticleColor(random).getRGB());
        particleAccess.setAlpha(1);
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(ParticleTypes.CHERRY_LEAVES, .25f);
        if (random.nextFloat() < 1 / 16f)
            particleAccess.spawnExtraParticle(ParticleTypes.SPORE_BLOSSOM_AIR, .25f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        if (entity instanceof LivingEntity l) {
            l.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, 1, false, false)); // Relaxes entity for 5 seconds
            l.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1, false, false));
        }
    }
}