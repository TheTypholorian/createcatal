package net.xander.createcatal;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ShriekingType implements FanProcessingType {
    private static final RecipeWrapper RECIPE_WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.SCULK_SHRIEKER);
    }

    /**
     * blasting is 100, haunting is 300, smoking is 200, and splashing (prob washing) is 400
     */
    @Override
    public int getPriority() {
        return 700;
    }

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        RECIPE_WRAPPER.setItem(0, stack);
        return RecipeTypes.SHRIEKING.find(RECIPE_WRAPPER, level).isPresent();
    }

    @Override
    @Nullable
    public List<ItemStack> process(ItemStack stack, Level level) {
        RECIPE_WRAPPER.setItem(0, stack);
        Optional<ShriekingRecipe> recipe = RecipeTypes.SHRIEKING.find(RECIPE_WRAPPER, level);
        return recipe.map(r -> RecipeApplier.applyRecipeOn(level, stack, r)).orElse(null);
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(8) != 0)
            return;
        level.addParticle(ParticleTypes.SONIC_BOOM, pos.x, pos.y + .25f, pos.z, 0, 1 / 16f, 0);
    }

    @Override
    public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
        particleAccess.setColor(Color.mixColors(new Color(41, 223, 235), new Color(13, 18, 23), random.nextFloat()).getRGB());
        particleAccess.setAlpha(1);
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(ParticleTypes.SCULK_SOUL, .25f);
        if (random.nextFloat() < 1 / 16f)
            particleAccess.spawnExtraParticle(ParticleTypes.SCULK_CHARGE_POP, .25f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        if (entity instanceof LivingEntity l) {
            l.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20, 1, false, false)); // Frightens entity for 5 seconds
        }
    }
}