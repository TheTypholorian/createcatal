package net.xander.createcatal;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.foundation.recipe.RecipeApplier;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SavoringType implements FanProcessingType {
    private static final RecipeWrapper RECIPE_WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

    public static DustParticleOptions createParticle(RandomSource random) {
        return new DustParticleOptions(Color.mixColors(new Color(98, 43, 52), new Color(229, 132, 93), random.nextFloat()).asVectorF(), random.nextFloat());
    }

    @Override
    public boolean isValidAt(Level level, BlockPos pos) {
        if (!Config.ENABLE_SAVORING.get()) {
            return false;
        }

        FluidState fluidState = level.getFluidState(pos);
        return fluidState.is(FluidTags.create(new ResourceLocation("forge:chocolate")));
    }

    /**
     * blasting is 100, haunting is 300, smoking is 200, and splashing (prob washing) is 400
     */
    @Override
    public int getPriority() {
        return 600;
    }

    @Override
    public boolean canProcess(ItemStack stack, Level level) {
        RECIPE_WRAPPER.setItem(0, stack);
        Optional<FillingRecipe> recipe = AllRecipeTypes.FILLING.find(RECIPE_WRAPPER, level);

        if (recipe.isEmpty()) {
            return false;
        }

        for (FluidStack fluid : recipe.get().getRequiredFluid().getMatchingFluidStacks()) {
            if (fluid.getFluid().isSame(AllFluids.CHOCOLATE.get())) {
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
        level.addParticle(createParticle(level.random), pos.x, pos.y + .25f, pos.z, 0, 1 / 16f, 0);
    }

    @Override
    public void morphAirFlow(AirFlowParticleAccess particleAccess, RandomSource random) {
        particleAccess.setColor(Color.mixColors(new Color(98, 43, 52), new Color(229, 132, 93), random.nextFloat()).getRGB());
        particleAccess.setAlpha(random.nextFloat());
        if (random.nextFloat() < 1 / 32f)
            particleAccess.spawnExtraParticle(createParticle(random), .5f);
        if (random.nextFloat() < 1 / 16f)
            particleAccess.spawnExtraParticle(createParticle(random), .5f);
    }

    @Override
    public void affectEntity(Entity entity, Level level) {
        if (entity instanceof LivingEntity l) {
            l.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, 1, false, false)); // Speeds entity for 5 seconds
        }
    }
}