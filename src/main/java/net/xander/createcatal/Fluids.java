package net.xander.createcatal;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Fluids {
    public static final DeferredRegister<Fluid> REGISTRY = DeferredRegister.create(ForgeRegistries.FLUIDS, CreateCatal.MODID);
    public static final RegistryObject<FlowingFluid> MILK = REGISTRY.register("milk", MilkFluid.Source::new);
    public static final RegistryObject<FlowingFluid> FLOWING_MILK = REGISTRY.register("flowing_milk", MilkFluid.Flowing::new);

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSideHandler {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(MILK.get(), RenderType.translucent());
            ItemBlockRenderTypes.setRenderLayer(FLOWING_MILK.get(), RenderType.translucent());
        }
    }

    public static final DeferredRegister<FluidType> TYPE_REGISTRY = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, CreateCatal.MODID);
    public static final RegistryObject<FluidType> MILK_TYPE = TYPE_REGISTRY.register("milk", MilkFluidType::new);

    public static abstract class MilkFluid extends ForgeFlowingFluid {
        public static final ForgeFlowingFluid.Properties PROPERTIES = new ForgeFlowingFluid.Properties(MILK_TYPE, MILK, FLOWING_MILK)
                .explosionResistance(100f).bucket(() -> Items.MILK_BUCKET).block(() -> (LiquidBlock) Blocks.MILK.get());

        private MilkFluid() {
            super(PROPERTIES);
        }

        public static class Source extends MilkFluid {
            public int getAmount(@NotNull FluidState state) {
                return 8;
            }

            public boolean isSource(@NotNull FluidState state) {
                return true;
            }
        }

        public static class Flowing extends MilkFluid {
            protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
                super.createFluidStateDefinition(builder);
                builder.add(LEVEL);
            }

            public int getAmount(FluidState state) {
                return state.getValue(LEVEL);
            }

            public boolean isSource(@NotNull FluidState state) {
                return false;
            }
        }
    }

    public static class MilkFluidType extends FluidType {
        public MilkFluidType() {
            super(FluidType.Properties.create().fallDistanceModifier(0F).canExtinguish(true).supportsBoating(true).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                    .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH));
        }

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
            consumer.accept(new IClientFluidTypeExtensions() {
                private static final ResourceLocation STILL_TEXTURE = new ResourceLocation("createcatal:block/milk_still"), FLOWING_TEXTURE = new ResourceLocation("createcatal:block/milk_flow");

                @Override
                public ResourceLocation getStillTexture() {
                    return STILL_TEXTURE;
                }

                @Override
                public ResourceLocation getFlowingTexture() {
                    return FLOWING_TEXTURE;
                }
            });
        }
    }
}
