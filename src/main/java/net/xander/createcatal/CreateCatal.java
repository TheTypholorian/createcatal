package net.xander.createcatal;

import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingTypeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CreateCatal.MODID)
public class CreateCatal {
    public static final String MODID = "createcatal";

    public static final StickingType STICKING = register("sticking", new StickingType());
    public static final SavoringType SAVORING = register("savoring", new SavoringType());
    public static final ShriekingType SHRIEKING = register("shrieking", new ShriekingType());
    public static final RoaringType ROARING = register("roaring", new RoaringType());
    public static final SinningType SINNING = register("sinning", new SinningType());
    public static final CalcifyingType CALCIFYING = register("calcifying", new CalcifyingType());

    private static <T extends FanProcessingType> T register(String id, T type) {
        FanProcessingTypeRegistry.register(asResource(id), type);
        return type;
    }

    public CreateCatal() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        RecipeTypes.register(bus);
        Blocks.REGISTRY.register(bus);
        Fluids.REGISTRY.register(bus);
        Fluids.TYPE_REGISTRY.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
