package cn.ksmcbrigade.dr.mixin;

import cn.ksmcbrigade.dr.Drugged;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemTooltipMixin {
    @Shadow public abstract boolean hasTag();

    @Shadow @Nullable public abstract CompoundTag getTag();

    @Inject(method = "getTooltipLines",at = @At("RETURN"), cancellable = true)
    public void tooltip(Player p_41652_, TooltipFlag p_41653_, CallbackInfoReturnable<List<Component>> cir){
        if(this.getTag()!=null && this.hasTag() && this.getTag().getList("drugged",10)!=null){
            if(!this.getTag().contains("druggedHide")){
                cir.setReturnValue(Drugged.get(cir.getReturnValue(),this.getTag().getList("drugged",10)));
            }
        }
    }
}
