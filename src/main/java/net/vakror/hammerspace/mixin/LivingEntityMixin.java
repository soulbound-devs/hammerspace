package net.vakror.hammerspace.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.vakror.hammerspace.capability.HammerspaceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    protected LivingEntityMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;getValue()D"))
    public double gravity(AttributeInstance instance) {
        if (instance.getAttribute().equals(ForgeMod.ENTITY_GRAVITY.get())) {
            final double[] grav = new double[1];
            grav[0] = 1;
            level.getCapability(HammerspaceProvider.HAMMERSPACE).ifPresent(gravity -> grav[0] = gravity.gravity());
            return instance.getValue() * grav[0];
        }
        return instance.getValue();
    }
}