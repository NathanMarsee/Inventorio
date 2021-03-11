package me.danetnaverno.inventorio.quickbar

import me.danetnaverno.inventorio.inventorioRowLength
import me.danetnaverno.inventorio.isNotEmpty
import me.danetnaverno.inventorio.player.PlayerInventoryAddon
import me.danetnaverno.inventorio.slot.QuickBarItemStack
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.util.collection.DefaultedList

class QuickBarInventory(private val inventoryWrapper: PlayerInventoryAddon) : Inventory
{
    val stacks = DefaultedList.ofSize(inventorioRowLength, ItemStack.EMPTY)!!

    override fun clear()
    {
        stacks.clear()
    }

    override fun size(): Int
    {
        return stacks.size
    }

    override fun isEmpty(): Boolean
    {
        return stacks.none { it.isNotEmpty }
    }


    override fun getStack(slot: Int): ItemStack
    {
        return if (slot !in 0..size())
            ItemStack.EMPTY
        else
            stacks[slot]
    }

    override fun removeStack(slot: Int): ItemStack
    {
        return Inventories.removeStack(stacks, slot)
    }

    override fun removeStack(slot: Int, amount: Int): ItemStack
    {
        return Inventories.splitStack(stacks, slot, amount)
    }

    override fun setStack(slot: Int, stack: ItemStack)
    {
        stacks[slot] = QuickBarItemStack(inventoryWrapper, stack)
    }

    override fun markDirty()
    {
    }

    override fun canPlayerUse(player: PlayerEntity): Boolean
    {
        return true
    }
}