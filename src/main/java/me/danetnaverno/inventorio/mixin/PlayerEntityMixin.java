package me.danetnaverno.inventorio.mixin;

import com.mojang.authlib.GameProfile;
import me.danetnaverno.inventorio.MathStuffConstantsKt;
import me.danetnaverno.inventorio.duck.HandlerDuck;
import me.danetnaverno.inventorio.duck.PlayerDuck;
import me.danetnaverno.inventorio.player.InventorioPlayerSerializer;
import me.danetnaverno.inventorio.player.PlayerAddon;
import me.danetnaverno.inventorio.player.PlayerScreenHandlerAddon;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerDuck
{
    @Unique public PlayerAddon addon;
    @Shadow @Final public PlayerInventory inventory;
    @Shadow public abstract EnderChestInventory getEnderChestInventory();

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void createAddon(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci)
    {
        PlayerEntity thisPlayer = (PlayerEntity)(Object)this;
        addon = PlayerAddon.Companion.create(thisPlayer);
        PlayerScreenHandlerAddon screenAddon = new PlayerScreenHandlerAddon(thisPlayer.playerScreenHandler);
        ((HandlerDuck)thisPlayer.playerScreenHandler).setAddon(screenAddon);
        screenAddon.initialize(addon);
    }

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void resizeEnderChest(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo ci)
    {
        SimpleInventoryAccessor accessor = ((SimpleInventoryAccessor) getEnderChestInventory());
        accessor.setSize(MathStuffConstantsKt.vanillaRowLength * 6);
        accessor.setStacks(DefaultedList.ofSize(MathStuffConstantsKt.vanillaRowLength * 6, ItemStack.EMPTY));
    }

    @Inject(method = "equipStack", at = @At(value = "JUMP"), cancellable = true)
    public void todoRenameMe(EquipmentSlot slot, ItemStack stack, CallbackInfo ci)
    {
        if (slot == EquipmentSlot.MAINHAND)
            addon.getInventoryAddon().getShortcutQuickBar().setStack(inventory.selectedSlot, stack);
        else if (slot == EquipmentSlot.OFFHAND)
            ci.cancel();
    }

    @Inject(method = "getEquippedStack", at = @At(value = "HEAD"), cancellable = true)
    public void todoRenameMe(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> cir)
    {
        if (slot == EquipmentSlot.OFFHAND)
        {
            int index = MathStuffConstantsKt.getUtilityBarSlotsRange().getFirst() + addon.getInventoryAddon().getSelectedUtility();
            ItemStack stack = inventory.getStack(index);
            cir.setReturnValue(stack);
        }
        else if (slot.getType() == EquipmentSlot.Type.ARMOR)
            cir.setReturnValue(this.inventory.armor.get(slot.getEntitySlotId()));
    }

    @Redirect(method = "getBlockBreakingSpeed", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
    public boolean todoRenameMe(ItemStack itemStack)
    {
        return false;
    }

    @Inject(method = "attack", at = @At(value = "HEAD"))
    public void preAttack(Entity target, CallbackInfo ci)
    {
        if (target.isAttackable())
            addon.getInventoryAddon().prePlayerAttack(target);
    }

    @Inject(method = "attack", at = @At(value = "RETURN"))
    public void postAttack(Entity target, CallbackInfo ci)
    {
        if (target.isAttackable())
            addon.getInventoryAddon().postPlayerAttack(target);
    }

    @Inject(method = "readCustomDataFromTag", at = @At(value = "RETURN"))
    public void todoRenameMe2(CompoundTag tag, CallbackInfo ci)
    {
        InventorioPlayerSerializer.INSTANCE.deserialize(addon, tag.getCompound("Inventorio"));
    }

    @Inject(method = "writeCustomDataToTag", at = @At(value = "RETURN"))
    public void todoRenameMe3(CompoundTag tag, CallbackInfo ci)
    {
        CompoundTag inventorioTag = new CompoundTag();
        InventorioPlayerSerializer.INSTANCE.serialize(addon, inventorioTag);
        tag.put("Inventorio", inventorioTag);
    }

    public PlayerAddon getAddon()
    {
        return addon;
    }
}