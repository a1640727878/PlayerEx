package github.clevernucleus.playerex.common.init.item;

import java.util.List;

import github.clevernucleus.playerex.api.Util;
import github.clevernucleus.playerex.common.init.Registry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * Mjolnir item object.
 */
public class MjolnirItem extends PickaxeItem implements ILoot {
	private final float weight;
	
	public MjolnirItem(final float par0) {
		super(ItemTier.IRON, 5, -3.2F, new Properties().group(Group.INSTANCE));
		this.weight = par0;
	}
	
	@Override
	public float getWeight() {
		return this.weight;
	}
	
	@Override
	public Rarity getRarity(ItemStack par0) {
		return Registry.IMMORTAL;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World par0, final PlayerEntity par1, final Hand par2) {
		ItemStack var0 = par1.getHeldItem(par2);
		RayTraceResult var1 = Util.lookPos(par0, par1, 100D);
		
		if(var1 != null && var1.getType() == RayTraceResult.Type.BLOCK || par1.rotationPitch >= -5) {
			int var2 = var1.getType().ordinal();
			
			if(var2 != -1) {
				par1.getCooldownTracker().setCooldown(this, 100);
				
				double var3 = var1.getHitVec().x - (var2 == 4 ? 0.5 : 0) + (var2 == 5 ? 0.5 : 0);
				double var4 = var1.getHitVec().y - (var2 == 0 ? 2.0 : 0) + (var2 == 1 ? 0.5 : 0);
				double var5 = var1.getHitVec().z - (var2 == 2 ? 0.5 : 0) + (var2 == 3 ? 0.5 : 0);
				
				if(par0 instanceof ServerWorld) {
					ServerWorld var6 = (ServerWorld)par0;
					List<LivingEntity> var7 = var6.getEntitiesWithinAABB(LivingEntity.class, Util.effectBounds(var3, var4, var5, 10D));
					
					for(LivingEntity var : var7) {
						LightningBoltEntity var8 = EntityType.LIGHTNING_BOLT.create(var6);
						var8.moveForced(Vector3d.copyCenteredHorizontally(var.getPosition()));
						var8.setEffectOnly(false);
						
						var8.setCaster((ServerPlayerEntity)par1);
						var6.addEntity(var8);
					}
					
					var0.damageItem(1, par1, var -> {
						var.sendBreakAnimation(par2);
					});
				}
			}
		}
		
		return ActionResult.resultSuccess(var0);
	}
}