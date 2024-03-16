package cn.ksmcbrigade.dr.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "finishUsingItem",at = @At("HEAD"))
    public void finish(ItemStack p_41409_, Level p_41410_, LivingEntity p_41411_, CallbackInfoReturnable<ItemStack> cir){
        if (p_41409_.getTag() != null && p_41409_.hasTag() && !p_41409_.getTag().getList("drugged", 10).equals(new ListTag())) {
            for (Tag tag : p_41409_.getTag().getList("drugged",10)) {
                if (tag instanceof CompoundTag compoundTag) {
                    p_41411_.addEffect(Objects.requireNonNull(MobEffectInstance.load(compoundTag)));
                }
            }
        }
    }
}
