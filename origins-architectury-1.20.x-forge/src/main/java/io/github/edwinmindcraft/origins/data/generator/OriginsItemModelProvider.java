package io.github.edwinmindcraft.origins.data.generator;

import io.github.apace100.origins.Origins;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class OriginsItemModelProvider extends ItemModelProvider {
	public OriginsItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), Origins.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		this.singleTexture("orb_of_origin", this.mcLoc("generated"), "layer0", this.modLoc("item/orb_of_origin"));
	}
}
