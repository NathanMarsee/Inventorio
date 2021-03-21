package me.danetnaverno.inventorio.util

import java.awt.Rectangle


val CANVAS_PLAYER_INVENTORY_TOP_WITH_DECO = Rectangle(0, 0, 256, 96)
val CANVAS_PLAYER_INVENTORY_TOP_NO_DECO = Rectangle(13, 13, 230, 83)

val CANVAS_PLAYER_INVENTORY_EXTENSION_WITH_DECO_P1 = Rectangle(0, 197, 256, 67)
val CANVAS_PLAYER_INVENTORY_EXTENSION_WITH_DECO_P2 = Rectangle(0, 264, 256, 17)

val CANVAS_PLAYER_INVENTORY_MAIN_WITH_DECO = Rectangle(0, 96, 256, 83)
val CANVAS_PLAYER_INVENTORY_MAIN_NO_DECO = Rectangle(13, 96, 230, 83)

val CANVAS_PLAYER_INVENTORY_UTILITY_EXT_WITH_DECO = Rectangle(0, 286, 34, 88)
val CANVAS_PLAYER_INVENTORY_UTILITY_EXT_NO_DECO = Rectangle(8, 294, 18, 72)

fun CANVAS_PLAYER_INVENTORY_TOOL_BELT_SLOT(slot: Int) = Rectangle(39 + 16 * slot, 286, 16, 16)

val CANVAS_PLAYER_INVENTORY_UTILITY_SELECTION_WITH_DECO = Rectangle(39, 307, 30, 30)
val CANVAS_PLAYER_INVENTORY_UTILITY_SELECTION_NO_DECO = Rectangle(43, 311, 22, 22)

val CANVAS_PLAYER_INVENTORY_PHYS_BAR = Rectangle(39, 344, 216, 18)


val CANVAS_EXTERNAL_INVENTORY_TOP_PART = Rectangle(0, 66, 256, 30)
val CANVAS_EXTERNAL_INVENTORY_EXTENSION_PART_TOP = Rectangle(0, 197, 256, 67)
val CANVAS_EXTERNAL_INVENTORY_EXTENSION_PART_BOTTOM = Rectangle(0, 264, 256, 17)
val CANVAS_EXTERNAL_INVENTORY_MAIN_PART = Rectangle(0, 96, 256, 96)
val CANVAS_EXTERNAL_INVENTORY_PHYS_BAR = Rectangle(39, 344, 216, 18)
val CANVAS_EXTERNAL_INVENTORY_INGORE_BUTTON = Rectangle(74, 307, 30, 32)

val CANVAS_CREATIVE_INVENTORY = Rectangle(0, 0, 256, 162)
val CANVAS_CREATIVE_INVENTORY_PHYS_BAR = Rectangle(12, 191, 216, 18)
val CANVAS_CREATIVE_INVENTORY_SEARCH_BAR = Rectangle(101, 227, 90, 12)