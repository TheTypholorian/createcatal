package net.xander.createcatal;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Blocks {
    public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, CreateCatal.MODID);
    public static final RegistryObject<Block> MILK = REGISTRY.register("milk", () -> new LiquidBlock(Fluids.MILK, BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WATER)));
}
