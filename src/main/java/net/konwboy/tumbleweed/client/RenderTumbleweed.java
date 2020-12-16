package net.konwboy.tumbleweed.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.konwboy.tumbleweed.Tumbleweed;
import net.konwboy.tumbleweed.common.EntityTumbleweed;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.common.model.TransformationHelper;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderTumbleweed extends EntityRenderer<EntityTumbleweed> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Tumbleweed.MOD_ID, "textures/entity/tumbleweed.png");
	private static RenderType TUMBLEWEED = ForgeRenderTypes.getUnlitTranslucent(TEXTURE, false);

	private ModelTumbleweed tumbleweed = new ModelTumbleweed(0);

	public RenderTumbleweed(EntityRendererManager manager) {
		super(manager);
		this.shadowSize = 0.4f;
		this.shadowOpaque = 0.8f;
	}

	@Override
	public void render(EntityTumbleweed entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int packedLightIn) {
		float alpha = (1f - entity.fadeProgress / (float) EntityTumbleweed.FADE_TIME) * 0.7f;
		this.shadowOpaque = alpha;
		this.shadowSize = entity.getWidth() * 0.5f;

		matrixStack.push();

		float size = 1.0f + entity.getSize() / 8f;
		float stretch = MathHelper.lerp(partialTicks, entity.prevStretch, entity.stretch);

		matrixStack.translate(0,entity.getHeight() * 0.3f,0);
		matrixStack.scale(size, size, size);
		matrixStack.scale(1, stretch, 1);

		matrixStack.rotate(TransformationHelper.slerp(entity.prevQuat, entity.quat, partialTicks));

		matrixStack.rotate(Vector3f.XP.rotationDegrees(entity.rot1));
		matrixStack.rotate(Vector3f.YP.rotationDegrees(entity.rot2));
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(entity.rot3));

		IVertexBuilder buf = bufferIn.getBuffer(TUMBLEWEED);
		this.tumbleweed.render(
				matrixStack,
				buf,
				packedLightIn,
				OverlayTexture.NO_OVERLAY,
				1,
				1,
				1,
				alpha
		);

		matrixStack.pop();

		super.render(entity, entityYaw, partialTicks, matrixStack, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTumbleweed entity) {
		return TEXTURE;
	}

	public static class Factory implements IRenderFactory<EntityTumbleweed> {

		@Override
		public EntityRenderer<EntityTumbleweed> createRenderFor(EntityRendererManager manager) {
			return new RenderTumbleweed(manager);
		}
	}

}
