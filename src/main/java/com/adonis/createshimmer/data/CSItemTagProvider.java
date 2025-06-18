package com.adonis.createshimmer.data;

import static com.simibubi.create.AllTags.commonItemTag;

import com.adonis.createshimmer.common.CSCommon;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class CSItemTagProvider extends ItemTagsProvider {

    // 自定义标签
    public static final TagKey<Item> GLOWING_BERRIES = commonItemTag("glowing_berries");
    public static final TagKey<Item> LUMINOUS_SUBSTANCES = commonItemTag("luminous_substances");

    public CSItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            CompletableFuture<TagLookup<Block>> blockTagProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, CSCommon.ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 发光浆果标签
        this.tag(GLOWING_BERRIES)
                .add(Items.GLOW_BERRIES) // 原版发光浆果
                .addOptional(ResourceLocation.fromNamespaceAndPath("twilightforest", "torchberries")); // 暮色森林火炬浆果

        // 荧光物质标签
        this.tag(LUMINOUS_SUBSTANCES)
                .add(Items.GLOWSTONE_DUST) // 萤石粉
                .add(Items.GLOW_INK_SAC) // 发光墨囊
                .addOptional(ResourceLocation.fromNamespaceAndPath("twilightforest", "firefly")) // 暮色森林萤火虫
                .addOptional(ResourceLocation.fromNamespaceAndPath("twilightforest", "mushgloom")); // 暮色森林荧光蘑菇
    }
}
