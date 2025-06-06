package io.github.edwinmindcraft.origins.data.generator;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.registry.ModBlocks;
import io.github.edwinmindcraft.origins.data.tag.OriginsBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class OriginsBlockTagProvider extends BlockTagsProvider {
	public OriginsBlockTagProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), lookupProvider, Origins.MODID, existingFileHelper);
	}

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(OriginsBlockTags.COBWEBS).add(Blocks.COBWEB).add(ModBlocks.TEMPORARY_COBWEB.get());
    }
}
