package net.xander.createcatal;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class StickingType implements FanProcessingType {
    private static final RecipeWrapper RECIPE_WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        if (!Config.ENABLE_STICKING.get()) {
            return false;
        }

        FluidState fluidState = level.getFluidState(pos);
        return fluidState.is(FluidTags.create(new ResourceLocation("forge:honey")));
    }

    /**
     * blasting is 100, haunting is 300, smoking is 200, and splashing (prob washing) is 400
     */
    @Override
    public int getPriority() {
        return 500;
    }

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        RECIPE_WRAPPER.setItem(0, stack);
        Optional<FillingRecipe> recipe = AllRecipeTypes.FILLING.find(RECIPE_WRAPPER, level);

        if (recipe.isEmpty()) {
            return false;
        }

        for (FluidStack fluid : recipe.get().getRequiredFluid().getMatchingFluidStacks()) {
            if (fluid.getFluid().isSame(AllFluids.HONEY.get())) {
                return true;
            }
        }

        return false;
    }

    @Override
    @Nullable
    public List<ItemStack> process(ItemStack stack, Level level) {
        RECIPE_WRAPPER.setItem(0, stack);
        Optional<FillingRecipe> recipe = AllRecipeTypes.FILLING.find(RECIPE_WRAPPER, level);
        return recipe.map(r -> RecipeApplier.applyRecipeOn(level, stack, r)).orElse(null);
    }

    @Override
    public void spawnProcessingParticles(Level level, Vec3 pos) {
        if (level.random.nextInt(8) != 0)
            return;
        level.addParticle(ParticleTypes.LANDING_HONEY, pos.x, pos.y + .25f, pos.z, 0, 1 / 16f, 0);
    }

    @Override
    public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
        particleAccess.setColor(Color.mixColors(new Color(233, 197, 116), new Color(229, 142, 20), random.nextFloat()).getRGB());
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(ParticleTypes.LANDING_HONEY, .25f);
        if (random.nextFloat() < 1 / 16f)
            particleAccess.spawnExtraParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.HONEY_BLOCK.defaultBlockState()), .25f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        if (entity instanceof LivingEntity l) {
            l.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1, false, false)); // Slows entity for 5 seconds
        }
    }
}