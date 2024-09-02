package net.xander.createcatal;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SinningType implements FanProcessingType {
    private static final RecipeWrapper RECIPE_WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        if (!Config.ENABLE_SINNING.get()) {
            return false;
        }

        return level.getBlockState(pos).is(Blocks.NETHER_PORTAL);
    }

    /**
     * blasting is 100, haunting is 300, smoking is 200, and splashing (prob washing) is 400
     */
    @Override
    public int getPriority() {
        return 800;
    }

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        RECIPE_WRAPPER.setItem(0, stack);
        return RecipeTypes.SINNING.find(RECIPE_WRAPPER, level).isPresent();
    }

    @Override
    @Nullable
    public List<ItemStack> process(ItemStack stack, Level level) {
        RECIPE_WRAPPER.setItem(0, stack);
        Optional<SinningRecipe> recipe = RecipeTypes.SINNING.find(RECIPE_WRAPPER, level);
        return recipe.map(r -> RecipeApplier.applyRecipeOn(level, stack, r)).orElse(null);
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(8) != 0)
            return;
        level.addParticle(ParticleTypes.PORTAL, pos.x, pos.y + .25f, pos.z, 0, 1 / 16f, 0);
    }

    @Override
    public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
        particleAccess.setColor(Color.mixColors(new Color(166, 79, 229), new Color(78, 3, 189), random.nextFloat()).getRGB());
        particleAccess.setAlpha(1);
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(ParticleTypes.CRIMSON_SPORE, .25f);
        if (random.nextFloat() < 1 / 16f)
            particleAccess.spawnExtraParticle(ParticleTypes.WARPED_SPORE, .25f);
        if (random.nextFloat() < 1 / 8f)
            particleAccess.spawnExtraParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.NETHERRACK.defaultBlockState()), .25f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        entity.setSecondsOnFire(10);
        entity.hurt(DamageSources.fanSin(level), 4);
    }
}