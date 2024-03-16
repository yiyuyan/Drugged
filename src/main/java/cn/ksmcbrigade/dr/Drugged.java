package cn.ksmcbrigade.dr;

import com.mojang.brigadier.arguments.BoolArgumentType;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod("dr")
@Mod.EventBusSubscriber
public class Drugged {

    public static int exp = 2;

    public Drugged() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void command(RegisterCommandsEvent event){
        event.getDispatcher().register(Commands.literal("drugged").executes(context -> {
            Player entity = (Player)context.getSource().getEntity();
            if(entity!=null){
                if(!entity.getMainHandItem().isEmpty() && !entity.getOffhandItem().isEmpty() && (entity.getOffhandItem().getItem() instanceof PotionItem)){
                    if(entity.experienceLevel>=exp){

                        if(!entity.getMainHandItem().hasTag()){
                            entity.getMainHandItem().getOrCreateTag().put("drugged",new ListTag());
                        }

                        if(!Objects.requireNonNull(entity.getMainHandItem().getTag()).contains("drugged")){
                            entity.getMainHandItem().getOrCreateTag().put("drugged",new ListTag());
                        }

                        for(MobEffectInstance effect:PotionUtils.getMobEffects(entity.getOffhandItem())){
                            CompoundTag compoundTag = effect.save(new CompoundTag());
                            Objects.requireNonNull(entity.getMainHandItem().getTag()).getList("drugged",10).add(compoundTag);
                        }

                        entity.setItemInHand(InteractionHand.OFF_HAND, Items.GLASS_BOTTLE.getDefaultInstance());
                        entity.giveExperienceLevels(-exp);
                    }
                    else{
                        entity.sendMessage(Component.nullToEmpty(I18n.get("commands.dr.cannot_xp").replace("{x}",String.valueOf(exp))),entity.getUUID());
                    }
                }
                else{
                    entity.sendMessage(new TranslatableComponent("commands.dr.empty"),entity.getUUID());
                }
            }
            return 0;
        }).then(Commands.argument("hide", BoolArgumentType.bool()).executes(context -> {
            Player entity = (Player)context.getSource().getEntity();
            if(entity!=null){
                if(!entity.getMainHandItem().isEmpty() && !entity.getOffhandItem().isEmpty() && (entity.getOffhandItem().getItem() instanceof PotionItem)){
                    if(entity.experienceLevel>=exp){

                        if(!entity.getMainHandItem().hasTag()){
                            entity.getMainHandItem().getOrCreateTag().put("drugged",new ListTag());
                        }

                        if(!Objects.requireNonNull(entity.getMainHandItem().getTag()).contains("drugged")){
                            entity.getMainHandItem().getOrCreateTag().put("drugged",new ListTag());
                        }

                        for(MobEffectInstance effect:PotionUtils.getMobEffects(entity.getOffhandItem())){
                            CompoundTag compoundTag = effect.save(new CompoundTag());
                            Objects.requireNonNull(entity.getMainHandItem().getTag()).getList("drugged",10).add(compoundTag);
                        }

                        if(BoolArgumentType.getBool(context,"hide")){
                            Objects.requireNonNull(entity.getMainHandItem().getTag()).put("druggedHide",new CompoundTag());
                        }

                        entity.setItemInHand(InteractionHand.OFF_HAND, Items.GLASS_BOTTLE.getDefaultInstance());
                        entity.giveExperienceLevels(-exp);
                    }
                    else{
                        entity.sendMessage(Component.nullToEmpty(I18n.get("commands.dr.cannot_xp").replace("{x}",String.valueOf(exp))),entity.getUUID());
                    }
                }
                else{
                    entity.sendMessage(new TranslatableComponent("commands.dr.empty"),entity.getUUID());
                }
            }
            return 0;
        })));
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event){
        if(event.getEntityLiving() instanceof Player player){
            ItemStack p_41395_ = player.getItemInHand(player.getUsedItemHand());
            if(p_41395_.getItem().getFoodProperties()==null){ //!food
                if (p_41395_.getTag() != null && p_41395_.hasTag() && !p_41395_.getTag().getList("drugged", 10).equals(new ListTag())) {
                    for (Tag tag : p_41395_.getTag().getList("drugged",10)) {
                        if (tag instanceof CompoundTag compoundTag) {
                            ((LivingEntity)event.getTarget()).addEffect(Objects.requireNonNull(MobEffectInstance.load(compoundTag)));
                        }
                    }
                }
            }
        }
    }

    public static List<Component> get(List<Component> list,ListTag ListTag){
        ArrayList<Component> componentList = new java.util.ArrayList<>(List.copyOf(list));
        for (Tag tag : ListTag) {
            if (tag instanceof CompoundTag compoundTag) {
                MobEffectInstance effect = MobEffectInstance.load(compoundTag);
                if (effect != null) {
                    componentList.add(Component.nullToEmpty(effect.getEffect().getDisplayName().getString() + " " + (effect.getAmplifier() + 1) + "-" + effect.getDuration() + "ticks"));
                }
            }
        }
        return componentList;
    }
}
