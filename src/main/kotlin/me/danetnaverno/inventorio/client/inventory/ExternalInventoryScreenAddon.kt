package me.danetnaverno.inventorio.client.inventory

import com.mojang.blaze3d.systems.RenderSystem
import me.danetnaverno.inventorio.MathStuffConstants
import me.danetnaverno.inventorio.duck.HandlerDuck
import me.danetnaverno.inventorio.gui_canvas_inventorySlotSize
import me.danetnaverno.inventorio.gui_generalInventoryWidth
import me.danetnaverno.inventorio.isPlayerSlot
import me.danetnaverno.inventorio.mixin.client.HandledScreenAccessor
import me.danetnaverno.inventorio.player.PlayerAddon
import me.danetnaverno.inventorio.util.*
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier
import java.awt.Point

@Environment(EnvType.CLIENT)
object ExternalInventoryScreenAddon
{
    private val BACKGROUND_TEXTURE = Identifier("inventorio", "textures/gui/inventory_replacement.png")

    private var screen: HandledScreen<*>? = null
    private lateinit var accessor: HandledScreenAccessor
    private val client = MinecraftClient.getInstance()!!
    private var isIgnored = false
    private lateinit var offsetPoint : Point

    private fun initialize(handledScreen: HandledScreen<*>)
    {
        screen = handledScreen
        isIgnored = PlayerAddon.Client.local.isScreenHandlerIgnored(handledScreen.screenHandler)
        if (isIgnored)
            return
        this.offsetPoint = SlotRestrictionFilters.screenHandlerOffsets[handledScreen.screenHandler.javaClass] ?: Point(0, 0)
        accessor = screen as HandledScreenAccessor
        accessor.backgroundHeight += MathStuffConstants.getExtraPixelHeight(client.player!!)
        accessor.y = (handledScreen.height - accessor.backgroundHeight) / 2
        val screenHandlerAddon = (screen!!.screenHandler as? HandlerDuck)?.addon
        screenHandlerAddon?.offsetPlayerSlots(
                Math.min(0, (accessor.backgroundWidth - gui_generalInventoryWidth) / 2), 0,
                0, 0)
    }

    fun drawAddon(handledScreen: HandledScreen<*>, matrices: MatrixStack, mouseX: Int, mouseY: Int)
    {
        if (screen != handledScreen)
            initialize(handledScreen)
        if (isIgnored)
            return

        val nonPlayerItems = screen!!.screenHandler.slots.filterNot { it.isPlayerSlot }

        val screenX = accessor.x + offsetPoint.x + Math.min(0, (accessor.backgroundWidth - gui_generalInventoryWidth) / 2)
        val screenY = accessor.y + offsetPoint.y + (nonPlayerItems.maxOfOrNull { it.y } ?: 0) + gui_canvas_inventorySlotSize + 3
        val expansionRows = MathStuffConstants.getExtraRows(client.player!!)

        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        client.textureManager.bindTexture(BACKGROUND_TEXTURE)

        DrawableHelper.drawTexture(matrices,
                screenX + GUI_EXTERNAL_INVENTORY_TOP_PART.x, screenY + GUI_EXTERNAL_INVENTORY_TOP_PART.y,
                CANVAS_EXTERNAL_INVENTORY_TOP_PART.x.toFloat(), CANVAS_EXTERNAL_INVENTORY_TOP_PART.y.toFloat(),
                GUI_EXTERNAL_INVENTORY_TOP_PART.width, GUI_EXTERNAL_INVENTORY_TOP_PART.height,
                256, 512)

        val mainRec = GUI_EXTERNAL_INVENTORY_MAIN_PART(expansionRows)
        DrawableHelper.drawTexture(matrices,
                screenX + mainRec.x, screenY + mainRec.y,
                CANVAS_EXTERNAL_INVENTORY_MAIN_PART.x.toFloat(), CANVAS_EXTERNAL_INVENTORY_MAIN_PART.y.toFloat(),
                mainRec.width, mainRec.height,
                256, 512)

        if (expansionRows > 0)
        {
            val extRec = GUI_EXTERNAL_INVENTORY_EXTENSION_PART_TOP(expansionRows)
            DrawableHelper.drawTexture(matrices,
                    screenX + extRec.x, screenY + extRec.y,
                    CANVAS_EXTERNAL_INVENTORY_EXTENSION_PART_TOP.x.toFloat(), CANVAS_EXTERNAL_INVENTORY_EXTENSION_PART_TOP.y.toFloat(),
                    extRec.width, extRec.height,
                    256, 512)

            val extRec2 = GUI_EXTERNAL_INVENTORY_EXTENSION_PART_BOTTOM(expansionRows)
            DrawableHelper.drawTexture(matrices,
                    screenX + extRec2.x, screenY + extRec2.y,
                    CANVAS_EXTERNAL_INVENTORY_EXTENSION_PART_BOTTOM.x.toFloat(), CANVAS_EXTERNAL_INVENTORY_EXTENSION_PART_BOTTOM.y.toFloat(),
                    extRec2.width, extRec2.height,
                    256, 512)
        }

        //QuickBar - Physical slots
        /*val offset = if (extraPixelHeight > 0) extraPixelHeight + canvas_inventoryGap else 0
        quickBarWidget.drawBackground(matrices,
                screenX, screenY + offset,
                7, 149)*/

        val extRec3 = GUI_EXTERNAL_INVENTORY_INGORE_BUTTON(expansionRows)
        val text = LiteralText("✘")
        accessor.addAButton(ButtonWidget(screenX + extRec3.x, screenY + extRec3.y, extRec3.width, extRec3.height, text,
                { button ->
                    val playerAddonLocal = PlayerAddon.Client.local
                    playerAddonLocal.addScreenHandlerToIgnored(playerAddonLocal.player.currentScreenHandler)
                    client.player!!.closeHandledScreen()
                }, { buttonWidget: ButtonWidget, matrixStack: MatrixStack, x: Int, y: Int ->
            screen!!.renderTooltip(matrices, TranslatableText("inventorio.ignore_screen.tooltip"), x, y)
        }))
    }
}