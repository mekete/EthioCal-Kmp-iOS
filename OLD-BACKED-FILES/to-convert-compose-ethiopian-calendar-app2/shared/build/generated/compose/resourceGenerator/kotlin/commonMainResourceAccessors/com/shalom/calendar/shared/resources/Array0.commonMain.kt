@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package com.shalom.calendar.shared.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringArrayResource

private object CommonMainArray0 {
  public val ethiopian_months: StringArrayResource by 
      lazy { init_ethiopian_months() }

  public val theme_color_names: StringArrayResource by 
      lazy { init_theme_color_names() }

  public val theme_mode_names: StringArrayResource by 
      lazy { init_theme_mode_names() }

  public val weekday_names_full: StringArrayResource by 
      lazy { init_weekday_names_full() }

  public val weekday_names_short: StringArrayResource by 
      lazy { init_weekday_names_short() }
}

@InternalResourceApi
internal fun _collectCommonMainArray0Resources(map: MutableMap<String, StringArrayResource>) {
  map.put("ethiopian_months", CommonMainArray0.ethiopian_months)
  map.put("theme_color_names", CommonMainArray0.theme_color_names)
  map.put("theme_mode_names", CommonMainArray0.theme_mode_names)
  map.put("weekday_names_full", CommonMainArray0.weekday_names_full)
  map.put("weekday_names_short", CommonMainArray0.weekday_names_short)
}

public val Res.array.ethiopian_months: StringArrayResource
  get() = CommonMainArray0.ethiopian_months

private fun init_ethiopian_months(): StringArrayResource =
    org.jetbrains.compose.resources.StringArrayResource(
  "string-array:ethiopian_months", "ethiopian_months",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 10, 154),
    )
)

public val Res.array.theme_color_names: StringArrayResource
  get() = CommonMainArray0.theme_color_names

private fun init_theme_color_names(): StringArrayResource =
    org.jetbrains.compose.resources.StringArrayResource(
  "string-array:theme_color_names", "theme_color_names",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 165, 71),
    )
)

public val Res.array.theme_mode_names: StringArrayResource
  get() = CommonMainArray0.theme_mode_names

private fun init_theme_mode_names(): StringArrayResource =
    org.jetbrains.compose.resources.StringArrayResource(
  "string-array:theme_mode_names", "theme_mode_names",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 237, 68),
    )
)

public val Res.array.weekday_names_full: StringArrayResource
  get() = CommonMainArray0.weekday_names_full

private fun init_weekday_names_full(): StringArrayResource =
    org.jetbrains.compose.resources.StringArrayResource(
  "string-array:weekday_names_full", "weekday_names_full",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 306,
    110),
    )
)

public val Res.array.weekday_names_short: StringArrayResource
  get() = CommonMainArray0.weekday_names_short

private fun init_weekday_names_short(): StringArrayResource =
    org.jetbrains.compose.resources.StringArrayResource(
  "string-array:weekday_names_short", "weekday_names_short",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 417, 67),
    )
)
