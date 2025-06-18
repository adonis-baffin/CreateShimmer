package com.adonis.createshimmer.data;

import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.common.registry.CSFluids;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class CSFluidTagProvider extends FluidTagsProvider {
    public CSFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CSCommon.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 将微光流体添加到鼓风机转换催化剂标签中
        this.tag(CSFluids.MOD_TAGS.fanTransmutationCatalysts)
                .add(CSFluids.SHIMMER.getSource()) // 微光源流体
                .add(CSFluids.SHIMMER.get()); // 微光流动流体
    }
}
