package com.adonis.createshimmer.compat.curios.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import com.adonis.createshimmer.common.CSCommon;
import com.adonis.createshimmer.client.model.CSModelLayers;
import com.adonis.createshimmer.compat.curios.model.ShimmerCharmNecklaceModel;

public class ShimmerCharmNecklaceRenderer implements ICurioRenderer {
    private final ShimmerCharmNecklaceModel model;
    private final int necklaceColor;

    public ShimmerCharmNecklaceRenderer(int necklaceColor) {
        this.model = new ShimmerCharmNecklaceModel(Minecraft.getInstance().getEntityModels().bakeLayer(CSModelLayers.CHARM_OF_SHIMMER));
        this.necklaceColor = necklaceColor;
    }

    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack item, SlotContext slotContext, PoseStack stack, RenderLayerParent<T, M> parent, MultiBufferSource buffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        EntityModel var14 = parent.getModel();
        if (var14 instanceof HumanoidModel<?> model) {
            stack.pushPose();
            model.body.translateAndRotate(stack);
            stack.translate(0.0, 0.23, -0.135);
            stack.mulPose(Axis.YP.rotationDegrees(0.0F));
            stack.scale(-0.4F, -0.4F, 0.4F);
            ItemInHandRenderer renderer = new ItemInHandRenderer(Minecraft.getInstance(), Minecraft.getInstance().getEntityRenderDispatcher(), Minecraft.getInstance().getItemRenderer());
            renderer.renderItem(slotContext.entity(), item, ItemDisplayContext.FIXED, false, stack, buffer, light);
            stack.popPose();
        }

        this.model.setupAnim(slotContext.entity(), limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.model.prepareMobModel(slotContext.entity(), limbSwing, limbSwingAmount, partialTicks);
        ICurioRenderer.followBodyRotations(slotContext.entity(), this.model);
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(CSCommon.asResource("textures/models/charm_of_shimmer_necklace.png")));
        this.model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, this.necklaceColor);
    }
}