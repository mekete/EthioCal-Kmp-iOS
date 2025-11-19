@file:OptIn(org.jetbrains.compose.resources.InternalResourceApi::class)

package com.shalom.calendar.shared.resources

import kotlin.OptIn
import kotlin.String
import kotlin.collections.MutableMap
import org.jetbrains.compose.resources.InternalResourceApi
import org.jetbrains.compose.resources.StringResource

private object CommonMainString0 {
  public val about_us_app_version: StringResource by 
      lazy { init_about_us_app_version() }

  public val about_us_build_time: StringResource by 
      lazy { init_about_us_build_time() }

  public val about_us_contact_us: StringResource by 
      lazy { init_about_us_contact_us() }

  public val about_us_developed_by: StringResource by 
      lazy { init_about_us_developed_by() }

  public val about_us_developer: StringResource by 
      lazy { init_about_us_developer() }

  public val about_us_dialog_title: StringResource by 
      lazy { init_about_us_dialog_title() }

  public val about_us_privacy_policy: StringResource by 
      lazy { init_about_us_privacy_policy() }

  public val about_us_rate_app: StringResource by 
      lazy { init_about_us_rate_app() }

  public val about_us_terms_of_service: StringResource by 
      lazy { init_about_us_terms_of_service() }

  public val add_event: StringResource by 
      lazy { init_add_event() }

  public val add_reminder: StringResource by 
      lazy { init_add_reminder() }

  public val all_day_event: StringResource by 
      lazy { init_all_day_event() }

  public val at_time_of_event: StringResource by 
      lazy { init_at_time_of_event() }

  public val button_calculate_difference: StringResource by 
      lazy { init_button_calculate_difference() }

  public val button_cancel: StringResource by 
      lazy { init_button_cancel() }

  public val button_close: StringResource by 
      lazy { init_button_close() }

  public val button_create_reminder: StringResource by 
      lazy { init_button_create_reminder() }

  public val button_next: StringResource by 
      lazy { init_button_next() }

  public val button_ok: StringResource by 
      lazy { init_button_ok() }

  public val button_prev: StringResource by 
      lazy { init_button_prev() }

  public val button_to_ethiopian: StringResource by 
      lazy { init_button_to_ethiopian() }

  public val button_to_gregorian: StringResource by 
      lazy { init_button_to_gregorian() }

  public val button_today: StringResource by 
      lazy { init_button_today() }

  public val button_view_events: StringResource by 
      lazy { init_button_view_events() }

  public val cancel: StringResource by 
      lazy { init_cancel() }

  public val category: StringResource by 
      lazy { init_category() }

  public val cd_back: StringResource by 
      lazy { init_cd_back() }

  public val cd_calendar_state: StringResource by 
      lazy { init_cd_calendar_state() }

  public val cd_holiday_info: StringResource by 
      lazy { init_cd_holiday_info() }

  public val cd_navigate: StringResource by 
      lazy { init_cd_navigate() }

  public val cd_next_year: StringResource by 
      lazy { init_cd_next_year() }

  public val cd_pick_date: StringResource by 
      lazy { init_cd_pick_date() }

  public val cd_previous_year: StringResource by 
      lazy { init_cd_previous_year() }

  public val cd_selected: StringResource by 
      lazy { init_cd_selected() }

  public val conversion_result_title: StringResource by 
      lazy { init_conversion_result_title() }

  public val create: StringResource by 
      lazy { init_create() }

  public val date: StringResource by 
      lazy { init_date() }

  public val date_details_dialog_title: StringResource by 
      lazy { init_date_details_dialog_title() }

  public val date_difference_result_title: StringResource by 
      lazy { init_date_difference_result_title() }

  public val delete: StringResource by 
      lazy { init_delete() }

  public val delete_event: StringResource by 
      lazy { init_delete_event() }

  public val delete_event_message: StringResource by 
      lazy { init_delete_event_message() }

  public val delete_event_title: StringResource by 
      lazy { init_delete_event_title() }

  public val description_optional: StringResource by 
      lazy { init_description_optional() }

  public val dialog_title_select_ethiopian_date: StringResource by 
      lazy { init_dialog_title_select_ethiopian_date() }

  public val edit_event: StringResource by 
      lazy { init_edit_event() }

  public val empty_no_holidays_display: StringResource by 
      lazy { init_empty_no_holidays_display() }

  public val empty_no_holidays_month: StringResource by 
      lazy { init_empty_no_holidays_month() }

  public val end_time: StringResource by 
      lazy { init_end_time() }

  public val ends: StringResource by 
      lazy { init_ends() }

  public val error_loading_calendar: StringResource by 
      lazy { init_error_loading_calendar() }

  public val error_loading_events: StringResource by 
      lazy { init_error_loading_events() }

  public val event_date_all_day: StringResource by 
      lazy { init_event_date_all_day() }

  public val filter: StringResource by 
      lazy { init_filter() }

  public val filter_muslim: StringResource by 
      lazy { init_filter_muslim() }

  public val filter_orthodox: StringResource by 
      lazy { init_filter_orthodox() }

  public val filter_public: StringResource by 
      lazy { init_filter_public() }

  public val holiday_derg: StringResource by 
      lazy { init_holiday_derg() }

  public val holiday_derg_celebration: StringResource by 
      lazy { init_holiday_derg_celebration() }

  public val holiday_derg_history: StringResource by 
      lazy { init_holiday_derg_history() }

  public val holiday_info_dialog_checkbox: StringResource by 
      lazy { init_holiday_info_dialog_checkbox() }

  public val holiday_info_dialog_message: StringResource by 
      lazy { init_holiday_info_dialog_message() }

  public val holiday_info_dialog_title: StringResource by 
      lazy { init_holiday_info_dialog_title() }

  public val holiday_muslim_arafat: StringResource by 
      lazy { init_holiday_muslim_arafat() }

  public val holiday_muslim_arafat_celebration: StringResource by 
      lazy { init_holiday_muslim_arafat_celebration() }

  public val holiday_muslim_arafat_history: StringResource by 
      lazy { init_holiday_muslim_arafat_history() }

  public val holiday_muslim_ashura: StringResource by 
      lazy { init_holiday_muslim_ashura() }

  public val holiday_muslim_ashura_celebration: StringResource by 
      lazy { init_holiday_muslim_ashura_celebration() }

  public val holiday_muslim_ashura_history: StringResource by 
      lazy { init_holiday_muslim_ashura_history() }

  public val holiday_muslim_eid_adha: StringResource by 
      lazy { init_holiday_muslim_eid_adha() }

  public val holiday_muslim_eid_adha_celebration: StringResource by 
      lazy { init_holiday_muslim_eid_adha_celebration() }

  public val holiday_muslim_eid_adha_history: StringResource by 
      lazy { init_holiday_muslim_eid_adha_history() }

  public val holiday_muslim_eid_fitr: StringResource by 
      lazy { init_holiday_muslim_eid_fitr() }

  public val holiday_muslim_eid_fitr_celebration: StringResource by 
      lazy { init_holiday_muslim_eid_fitr_celebration() }

  public val holiday_muslim_eid_fitr_history: StringResource by 
      lazy { init_holiday_muslim_eid_fitr_history() }

  public val holiday_muslim_hijri_new_year: StringResource by 
      lazy { init_holiday_muslim_hijri_new_year() }

  public val holiday_muslim_hijri_new_year_celebration: StringResource by 
      lazy { init_holiday_muslim_hijri_new_year_celebration() }

  public val holiday_muslim_hijri_new_year_history: StringResource by 
      lazy { init_holiday_muslim_hijri_new_year_history() }

  public val holiday_muslim_isra_miraj: StringResource by 
      lazy { init_holiday_muslim_isra_miraj() }

  public val holiday_muslim_isra_miraj_celebration: StringResource by 
      lazy { init_holiday_muslim_isra_miraj_celebration() }

  public val holiday_muslim_isra_miraj_history: StringResource by 
      lazy { init_holiday_muslim_isra_miraj_history() }

  public val holiday_muslim_mawlid: StringResource by 
      lazy { init_holiday_muslim_mawlid() }

  public val holiday_muslim_mawlid_celebration: StringResource by 
      lazy { init_holiday_muslim_mawlid_celebration() }

  public val holiday_muslim_mawlid_history: StringResource by 
      lazy { init_holiday_muslim_mawlid_history() }

  public val holiday_muslim_mid_shaban: StringResource by 
      lazy { init_holiday_muslim_mid_shaban() }

  public val holiday_muslim_mid_shaban_celebration: StringResource by 
      lazy { init_holiday_muslim_mid_shaban_celebration() }

  public val holiday_muslim_mid_shaban_history: StringResource by 
      lazy { init_holiday_muslim_mid_shaban_history() }

  public val holiday_muslim_ramadan_start: StringResource by 
      lazy { init_holiday_muslim_ramadan_start() }

  public val holiday_muslim_ramadan_start_celebration: StringResource by 
      lazy { init_holiday_muslim_ramadan_start_celebration() }

  public val holiday_muslim_ramadan_start_history: StringResource by 
      lazy { init_holiday_muslim_ramadan_start_history() }

  public val holiday_orthodox_abiy_tsom: StringResource by 
      lazy { init_holiday_orthodox_abiy_tsom() }

  public val holiday_orthodox_abiy_tsom_celebration: StringResource by 
      lazy { init_holiday_orthodox_abiy_tsom_celebration() }

  public val holiday_orthodox_abiy_tsom_history: StringResource by 
      lazy { init_holiday_orthodox_abiy_tsom_history() }

  public val holiday_orthodox_debre_tabor: StringResource by 
      lazy { init_holiday_orthodox_debre_tabor() }

  public val holiday_orthodox_debre_tabor_celebration: StringResource by 
      lazy { init_holiday_orthodox_debre_tabor_celebration() }

  public val holiday_orthodox_debre_tabor_history: StringResource by 
      lazy { init_holiday_orthodox_debre_tabor_history() }

  public val holiday_orthodox_debre_zeit: StringResource by 
      lazy { init_holiday_orthodox_debre_zeit() }

  public val holiday_orthodox_debre_zeit_celebration: StringResource by 
      lazy { init_holiday_orthodox_debre_zeit_celebration() }

  public val holiday_orthodox_debre_zeit_history: StringResource by 
      lazy { init_holiday_orthodox_debre_zeit_history() }

  public val holiday_orthodox_erget: StringResource by 
      lazy { init_holiday_orthodox_erget() }

  public val holiday_orthodox_erget_celebration: StringResource by 
      lazy { init_holiday_orthodox_erget_celebration() }

  public val holiday_orthodox_erget_history: StringResource by 
      lazy { init_holiday_orthodox_erget_history() }

  public val holiday_orthodox_fasika: StringResource by 
      lazy { init_holiday_orthodox_fasika() }

  public val holiday_orthodox_fasika_celebration: StringResource by 
      lazy { init_holiday_orthodox_fasika_celebration() }

  public val holiday_orthodox_fasika_history: StringResource by 
      lazy { init_holiday_orthodox_fasika_history() }

  public val holiday_orthodox_filseta: StringResource by 
      lazy { init_holiday_orthodox_filseta() }

  public val holiday_orthodox_filseta_celebration: StringResource by 
      lazy { init_holiday_orthodox_filseta_celebration() }

  public val holiday_orthodox_filseta_history: StringResource by 
      lazy { init_holiday_orthodox_filseta_history() }

  public val holiday_orthodox_ghad: StringResource by 
      lazy { init_holiday_orthodox_ghad() }

  public val holiday_orthodox_ghad_celebration: StringResource by 
      lazy { init_holiday_orthodox_ghad_celebration() }

  public val holiday_orthodox_ghad_history: StringResource by 
      lazy { init_holiday_orthodox_ghad_history() }

  public val holiday_orthodox_hosanna: StringResource by 
      lazy { init_holiday_orthodox_hosanna() }

  public val holiday_orthodox_hosanna_celebration: StringResource by 
      lazy { init_holiday_orthodox_hosanna_celebration() }

  public val holiday_orthodox_hosanna_history: StringResource by 
      lazy { init_holiday_orthodox_hosanna_history() }

  public val holiday_orthodox_kana_zegelila: StringResource by 
      lazy { init_holiday_orthodox_kana_zegelila() }

  public val holiday_orthodox_kana_zegelila_celebration: StringResource by 
      lazy { init_holiday_orthodox_kana_zegelila_celebration() }

  public val holiday_orthodox_kana_zegelila_history: StringResource by 
      lazy { init_holiday_orthodox_kana_zegelila_history() }

  public val holiday_orthodox_lideta_mariam: StringResource by 
      lazy { init_holiday_orthodox_lideta_mariam() }

  public val holiday_orthodox_lideta_mariam_celebration: StringResource by 
      lazy { init_holiday_orthodox_lideta_mariam_celebration() }

  public val holiday_orthodox_lideta_mariam_history: StringResource by 
      lazy { init_holiday_orthodox_lideta_mariam_history() }

  public val holiday_orthodox_nineveh: StringResource by 
      lazy { init_holiday_orthodox_nineveh() }

  public val holiday_orthodox_nineveh_celebration: StringResource by 
      lazy { init_holiday_orthodox_nineveh_celebration() }

  public val holiday_orthodox_nineveh_history: StringResource by 
      lazy { init_holiday_orthodox_nineveh_history() }

  public val holiday_orthodox_peraklitos: StringResource by 
      lazy { init_holiday_orthodox_peraklitos() }

  public val holiday_orthodox_peraklitos_celebration: StringResource by 
      lazy { init_holiday_orthodox_peraklitos_celebration() }

  public val holiday_orthodox_peraklitos_history: StringResource by 
      lazy { init_holiday_orthodox_peraklitos_history() }

  public val holiday_orthodox_rikbe_kahinat: StringResource by 
      lazy { init_holiday_orthodox_rikbe_kahinat() }

  public val holiday_orthodox_rikbe_kahinat_celebration: StringResource by 
      lazy { init_holiday_orthodox_rikbe_kahinat_celebration() }

  public val holiday_orthodox_rikbe_kahinat_history: StringResource by 
      lazy { init_holiday_orthodox_rikbe_kahinat_history() }

  public val holiday_orthodox_siklet: StringResource by 
      lazy { init_holiday_orthodox_siklet() }

  public val holiday_orthodox_siklet_celebration: StringResource by 
      lazy { init_holiday_orthodox_siklet_celebration() }

  public val holiday_orthodox_siklet_history: StringResource by 
      lazy { init_holiday_orthodox_siklet_history() }

  public val holiday_orthodox_tensae: StringResource by 
      lazy { init_holiday_orthodox_tensae() }

  public val holiday_orthodox_tensae_celebration: StringResource by 
      lazy { init_holiday_orthodox_tensae_celebration() }

  public val holiday_orthodox_tensae_history: StringResource by 
      lazy { init_holiday_orthodox_tensae_history() }

  public val holiday_orthodox_tsome_dihnet: StringResource by 
      lazy { init_holiday_orthodox_tsome_dihnet() }

  public val holiday_orthodox_tsome_dihnet_celebration: StringResource by 
      lazy { init_holiday_orthodox_tsome_dihnet_celebration() }

  public val holiday_orthodox_tsome_dihnet_history: StringResource by 
      lazy { init_holiday_orthodox_tsome_dihnet_history() }

  public val holiday_orthodox_tsome_hawariat: StringResource by 
      lazy { init_holiday_orthodox_tsome_hawariat() }

  public val holiday_orthodox_tsome_hawariat_celebration: StringResource by 
      lazy { init_holiday_orthodox_tsome_hawariat_celebration() }

  public val holiday_orthodox_tsome_hawariat_history: StringResource by 
      lazy { init_holiday_orthodox_tsome_hawariat_history() }

  public val holiday_public_adwa: StringResource by 
      lazy { init_holiday_public_adwa() }

  public val holiday_public_adwa_celebration: StringResource by 
      lazy { init_holiday_public_adwa_celebration() }

  public val holiday_public_adwa_history: StringResource by 
      lazy { init_holiday_public_adwa_history() }

  public val holiday_public_christian_genna: StringResource by 
      lazy { init_holiday_public_christian_genna() }

  public val holiday_public_christian_genna_celebration: StringResource by 
      lazy { init_holiday_public_christian_genna_celebration() }

  public val holiday_public_christian_genna_history: StringResource by 
      lazy { init_holiday_public_christian_genna_history() }

  public val holiday_public_christian_meskel: StringResource by 
      lazy { init_holiday_public_christian_meskel() }

  public val holiday_public_christian_meskel_celebration: StringResource by 
      lazy { init_holiday_public_christian_meskel_celebration() }

  public val holiday_public_christian_meskel_history: StringResource by 
      lazy { init_holiday_public_christian_meskel_history() }

  public val holiday_public_christian_timket: StringResource by 
      lazy { init_holiday_public_christian_timket() }

  public val holiday_public_christian_timket_celebration: StringResource by 
      lazy { init_holiday_public_christian_timket_celebration() }

  public val holiday_public_christian_timket_history: StringResource by 
      lazy { init_holiday_public_christian_timket_history() }

  public val holiday_public_enkutatash: StringResource by 
      lazy { init_holiday_public_enkutatash() }

  public val holiday_public_enkutatash_celebration: StringResource by 
      lazy { init_holiday_public_enkutatash_celebration() }

  public val holiday_public_enkutatash_history: StringResource by 
      lazy { init_holiday_public_enkutatash_history() }

  public val holiday_public_labour_day: StringResource by 
      lazy { init_holiday_public_labour_day() }

  public val holiday_public_labour_day_celebration: StringResource by 
      lazy { init_holiday_public_labour_day_celebration() }

  public val holiday_public_labour_day_history: StringResource by 
      lazy { init_holiday_public_labour_day_history() }

  public val holiday_public_patriots_day: StringResource by 
      lazy { init_holiday_public_patriots_day() }

  public val holiday_public_patriots_day_celebration: StringResource by 
      lazy { init_holiday_public_patriots_day_celebration() }

  public val holiday_public_patriots_day_history: StringResource by 
      lazy { init_holiday_public_patriots_day_history() }

  public val hour_before_1: StringResource by 
      lazy { init_hour_before_1() }

  public val label_date_difference: StringResource by 
      lazy { init_label_date_difference() }

  public val label_day_off: StringResource by 
      lazy { init_label_day_off() }

  public val label_difference_result: StringResource by 
      lazy { init_label_difference_result() }

  public val label_ec_suffix: StringResource by 
      lazy { init_label_ec_suffix() }

  public val label_end_date: StringResource by 
      lazy { init_label_end_date() }

  public val label_ethiopian_date: StringResource by 
      lazy { init_label_ethiopian_date() }

  public val label_filter_by_type: StringResource by 
      lazy { init_label_filter_by_type() }

  public val label_from_ethiopian: StringResource by 
      lazy { init_label_from_ethiopian() }

  public val label_from_gregorian: StringResource by 
      lazy { init_label_from_gregorian() }

  public val label_gregorian_date: StringResource by 
      lazy { init_label_gregorian_date() }

  public val label_holidays: StringResource by 
      lazy { init_label_holidays() }

  public val label_start_date: StringResource by 
      lazy { init_label_start_date() }

  public val label_theme_color: StringResource by 
      lazy { init_label_theme_color() }

  public val label_theme_mode: StringResource by 
      lazy { init_label_theme_mode() }

  public val menu_about_us: StringResource by 
      lazy { init_menu_about_us() }

  public val menu_additional_settings: StringResource by 
      lazy { init_menu_additional_settings() }

  public val menu_color_theme: StringResource by 
      lazy { init_menu_color_theme() }

  public val menu_language: StringResource by 
      lazy { init_menu_language() }

  public val menu_notifications: StringResource by 
      lazy { init_menu_notifications() }

  public val menu_privacy_policy: StringResource by 
      lazy { init_menu_privacy_policy() }

  public val menu_share_app: StringResource by 
      lazy { init_menu_share_app() }

  public val minutes_before_15: StringResource by 
      lazy { init_minutes_before_15() }

  public val minutes_before_30: StringResource by 
      lazy { init_minutes_before_30() }

  public val minutes_before_5: StringResource by 
      lazy { init_minutes_before_5() }

  public val nav_convert: StringResource by 
      lazy { init_nav_convert() }

  public val nav_event: StringResource by 
      lazy { init_nav_event() }

  public val nav_holiday: StringResource by 
      lazy { init_nav_holiday() }

  public val nav_month: StringResource by 
      lazy { init_nav_month() }

  public val nav_more: StringResource by 
      lazy { init_nav_more() }

  public val never: StringResource by 
      lazy { init_never() }

  public val no_events: StringResource by 
      lazy { init_no_events() }

  public val no_repeat: StringResource by 
      lazy { init_no_repeat() }

  public val notification_banner_dismiss: StringResource by 
      lazy { init_notification_banner_dismiss() }

  public val notification_banner_enable: StringResource by 
      lazy { init_notification_banner_enable() }

  public val notification_banner_message: StringResource by 
      lazy { init_notification_banner_message() }

  public val notification_banner_title: StringResource by 
      lazy { init_notification_banner_title() }

  public val ok: StringResource by 
      lazy { init_ok() }

  public val onboarding_calendar_description: StringResource by 
      lazy { init_onboarding_calendar_description() }

  public val onboarding_calendar_dual_display: StringResource by 
      lazy { init_onboarding_calendar_dual_display() }

  public val onboarding_calendar_dual_display_desc: StringResource by 
      lazy { init_onboarding_calendar_dual_display_desc() }

  public val onboarding_calendar_tip: StringResource by 
      lazy { init_onboarding_calendar_tip() }

  public val onboarding_calendar_title: StringResource by 
      lazy { init_onboarding_calendar_title() }

  public val onboarding_done: StringResource by 
      lazy { init_onboarding_done() }

  public val onboarding_feature_date_converter: StringResource by 
      lazy { init_onboarding_feature_date_converter() }

  public val onboarding_feature_dual_calendar: StringResource by 
      lazy { init_onboarding_feature_dual_calendar() }

  public val onboarding_feature_holidays: StringResource by 
      lazy { init_onboarding_feature_holidays() }

  public val onboarding_feature_multilingual: StringResource by 
      lazy { init_onboarding_feature_multilingual() }

  public val onboarding_feature_reminders: StringResource by 
      lazy { init_onboarding_feature_reminders() }

  public val onboarding_get_started: StringResource by 
      lazy { init_onboarding_get_started() }

  public val onboarding_holidays_description: StringResource by 
      lazy { init_onboarding_holidays_description() }

  public val onboarding_holidays_muslim: StringResource by 
      lazy { init_onboarding_holidays_muslim() }

  public val onboarding_holidays_muslim_desc: StringResource by 
      lazy { init_onboarding_holidays_muslim_desc() }

  public val onboarding_holidays_orthodox: StringResource by 
      lazy { init_onboarding_holidays_orthodox() }

  public val onboarding_holidays_orthodox_days: StringResource by 
      lazy { init_onboarding_holidays_orthodox_days() }

  public val onboarding_holidays_orthodox_days_desc: StringResource by 
      lazy { init_onboarding_holidays_orthodox_days_desc() }

  public val onboarding_holidays_orthodox_desc: StringResource by 
      lazy { init_onboarding_holidays_orthodox_desc() }

  public val onboarding_holidays_public: StringResource by 
      lazy { init_onboarding_holidays_public() }

  public val onboarding_holidays_public_desc: StringResource by 
      lazy { init_onboarding_holidays_public_desc() }

  public val onboarding_holidays_title: StringResource by 
      lazy { init_onboarding_holidays_title() }

  public val onboarding_language_description: StringResource by 
      lazy { init_onboarding_language_description() }

  public val onboarding_language_title: StringResource by 
      lazy { init_onboarding_language_title() }

  public val onboarding_next: StringResource by 
      lazy { init_onboarding_next() }

  public val onboarding_skip: StringResource by 
      lazy { init_onboarding_skip() }

  public val onboarding_theme_dark: StringResource by 
      lazy { init_onboarding_theme_dark() }

  public val onboarding_theme_dark_desc: StringResource by 
      lazy { init_onboarding_theme_dark_desc() }

  public val onboarding_theme_description: StringResource by 
      lazy { init_onboarding_theme_description() }

  public val onboarding_theme_light: StringResource by 
      lazy { init_onboarding_theme_light() }

  public val onboarding_theme_light_desc: StringResource by 
      lazy { init_onboarding_theme_light_desc() }

  public val onboarding_theme_system: StringResource by 
      lazy { init_onboarding_theme_system() }

  public val onboarding_theme_system_desc: StringResource by 
      lazy { init_onboarding_theme_system_desc() }

  public val onboarding_theme_title: StringResource by 
      lazy { init_onboarding_theme_title() }

  public val onboarding_welcome_subtitle: StringResource by 
      lazy { init_onboarding_welcome_subtitle() }

  public val onboarding_welcome_title: StringResource by 
      lazy { init_onboarding_welcome_title() }

  public val permission_banner_message: StringResource by 
      lazy { init_permission_banner_message() }

  public val permission_banner_title: StringResource by 
      lazy { init_permission_banner_title() }

  public val permission_close: StringResource by 
      lazy { init_permission_close() }

  public val permission_exact_alarm_instructions: StringResource by 
      lazy { init_permission_exact_alarm_instructions() }

  public val permission_exact_alarm_rationale: StringResource by 
      lazy { init_permission_exact_alarm_rationale() }

  public val permission_exact_alarm_title: StringResource by 
      lazy { init_permission_exact_alarm_title() }

  public val permission_fix: StringResource by 
      lazy { init_permission_fix() }

  public val permission_fix_message: StringResource by 
      lazy { init_permission_fix_message() }

  public val permission_fix_title: StringResource by 
      lazy { init_permission_fix_title() }

  public val permission_grant: StringResource by 
      lazy { init_permission_grant() }

  public val permission_grant_exact_alarms: StringResource by 
      lazy { init_permission_grant_exact_alarms() }

  public val permission_grant_notifications: StringResource by 
      lazy { init_permission_grant_notifications() }

  public val permission_not_now: StringResource by 
      lazy { init_permission_not_now() }

  public val permission_notification_rationale: StringResource by 
      lazy { init_permission_notification_rationale() }

  public val permission_notification_title: StringResource by 
      lazy { init_permission_notification_title() }

  public val permission_open_settings: StringResource by 
      lazy { init_permission_open_settings() }

  public val permission_warning_compact: StringResource by 
      lazy { init_permission_warning_compact() }

  public val pick_ethiopian: StringResource by 
      lazy { init_pick_ethiopian() }

  public val pick_gregorian: StringResource by 
      lazy { init_pick_gregorian() }

  public val placeholder_coming_soon: StringResource by 
      lazy { init_placeholder_coming_soon() }

  public val placeholder_news_events: StringResource by 
      lazy { init_placeholder_news_events() }

  public val pref_language_dialog_title: StringResource by 
      lazy { init_pref_language_dialog_title() }

  public val recurring: StringResource by 
      lazy { init_recurring() }

  public val recurring_event: StringResource by 
      lazy { init_recurring_event() }

  public val reminder: StringResource by 
      lazy { init_reminder() }

  public val reminder_minutes_before: StringResource by 
      lazy { init_reminder_minutes_before() }

  public val repeat: StringResource by 
      lazy { init_repeat() }

  public val repeat_on: StringResource by 
      lazy { init_repeat_on() }

  public val repeat_weekly: StringResource by 
      lazy { init_repeat_weekly() }

  public val screen_title_date_converter: StringResource by 
      lazy { init_screen_title_date_converter() }

  public val screen_title_events: StringResource by 
      lazy { init_screen_title_events() }

  public val screen_title_holidays: StringResource by 
      lazy { init_screen_title_holidays() }

  public val screen_title_month_calendar: StringResource by 
      lazy { init_screen_title_month_calendar() }

  public val screen_title_more: StringResource by 
      lazy { init_screen_title_more() }

  public val screen_title_settings: StringResource by 
      lazy { init_screen_title_settings() }

  public val select_end_date: StringResource by 
      lazy { init_select_end_date() }

  public val settings_calendar_display: StringResource by 
      lazy { init_settings_calendar_display() }

  public val settings_calendar_display_dialog_title: StringResource by 
      lazy { init_settings_calendar_display_dialog_title() }

  public val settings_calendar_ethiopian: StringResource by 
      lazy { init_settings_calendar_ethiopian() }

  public val settings_calendar_gregorian: StringResource by 
      lazy { init_settings_calendar_gregorian() }

  public val settings_calendar_hirji: StringResource by 
      lazy { init_settings_calendar_hirji() }

  public val settings_display_dual_calendar: StringResource by 
      lazy { init_settings_display_dual_calendar() }

  public val settings_display_two_clocks: StringResource by 
      lazy { init_settings_display_two_clocks() }

  public val settings_ethiopian_gregorian_display: StringResource by 
      lazy { init_settings_ethiopian_gregorian_display() }

  public val settings_holidays_display: StringResource by 
      lazy { init_settings_holidays_display() }

  public val settings_holidays_display_dialog_title: StringResource by 
      lazy { init_settings_holidays_display_dialog_title() }

  public val settings_orthodox_day_names_button: StringResource by 
      lazy { init_settings_orthodox_day_names_button() }

  public val settings_orthodox_day_names_dialog_title: StringResource by 
      lazy { init_settings_orthodox_day_names_dialog_title() }

  public val settings_primary_calendar: StringResource by 
      lazy { init_settings_primary_calendar() }

  public val settings_primary_timezone: StringResource by 
      lazy { init_settings_primary_timezone() }

  public val settings_secondary_calendar: StringResource by 
      lazy { init_settings_secondary_calendar() }

  public val settings_secondary_timezone: StringResource by 
      lazy { init_settings_secondary_timezone() }

  public val settings_show_cultural_holidays: StringResource by 
      lazy { init_settings_show_cultural_holidays() }

  public val settings_show_day_off_holidays: StringResource by 
      lazy { init_settings_show_day_off_holidays() }

  public val settings_show_muslim_working_holidays: StringResource by 
      lazy { init_settings_show_muslim_working_holidays() }

  public val settings_show_orthodox_day_names: StringResource by 
      lazy { init_settings_show_orthodox_day_names() }

  public val settings_show_orthodox_working_holidays: StringResource by 
      lazy { init_settings_show_orthodox_working_holidays() }

  public val settings_show_us_holidays: StringResource by 
      lazy { init_settings_show_us_holidays() }

  public val settings_timezone_hint: StringResource by 
      lazy { init_settings_timezone_hint() }

  public val settings_use_24_hour_format: StringResource by 
      lazy { init_settings_use_24_hour_format() }

  public val settings_use_geez_numbers: StringResource by 
      lazy { init_settings_use_geez_numbers() }

  public val settings_use_transparent_background: StringResource by 
      lazy { init_settings_use_transparent_background() }

  public val settings_widget_dialog_title: StringResource by 
      lazy { init_settings_widget_dialog_title() }

  public val settings_widget_options: StringResource by 
      lazy { init_settings_widget_options() }

  public val show_all: StringResource by 
      lazy { init_show_all() }

  public val show_reminder_before: StringResource by 
      lazy { init_show_reminder_before() }

  public val showing_all_events: StringResource by 
      lazy { init_showing_all_events() }

  public val showing_from: StringResource by 
      lazy { init_showing_from() }

  public val showing_from_to: StringResource by 
      lazy { init_showing_from_to() }

  public val showing_until: StringResource by 
      lazy { init_showing_until() }

  public val start_time: StringResource by 
      lazy { init_start_time() }

  public val tap_plus_to_add_event: StringResource by 
      lazy { init_tap_plus_to_add_event() }

  public val time_ago: StringResource by 
      lazy { init_time_ago() }

  public val time_day_plural: StringResource by 
      lazy { init_time_day_plural() }

  public val time_day_singular: StringResource by 
      lazy { init_time_day_singular() }

  public val time_hour_plural: StringResource by 
      lazy { init_time_hour_plural() }

  public val time_hour_singular: StringResource by 
      lazy { init_time_hour_singular() }

  public val time_in_a_moment: StringResource by 
      lazy { init_time_in_a_moment() }

  public val time_in_future: StringResource by 
      lazy { init_time_in_future() }

  public val time_just_now: StringResource by 
      lazy { init_time_just_now() }

  public val time_minute_plural: StringResource by 
      lazy { init_time_minute_plural() }

  public val time_minute_singular: StringResource by 
      lazy { init_time_minute_singular() }

  public val time_month_plural: StringResource by 
      lazy { init_time_month_plural() }

  public val time_month_singular: StringResource by 
      lazy { init_time_month_singular() }

  public val time_week_plural: StringResource by 
      lazy { init_time_week_plural() }

  public val time_week_singular: StringResource by 
      lazy { init_time_week_singular() }

  public val time_year_plural: StringResource by 
      lazy { init_time_year_plural() }

  public val time_year_singular: StringResource by 
      lazy { init_time_year_singular() }

  public val title: StringResource by 
      lazy { init_title() }

  public val title_required: StringResource by 
      lazy { init_title_required() }

  public val until: StringResource by 
      lazy { init_until() }

  public val until_date: StringResource by 
      lazy { init_until_date() }

  public val update: StringResource by 
      lazy { init_update() }

  public val weekly: StringResource by 
      lazy { init_weekly() }

  public val widget_description: StringResource by 
      lazy { init_widget_description() }
}

@InternalResourceApi
internal fun _collectCommonMainString0Resources(map: MutableMap<String, StringResource>) {
  map.put("about_us_app_version", CommonMainString0.about_us_app_version)
  map.put("about_us_build_time", CommonMainString0.about_us_build_time)
  map.put("about_us_contact_us", CommonMainString0.about_us_contact_us)
  map.put("about_us_developed_by", CommonMainString0.about_us_developed_by)
  map.put("about_us_developer", CommonMainString0.about_us_developer)
  map.put("about_us_dialog_title", CommonMainString0.about_us_dialog_title)
  map.put("about_us_privacy_policy", CommonMainString0.about_us_privacy_policy)
  map.put("about_us_rate_app", CommonMainString0.about_us_rate_app)
  map.put("about_us_terms_of_service", CommonMainString0.about_us_terms_of_service)
  map.put("add_event", CommonMainString0.add_event)
  map.put("add_reminder", CommonMainString0.add_reminder)
  map.put("all_day_event", CommonMainString0.all_day_event)
  map.put("at_time_of_event", CommonMainString0.at_time_of_event)
  map.put("button_calculate_difference", CommonMainString0.button_calculate_difference)
  map.put("button_cancel", CommonMainString0.button_cancel)
  map.put("button_close", CommonMainString0.button_close)
  map.put("button_create_reminder", CommonMainString0.button_create_reminder)
  map.put("button_next", CommonMainString0.button_next)
  map.put("button_ok", CommonMainString0.button_ok)
  map.put("button_prev", CommonMainString0.button_prev)
  map.put("button_to_ethiopian", CommonMainString0.button_to_ethiopian)
  map.put("button_to_gregorian", CommonMainString0.button_to_gregorian)
  map.put("button_today", CommonMainString0.button_today)
  map.put("button_view_events", CommonMainString0.button_view_events)
  map.put("cancel", CommonMainString0.cancel)
  map.put("category", CommonMainString0.category)
  map.put("cd_back", CommonMainString0.cd_back)
  map.put("cd_calendar_state", CommonMainString0.cd_calendar_state)
  map.put("cd_holiday_info", CommonMainString0.cd_holiday_info)
  map.put("cd_navigate", CommonMainString0.cd_navigate)
  map.put("cd_next_year", CommonMainString0.cd_next_year)
  map.put("cd_pick_date", CommonMainString0.cd_pick_date)
  map.put("cd_previous_year", CommonMainString0.cd_previous_year)
  map.put("cd_selected", CommonMainString0.cd_selected)
  map.put("conversion_result_title", CommonMainString0.conversion_result_title)
  map.put("create", CommonMainString0.create)
  map.put("date", CommonMainString0.date)
  map.put("date_details_dialog_title", CommonMainString0.date_details_dialog_title)
  map.put("date_difference_result_title", CommonMainString0.date_difference_result_title)
  map.put("delete", CommonMainString0.delete)
  map.put("delete_event", CommonMainString0.delete_event)
  map.put("delete_event_message", CommonMainString0.delete_event_message)
  map.put("delete_event_title", CommonMainString0.delete_event_title)
  map.put("description_optional", CommonMainString0.description_optional)
  map.put("dialog_title_select_ethiopian_date",
      CommonMainString0.dialog_title_select_ethiopian_date)
  map.put("edit_event", CommonMainString0.edit_event)
  map.put("empty_no_holidays_display", CommonMainString0.empty_no_holidays_display)
  map.put("empty_no_holidays_month", CommonMainString0.empty_no_holidays_month)
  map.put("end_time", CommonMainString0.end_time)
  map.put("ends", CommonMainString0.ends)
  map.put("error_loading_calendar", CommonMainString0.error_loading_calendar)
  map.put("error_loading_events", CommonMainString0.error_loading_events)
  map.put("event_date_all_day", CommonMainString0.event_date_all_day)
  map.put("filter", CommonMainString0.filter)
  map.put("filter_muslim", CommonMainString0.filter_muslim)
  map.put("filter_orthodox", CommonMainString0.filter_orthodox)
  map.put("filter_public", CommonMainString0.filter_public)
  map.put("holiday_derg", CommonMainString0.holiday_derg)
  map.put("holiday_derg_celebration", CommonMainString0.holiday_derg_celebration)
  map.put("holiday_derg_history", CommonMainString0.holiday_derg_history)
  map.put("holiday_info_dialog_checkbox", CommonMainString0.holiday_info_dialog_checkbox)
  map.put("holiday_info_dialog_message", CommonMainString0.holiday_info_dialog_message)
  map.put("holiday_info_dialog_title", CommonMainString0.holiday_info_dialog_title)
  map.put("holiday_muslim_arafat", CommonMainString0.holiday_muslim_arafat)
  map.put("holiday_muslim_arafat_celebration", CommonMainString0.holiday_muslim_arafat_celebration)
  map.put("holiday_muslim_arafat_history", CommonMainString0.holiday_muslim_arafat_history)
  map.put("holiday_muslim_ashura", CommonMainString0.holiday_muslim_ashura)
  map.put("holiday_muslim_ashura_celebration", CommonMainString0.holiday_muslim_ashura_celebration)
  map.put("holiday_muslim_ashura_history", CommonMainString0.holiday_muslim_ashura_history)
  map.put("holiday_muslim_eid_adha", CommonMainString0.holiday_muslim_eid_adha)
  map.put("holiday_muslim_eid_adha_celebration",
      CommonMainString0.holiday_muslim_eid_adha_celebration)
  map.put("holiday_muslim_eid_adha_history", CommonMainString0.holiday_muslim_eid_adha_history)
  map.put("holiday_muslim_eid_fitr", CommonMainString0.holiday_muslim_eid_fitr)
  map.put("holiday_muslim_eid_fitr_celebration",
      CommonMainString0.holiday_muslim_eid_fitr_celebration)
  map.put("holiday_muslim_eid_fitr_history", CommonMainString0.holiday_muslim_eid_fitr_history)
  map.put("holiday_muslim_hijri_new_year", CommonMainString0.holiday_muslim_hijri_new_year)
  map.put("holiday_muslim_hijri_new_year_celebration",
      CommonMainString0.holiday_muslim_hijri_new_year_celebration)
  map.put("holiday_muslim_hijri_new_year_history",
      CommonMainString0.holiday_muslim_hijri_new_year_history)
  map.put("holiday_muslim_isra_miraj", CommonMainString0.holiday_muslim_isra_miraj)
  map.put("holiday_muslim_isra_miraj_celebration",
      CommonMainString0.holiday_muslim_isra_miraj_celebration)
  map.put("holiday_muslim_isra_miraj_history", CommonMainString0.holiday_muslim_isra_miraj_history)
  map.put("holiday_muslim_mawlid", CommonMainString0.holiday_muslim_mawlid)
  map.put("holiday_muslim_mawlid_celebration", CommonMainString0.holiday_muslim_mawlid_celebration)
  map.put("holiday_muslim_mawlid_history", CommonMainString0.holiday_muslim_mawlid_history)
  map.put("holiday_muslim_mid_shaban", CommonMainString0.holiday_muslim_mid_shaban)
  map.put("holiday_muslim_mid_shaban_celebration",
      CommonMainString0.holiday_muslim_mid_shaban_celebration)
  map.put("holiday_muslim_mid_shaban_history", CommonMainString0.holiday_muslim_mid_shaban_history)
  map.put("holiday_muslim_ramadan_start", CommonMainString0.holiday_muslim_ramadan_start)
  map.put("holiday_muslim_ramadan_start_celebration",
      CommonMainString0.holiday_muslim_ramadan_start_celebration)
  map.put("holiday_muslim_ramadan_start_history",
      CommonMainString0.holiday_muslim_ramadan_start_history)
  map.put("holiday_orthodox_abiy_tsom", CommonMainString0.holiday_orthodox_abiy_tsom)
  map.put("holiday_orthodox_abiy_tsom_celebration",
      CommonMainString0.holiday_orthodox_abiy_tsom_celebration)
  map.put("holiday_orthodox_abiy_tsom_history",
      CommonMainString0.holiday_orthodox_abiy_tsom_history)
  map.put("holiday_orthodox_debre_tabor", CommonMainString0.holiday_orthodox_debre_tabor)
  map.put("holiday_orthodox_debre_tabor_celebration",
      CommonMainString0.holiday_orthodox_debre_tabor_celebration)
  map.put("holiday_orthodox_debre_tabor_history",
      CommonMainString0.holiday_orthodox_debre_tabor_history)
  map.put("holiday_orthodox_debre_zeit", CommonMainString0.holiday_orthodox_debre_zeit)
  map.put("holiday_orthodox_debre_zeit_celebration",
      CommonMainString0.holiday_orthodox_debre_zeit_celebration)
  map.put("holiday_orthodox_debre_zeit_history",
      CommonMainString0.holiday_orthodox_debre_zeit_history)
  map.put("holiday_orthodox_erget", CommonMainString0.holiday_orthodox_erget)
  map.put("holiday_orthodox_erget_celebration",
      CommonMainString0.holiday_orthodox_erget_celebration)
  map.put("holiday_orthodox_erget_history", CommonMainString0.holiday_orthodox_erget_history)
  map.put("holiday_orthodox_fasika", CommonMainString0.holiday_orthodox_fasika)
  map.put("holiday_orthodox_fasika_celebration",
      CommonMainString0.holiday_orthodox_fasika_celebration)
  map.put("holiday_orthodox_fasika_history", CommonMainString0.holiday_orthodox_fasika_history)
  map.put("holiday_orthodox_filseta", CommonMainString0.holiday_orthodox_filseta)
  map.put("holiday_orthodox_filseta_celebration",
      CommonMainString0.holiday_orthodox_filseta_celebration)
  map.put("holiday_orthodox_filseta_history", CommonMainString0.holiday_orthodox_filseta_history)
  map.put("holiday_orthodox_ghad", CommonMainString0.holiday_orthodox_ghad)
  map.put("holiday_orthodox_ghad_celebration", CommonMainString0.holiday_orthodox_ghad_celebration)
  map.put("holiday_orthodox_ghad_history", CommonMainString0.holiday_orthodox_ghad_history)
  map.put("holiday_orthodox_hosanna", CommonMainString0.holiday_orthodox_hosanna)
  map.put("holiday_orthodox_hosanna_celebration",
      CommonMainString0.holiday_orthodox_hosanna_celebration)
  map.put("holiday_orthodox_hosanna_history", CommonMainString0.holiday_orthodox_hosanna_history)
  map.put("holiday_orthodox_kana_zegelila", CommonMainString0.holiday_orthodox_kana_zegelila)
  map.put("holiday_orthodox_kana_zegelila_celebration",
      CommonMainString0.holiday_orthodox_kana_zegelila_celebration)
  map.put("holiday_orthodox_kana_zegelila_history",
      CommonMainString0.holiday_orthodox_kana_zegelila_history)
  map.put("holiday_orthodox_lideta_mariam", CommonMainString0.holiday_orthodox_lideta_mariam)
  map.put("holiday_orthodox_lideta_mariam_celebration",
      CommonMainString0.holiday_orthodox_lideta_mariam_celebration)
  map.put("holiday_orthodox_lideta_mariam_history",
      CommonMainString0.holiday_orthodox_lideta_mariam_history)
  map.put("holiday_orthodox_nineveh", CommonMainString0.holiday_orthodox_nineveh)
  map.put("holiday_orthodox_nineveh_celebration",
      CommonMainString0.holiday_orthodox_nineveh_celebration)
  map.put("holiday_orthodox_nineveh_history", CommonMainString0.holiday_orthodox_nineveh_history)
  map.put("holiday_orthodox_peraklitos", CommonMainString0.holiday_orthodox_peraklitos)
  map.put("holiday_orthodox_peraklitos_celebration",
      CommonMainString0.holiday_orthodox_peraklitos_celebration)
  map.put("holiday_orthodox_peraklitos_history",
      CommonMainString0.holiday_orthodox_peraklitos_history)
  map.put("holiday_orthodox_rikbe_kahinat", CommonMainString0.holiday_orthodox_rikbe_kahinat)
  map.put("holiday_orthodox_rikbe_kahinat_celebration",
      CommonMainString0.holiday_orthodox_rikbe_kahinat_celebration)
  map.put("holiday_orthodox_rikbe_kahinat_history",
      CommonMainString0.holiday_orthodox_rikbe_kahinat_history)
  map.put("holiday_orthodox_siklet", CommonMainString0.holiday_orthodox_siklet)
  map.put("holiday_orthodox_siklet_celebration",
      CommonMainString0.holiday_orthodox_siklet_celebration)
  map.put("holiday_orthodox_siklet_history", CommonMainString0.holiday_orthodox_siklet_history)
  map.put("holiday_orthodox_tensae", CommonMainString0.holiday_orthodox_tensae)
  map.put("holiday_orthodox_tensae_celebration",
      CommonMainString0.holiday_orthodox_tensae_celebration)
  map.put("holiday_orthodox_tensae_history", CommonMainString0.holiday_orthodox_tensae_history)
  map.put("holiday_orthodox_tsome_dihnet", CommonMainString0.holiday_orthodox_tsome_dihnet)
  map.put("holiday_orthodox_tsome_dihnet_celebration",
      CommonMainString0.holiday_orthodox_tsome_dihnet_celebration)
  map.put("holiday_orthodox_tsome_dihnet_history",
      CommonMainString0.holiday_orthodox_tsome_dihnet_history)
  map.put("holiday_orthodox_tsome_hawariat", CommonMainString0.holiday_orthodox_tsome_hawariat)
  map.put("holiday_orthodox_tsome_hawariat_celebration",
      CommonMainString0.holiday_orthodox_tsome_hawariat_celebration)
  map.put("holiday_orthodox_tsome_hawariat_history",
      CommonMainString0.holiday_orthodox_tsome_hawariat_history)
  map.put("holiday_public_adwa", CommonMainString0.holiday_public_adwa)
  map.put("holiday_public_adwa_celebration", CommonMainString0.holiday_public_adwa_celebration)
  map.put("holiday_public_adwa_history", CommonMainString0.holiday_public_adwa_history)
  map.put("holiday_public_christian_genna", CommonMainString0.holiday_public_christian_genna)
  map.put("holiday_public_christian_genna_celebration",
      CommonMainString0.holiday_public_christian_genna_celebration)
  map.put("holiday_public_christian_genna_history",
      CommonMainString0.holiday_public_christian_genna_history)
  map.put("holiday_public_christian_meskel", CommonMainString0.holiday_public_christian_meskel)
  map.put("holiday_public_christian_meskel_celebration",
      CommonMainString0.holiday_public_christian_meskel_celebration)
  map.put("holiday_public_christian_meskel_history",
      CommonMainString0.holiday_public_christian_meskel_history)
  map.put("holiday_public_christian_timket", CommonMainString0.holiday_public_christian_timket)
  map.put("holiday_public_christian_timket_celebration",
      CommonMainString0.holiday_public_christian_timket_celebration)
  map.put("holiday_public_christian_timket_history",
      CommonMainString0.holiday_public_christian_timket_history)
  map.put("holiday_public_enkutatash", CommonMainString0.holiday_public_enkutatash)
  map.put("holiday_public_enkutatash_celebration",
      CommonMainString0.holiday_public_enkutatash_celebration)
  map.put("holiday_public_enkutatash_history", CommonMainString0.holiday_public_enkutatash_history)
  map.put("holiday_public_labour_day", CommonMainString0.holiday_public_labour_day)
  map.put("holiday_public_labour_day_celebration",
      CommonMainString0.holiday_public_labour_day_celebration)
  map.put("holiday_public_labour_day_history", CommonMainString0.holiday_public_labour_day_history)
  map.put("holiday_public_patriots_day", CommonMainString0.holiday_public_patriots_day)
  map.put("holiday_public_patriots_day_celebration",
      CommonMainString0.holiday_public_patriots_day_celebration)
  map.put("holiday_public_patriots_day_history",
      CommonMainString0.holiday_public_patriots_day_history)
  map.put("hour_before_1", CommonMainString0.hour_before_1)
  map.put("label_date_difference", CommonMainString0.label_date_difference)
  map.put("label_day_off", CommonMainString0.label_day_off)
  map.put("label_difference_result", CommonMainString0.label_difference_result)
  map.put("label_ec_suffix", CommonMainString0.label_ec_suffix)
  map.put("label_end_date", CommonMainString0.label_end_date)
  map.put("label_ethiopian_date", CommonMainString0.label_ethiopian_date)
  map.put("label_filter_by_type", CommonMainString0.label_filter_by_type)
  map.put("label_from_ethiopian", CommonMainString0.label_from_ethiopian)
  map.put("label_from_gregorian", CommonMainString0.label_from_gregorian)
  map.put("label_gregorian_date", CommonMainString0.label_gregorian_date)
  map.put("label_holidays", CommonMainString0.label_holidays)
  map.put("label_start_date", CommonMainString0.label_start_date)
  map.put("label_theme_color", CommonMainString0.label_theme_color)
  map.put("label_theme_mode", CommonMainString0.label_theme_mode)
  map.put("menu_about_us", CommonMainString0.menu_about_us)
  map.put("menu_additional_settings", CommonMainString0.menu_additional_settings)
  map.put("menu_color_theme", CommonMainString0.menu_color_theme)
  map.put("menu_language", CommonMainString0.menu_language)
  map.put("menu_notifications", CommonMainString0.menu_notifications)
  map.put("menu_privacy_policy", CommonMainString0.menu_privacy_policy)
  map.put("menu_share_app", CommonMainString0.menu_share_app)
  map.put("minutes_before_15", CommonMainString0.minutes_before_15)
  map.put("minutes_before_30", CommonMainString0.minutes_before_30)
  map.put("minutes_before_5", CommonMainString0.minutes_before_5)
  map.put("nav_convert", CommonMainString0.nav_convert)
  map.put("nav_event", CommonMainString0.nav_event)
  map.put("nav_holiday", CommonMainString0.nav_holiday)
  map.put("nav_month", CommonMainString0.nav_month)
  map.put("nav_more", CommonMainString0.nav_more)
  map.put("never", CommonMainString0.never)
  map.put("no_events", CommonMainString0.no_events)
  map.put("no_repeat", CommonMainString0.no_repeat)
  map.put("notification_banner_dismiss", CommonMainString0.notification_banner_dismiss)
  map.put("notification_banner_enable", CommonMainString0.notification_banner_enable)
  map.put("notification_banner_message", CommonMainString0.notification_banner_message)
  map.put("notification_banner_title", CommonMainString0.notification_banner_title)
  map.put("ok", CommonMainString0.ok)
  map.put("onboarding_calendar_description", CommonMainString0.onboarding_calendar_description)
  map.put("onboarding_calendar_dual_display", CommonMainString0.onboarding_calendar_dual_display)
  map.put("onboarding_calendar_dual_display_desc",
      CommonMainString0.onboarding_calendar_dual_display_desc)
  map.put("onboarding_calendar_tip", CommonMainString0.onboarding_calendar_tip)
  map.put("onboarding_calendar_title", CommonMainString0.onboarding_calendar_title)
  map.put("onboarding_done", CommonMainString0.onboarding_done)
  map.put("onboarding_feature_date_converter", CommonMainString0.onboarding_feature_date_converter)
  map.put("onboarding_feature_dual_calendar", CommonMainString0.onboarding_feature_dual_calendar)
  map.put("onboarding_feature_holidays", CommonMainString0.onboarding_feature_holidays)
  map.put("onboarding_feature_multilingual", CommonMainString0.onboarding_feature_multilingual)
  map.put("onboarding_feature_reminders", CommonMainString0.onboarding_feature_reminders)
  map.put("onboarding_get_started", CommonMainString0.onboarding_get_started)
  map.put("onboarding_holidays_description", CommonMainString0.onboarding_holidays_description)
  map.put("onboarding_holidays_muslim", CommonMainString0.onboarding_holidays_muslim)
  map.put("onboarding_holidays_muslim_desc", CommonMainString0.onboarding_holidays_muslim_desc)
  map.put("onboarding_holidays_orthodox", CommonMainString0.onboarding_holidays_orthodox)
  map.put("onboarding_holidays_orthodox_days", CommonMainString0.onboarding_holidays_orthodox_days)
  map.put("onboarding_holidays_orthodox_days_desc",
      CommonMainString0.onboarding_holidays_orthodox_days_desc)
  map.put("onboarding_holidays_orthodox_desc", CommonMainString0.onboarding_holidays_orthodox_desc)
  map.put("onboarding_holidays_public", CommonMainString0.onboarding_holidays_public)
  map.put("onboarding_holidays_public_desc", CommonMainString0.onboarding_holidays_public_desc)
  map.put("onboarding_holidays_title", CommonMainString0.onboarding_holidays_title)
  map.put("onboarding_language_description", CommonMainString0.onboarding_language_description)
  map.put("onboarding_language_title", CommonMainString0.onboarding_language_title)
  map.put("onboarding_next", CommonMainString0.onboarding_next)
  map.put("onboarding_skip", CommonMainString0.onboarding_skip)
  map.put("onboarding_theme_dark", CommonMainString0.onboarding_theme_dark)
  map.put("onboarding_theme_dark_desc", CommonMainString0.onboarding_theme_dark_desc)
  map.put("onboarding_theme_description", CommonMainString0.onboarding_theme_description)
  map.put("onboarding_theme_light", CommonMainString0.onboarding_theme_light)
  map.put("onboarding_theme_light_desc", CommonMainString0.onboarding_theme_light_desc)
  map.put("onboarding_theme_system", CommonMainString0.onboarding_theme_system)
  map.put("onboarding_theme_system_desc", CommonMainString0.onboarding_theme_system_desc)
  map.put("onboarding_theme_title", CommonMainString0.onboarding_theme_title)
  map.put("onboarding_welcome_subtitle", CommonMainString0.onboarding_welcome_subtitle)
  map.put("onboarding_welcome_title", CommonMainString0.onboarding_welcome_title)
  map.put("permission_banner_message", CommonMainString0.permission_banner_message)
  map.put("permission_banner_title", CommonMainString0.permission_banner_title)
  map.put("permission_close", CommonMainString0.permission_close)
  map.put("permission_exact_alarm_instructions",
      CommonMainString0.permission_exact_alarm_instructions)
  map.put("permission_exact_alarm_rationale", CommonMainString0.permission_exact_alarm_rationale)
  map.put("permission_exact_alarm_title", CommonMainString0.permission_exact_alarm_title)
  map.put("permission_fix", CommonMainString0.permission_fix)
  map.put("permission_fix_message", CommonMainString0.permission_fix_message)
  map.put("permission_fix_title", CommonMainString0.permission_fix_title)
  map.put("permission_grant", CommonMainString0.permission_grant)
  map.put("permission_grant_exact_alarms", CommonMainString0.permission_grant_exact_alarms)
  map.put("permission_grant_notifications", CommonMainString0.permission_grant_notifications)
  map.put("permission_not_now", CommonMainString0.permission_not_now)
  map.put("permission_notification_rationale", CommonMainString0.permission_notification_rationale)
  map.put("permission_notification_title", CommonMainString0.permission_notification_title)
  map.put("permission_open_settings", CommonMainString0.permission_open_settings)
  map.put("permission_warning_compact", CommonMainString0.permission_warning_compact)
  map.put("pick_ethiopian", CommonMainString0.pick_ethiopian)
  map.put("pick_gregorian", CommonMainString0.pick_gregorian)
  map.put("placeholder_coming_soon", CommonMainString0.placeholder_coming_soon)
  map.put("placeholder_news_events", CommonMainString0.placeholder_news_events)
  map.put("pref_language_dialog_title", CommonMainString0.pref_language_dialog_title)
  map.put("recurring", CommonMainString0.recurring)
  map.put("recurring_event", CommonMainString0.recurring_event)
  map.put("reminder", CommonMainString0.reminder)
  map.put("reminder_minutes_before", CommonMainString0.reminder_minutes_before)
  map.put("repeat", CommonMainString0.repeat)
  map.put("repeat_on", CommonMainString0.repeat_on)
  map.put("repeat_weekly", CommonMainString0.repeat_weekly)
  map.put("screen_title_date_converter", CommonMainString0.screen_title_date_converter)
  map.put("screen_title_events", CommonMainString0.screen_title_events)
  map.put("screen_title_holidays", CommonMainString0.screen_title_holidays)
  map.put("screen_title_month_calendar", CommonMainString0.screen_title_month_calendar)
  map.put("screen_title_more", CommonMainString0.screen_title_more)
  map.put("screen_title_settings", CommonMainString0.screen_title_settings)
  map.put("select_end_date", CommonMainString0.select_end_date)
  map.put("settings_calendar_display", CommonMainString0.settings_calendar_display)
  map.put("settings_calendar_display_dialog_title",
      CommonMainString0.settings_calendar_display_dialog_title)
  map.put("settings_calendar_ethiopian", CommonMainString0.settings_calendar_ethiopian)
  map.put("settings_calendar_gregorian", CommonMainString0.settings_calendar_gregorian)
  map.put("settings_calendar_hirji", CommonMainString0.settings_calendar_hirji)
  map.put("settings_display_dual_calendar", CommonMainString0.settings_display_dual_calendar)
  map.put("settings_display_two_clocks", CommonMainString0.settings_display_two_clocks)
  map.put("settings_ethiopian_gregorian_display",
      CommonMainString0.settings_ethiopian_gregorian_display)
  map.put("settings_holidays_display", CommonMainString0.settings_holidays_display)
  map.put("settings_holidays_display_dialog_title",
      CommonMainString0.settings_holidays_display_dialog_title)
  map.put("settings_orthodox_day_names_button",
      CommonMainString0.settings_orthodox_day_names_button)
  map.put("settings_orthodox_day_names_dialog_title",
      CommonMainString0.settings_orthodox_day_names_dialog_title)
  map.put("settings_primary_calendar", CommonMainString0.settings_primary_calendar)
  map.put("settings_primary_timezone", CommonMainString0.settings_primary_timezone)
  map.put("settings_secondary_calendar", CommonMainString0.settings_secondary_calendar)
  map.put("settings_secondary_timezone", CommonMainString0.settings_secondary_timezone)
  map.put("settings_show_cultural_holidays", CommonMainString0.settings_show_cultural_holidays)
  map.put("settings_show_day_off_holidays", CommonMainString0.settings_show_day_off_holidays)
  map.put("settings_show_muslim_working_holidays",
      CommonMainString0.settings_show_muslim_working_holidays)
  map.put("settings_show_orthodox_day_names", CommonMainString0.settings_show_orthodox_day_names)
  map.put("settings_show_orthodox_working_holidays",
      CommonMainString0.settings_show_orthodox_working_holidays)
  map.put("settings_show_us_holidays", CommonMainString0.settings_show_us_holidays)
  map.put("settings_timezone_hint", CommonMainString0.settings_timezone_hint)
  map.put("settings_use_24_hour_format", CommonMainString0.settings_use_24_hour_format)
  map.put("settings_use_geez_numbers", CommonMainString0.settings_use_geez_numbers)
  map.put("settings_use_transparent_background",
      CommonMainString0.settings_use_transparent_background)
  map.put("settings_widget_dialog_title", CommonMainString0.settings_widget_dialog_title)
  map.put("settings_widget_options", CommonMainString0.settings_widget_options)
  map.put("show_all", CommonMainString0.show_all)
  map.put("show_reminder_before", CommonMainString0.show_reminder_before)
  map.put("showing_all_events", CommonMainString0.showing_all_events)
  map.put("showing_from", CommonMainString0.showing_from)
  map.put("showing_from_to", CommonMainString0.showing_from_to)
  map.put("showing_until", CommonMainString0.showing_until)
  map.put("start_time", CommonMainString0.start_time)
  map.put("tap_plus_to_add_event", CommonMainString0.tap_plus_to_add_event)
  map.put("time_ago", CommonMainString0.time_ago)
  map.put("time_day_plural", CommonMainString0.time_day_plural)
  map.put("time_day_singular", CommonMainString0.time_day_singular)
  map.put("time_hour_plural", CommonMainString0.time_hour_plural)
  map.put("time_hour_singular", CommonMainString0.time_hour_singular)
  map.put("time_in_a_moment", CommonMainString0.time_in_a_moment)
  map.put("time_in_future", CommonMainString0.time_in_future)
  map.put("time_just_now", CommonMainString0.time_just_now)
  map.put("time_minute_plural", CommonMainString0.time_minute_plural)
  map.put("time_minute_singular", CommonMainString0.time_minute_singular)
  map.put("time_month_plural", CommonMainString0.time_month_plural)
  map.put("time_month_singular", CommonMainString0.time_month_singular)
  map.put("time_week_plural", CommonMainString0.time_week_plural)
  map.put("time_week_singular", CommonMainString0.time_week_singular)
  map.put("time_year_plural", CommonMainString0.time_year_plural)
  map.put("time_year_singular", CommonMainString0.time_year_singular)
  map.put("title", CommonMainString0.title)
  map.put("title_required", CommonMainString0.title_required)
  map.put("until", CommonMainString0.until)
  map.put("until_date", CommonMainString0.until_date)
  map.put("update", CommonMainString0.update)
  map.put("weekly", CommonMainString0.weekly)
  map.put("widget_description", CommonMainString0.widget_description)
}

public val Res.string.about_us_app_version: StringResource
  get() = CommonMainString0.about_us_app_version

private fun init_about_us_app_version(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:about_us_app_version", "about_us_app_version",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 485, 44),
    )
)

public val Res.string.about_us_build_time: StringResource
  get() = CommonMainString0.about_us_build_time

private fun init_about_us_build_time(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:about_us_build_time", "about_us_build_time",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 530, 43),
    )
)

public val Res.string.about_us_contact_us: StringResource
  get() = CommonMainString0.about_us_contact_us

private fun init_about_us_contact_us(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:about_us_contact_us", "about_us_contact_us",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 574, 43),
    )
)

public val Res.string.about_us_developed_by: StringResource
  get() = CommonMainString0.about_us_developed_by

private fun init_about_us_developed_by(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:about_us_developed_by", "about_us_developed_by",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 618, 61),
    )
)

public val Res.string.about_us_developer: StringResource
  get() = CommonMainString0.about_us_developer

private fun init_about_us_developer(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:about_us_developer", "about_us_developer",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 680, 38),
    )
)

public val Res.string.about_us_dialog_title: StringResource
  get() = CommonMainString0.about_us_dialog_title

private fun init_about_us_dialog_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:about_us_dialog_title", "about_us_dialog_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 719, 41),
    )
)

public val Res.string.about_us_privacy_policy: StringResource
  get() = CommonMainString0.about_us_privacy_policy

private fun init_about_us_privacy_policy(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:about_us_privacy_policy", "about_us_privacy_policy",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 761, 51),
    )
)

public val Res.string.about_us_rate_app: StringResource
  get() = CommonMainString0.about_us_rate_app

private fun init_about_us_rate_app(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:about_us_rate_app", "about_us_rate_app",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 813, 37),
    )
)

public val Res.string.about_us_terms_of_service: StringResource
  get() = CommonMainString0.about_us_terms_of_service

private fun init_about_us_terms_of_service(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:about_us_terms_of_service", "about_us_terms_of_service",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 851, 57),
    )
)

public val Res.string.add_event: StringResource
  get() = CommonMainString0.add_event

private fun init_add_event(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:add_event", "add_event",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 909, 29),
    )
)

public val Res.string.add_reminder: StringResource
  get() = CommonMainString0.add_reminder

private fun init_add_reminder(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:add_reminder", "add_reminder",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 939, 36),
    )
)

public val Res.string.all_day_event: StringResource
  get() = CommonMainString0.all_day_event

private fun init_all_day_event(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:all_day_event", "all_day_event",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 976, 41),
    )
)

public val Res.string.at_time_of_event: StringResource
  get() = CommonMainString0.at_time_of_event

private fun init_at_time_of_event(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:at_time_of_event", "at_time_of_event",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1018,
    48),
    )
)

public val Res.string.button_calculate_difference: StringResource
  get() = CommonMainString0.button_calculate_difference

private fun init_button_calculate_difference(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:button_calculate_difference", "button_calculate_difference",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1067,
    63),
    )
)

public val Res.string.button_cancel: StringResource
  get() = CommonMainString0.button_cancel

private fun init_button_cancel(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:button_cancel", "button_cancel",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1131,
    29),
    )
)

public val Res.string.button_close: StringResource
  get() = CommonMainString0.button_close

private fun init_button_close(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:button_close", "button_close",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1161,
    28),
    )
)

public val Res.string.button_create_reminder: StringResource
  get() = CommonMainString0.button_create_reminder

private fun init_button_create_reminder(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:button_create_reminder", "button_create_reminder",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1190,
    50),
    )
)

public val Res.string.button_next: StringResource
  get() = CommonMainString0.button_next

private fun init_button_next(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:button_next", "button_next",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1241,
    27),
    )
)

public val Res.string.button_ok: StringResource
  get() = CommonMainString0.button_ok

private fun init_button_ok(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:button_ok", "button_ok",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1269,
    21),
    )
)

public val Res.string.button_prev: StringResource
  get() = CommonMainString0.button_prev

private fun init_button_prev(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:button_prev", "button_prev",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1291,
    27),
    )
)

public val Res.string.button_to_ethiopian: StringResource
  get() = CommonMainString0.button_to_ethiopian

private fun init_button_to_ethiopian(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:button_to_ethiopian", "button_to_ethiopian",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1319,
    43),
    )
)

public val Res.string.button_to_gregorian: StringResource
  get() = CommonMainString0.button_to_gregorian

private fun init_button_to_gregorian(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:button_to_gregorian", "button_to_gregorian",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1363,
    43),
    )
)

public val Res.string.button_today: StringResource
  get() = CommonMainString0.button_today

private fun init_button_today(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:button_today", "button_today",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1407,
    28),
    )
)

public val Res.string.button_view_events: StringResource
  get() = CommonMainString0.button_view_events

private fun init_button_view_events(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:button_view_events", "button_view_events",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1436,
    42),
    )
)

public val Res.string.cancel: StringResource
  get() = CommonMainString0.cancel

private fun init_cancel(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:cancel", "cancel",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1479,
    22),
    )
)

public val Res.string.category: StringResource
  get() = CommonMainString0.category

private fun init_category(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:category", "category",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1502,
    28),
    )
)

public val Res.string.cd_back: StringResource
  get() = CommonMainString0.cd_back

private fun init_cd_back(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:cd_back", "cd_back",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1531,
    23),
    )
)

public val Res.string.cd_calendar_state: StringResource
  get() = CommonMainString0.cd_calendar_state

private fun init_cd_calendar_state(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:cd_calendar_state", "cd_calendar_state",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1555,
    81),
    )
)

public val Res.string.cd_holiday_info: StringResource
  get() = CommonMainString0.cd_holiday_info

private fun init_cd_holiday_info(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:cd_holiday_info", "cd_holiday_info",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1637,
    63),
    )
)

public val Res.string.cd_navigate: StringResource
  get() = CommonMainString0.cd_navigate

private fun init_cd_navigate(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:cd_navigate", "cd_navigate",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1701,
    31),
    )
)

public val Res.string.cd_next_year: StringResource
  get() = CommonMainString0.cd_next_year

private fun init_cd_next_year(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:cd_next_year", "cd_next_year",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1733,
    32),
    )
)

public val Res.string.cd_pick_date: StringResource
  get() = CommonMainString0.cd_pick_date

private fun init_cd_pick_date(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:cd_pick_date", "cd_pick_date",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1766,
    32),
    )
)

public val Res.string.cd_previous_year: StringResource
  get() = CommonMainString0.cd_previous_year

private fun init_cd_previous_year(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:cd_previous_year", "cd_previous_year",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1799,
    44),
    )
)

public val Res.string.cd_selected: StringResource
  get() = CommonMainString0.cd_selected

private fun init_cd_selected(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:cd_selected", "cd_selected",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1844,
    31),
    )
)

public val Res.string.conversion_result_title: StringResource
  get() = CommonMainString0.conversion_result_title

private fun init_conversion_result_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:conversion_result_title", "conversion_result_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1876,
    55),
    )
)

public val Res.string.create: StringResource
  get() = CommonMainString0.create

private fun init_create(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:create", "create",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1932,
    22),
    )
)

public val Res.string.date: StringResource
  get() = CommonMainString0.date

private fun init_date(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:date", "date",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2062,
    20),
    )
)

public val Res.string.date_details_dialog_title: StringResource
  get() = CommonMainString0.date_details_dialog_title

private fun init_date_details_dialog_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:date_details_dialog_title", "date_details_dialog_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 1955,
    49),
    )
)

public val Res.string.date_difference_result_title: StringResource
  get() = CommonMainString0.date_difference_result_title

private fun init_date_difference_result_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:date_difference_result_title", "date_difference_result_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2005,
    56),
    )
)

public val Res.string.delete: StringResource
  get() = CommonMainString0.delete

private fun init_delete(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:delete", "delete",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2252,
    22),
    )
)

public val Res.string.delete_event: StringResource
  get() = CommonMainString0.delete_event

private fun init_delete_event(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:delete_event", "delete_event",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2215,
    36),
    )
)

public val Res.string.delete_event_message: StringResource
  get() = CommonMainString0.delete_event_message

private fun init_delete_event_message(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:delete_event_message", "delete_event_message",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2083,
    84),
    )
)

public val Res.string.delete_event_title: StringResource
  get() = CommonMainString0.delete_event_title

private fun init_delete_event_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:delete_event_title", "delete_event_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2168,
    46),
    )
)

public val Res.string.description_optional: StringResource
  get() = CommonMainString0.description_optional

private fun init_description_optional(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:description_optional", "description_optional",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2275,
    60),
    )
)

public val Res.string.dialog_title_select_ethiopian_date: StringResource
  get() = CommonMainString0.dialog_title_select_ethiopian_date

private fun init_dialog_title_select_ethiopian_date(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:dialog_title_select_ethiopian_date", "dialog_title_select_ethiopian_date",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2336,
    70),
    )
)

public val Res.string.edit_event: StringResource
  get() = CommonMainString0.edit_event

private fun init_edit_event(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:edit_event", "edit_event",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2407,
    34),
    )
)

public val Res.string.empty_no_holidays_display: StringResource
  get() = CommonMainString0.empty_no_holidays_display

private fun init_empty_no_holidays_display(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:empty_no_holidays_display", "empty_no_holidays_display",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2442,
    65),
    )
)

public val Res.string.empty_no_holidays_month: StringResource
  get() = CommonMainString0.empty_no_holidays_month

private fun init_empty_no_holidays_month(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:empty_no_holidays_month", "empty_no_holidays_month",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2508,
    63),
    )
)

public val Res.string.end_time: StringResource
  get() = CommonMainString0.end_time

private fun init_end_time(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:end_time", "end_time",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2572,
    28),
    )
)

public val Res.string.ends: StringResource
  get() = CommonMainString0.ends

private fun init_ends(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:ends", "ends",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2601,
    20),
    )
)

public val Res.string.error_loading_calendar: StringResource
  get() = CommonMainString0.error_loading_calendar

private fun init_error_loading_calendar(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:error_loading_calendar", "error_loading_calendar",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2622,
    62),
    )
)

public val Res.string.error_loading_events: StringResource
  get() = CommonMainString0.error_loading_events

private fun init_error_loading_events(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:error_loading_events", "error_loading_events",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2685,
    56),
    )
)

public val Res.string.event_date_all_day: StringResource
  get() = CommonMainString0.event_date_all_day

private fun init_event_date_all_day(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:event_date_all_day", "event_date_all_day",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2742,
    58),
    )
)

public val Res.string.filter: StringResource
  get() = CommonMainString0.filter

private fun init_filter(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:filter", "filter",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2897,
    22),
    )
)

public val Res.string.filter_muslim: StringResource
  get() = CommonMainString0.filter_muslim

private fun init_filter_muslim(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:filter_muslim", "filter_muslim",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2801,
    29),
    )
)

public val Res.string.filter_orthodox: StringResource
  get() = CommonMainString0.filter_orthodox

private fun init_filter_orthodox(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:filter_orthodox", "filter_orthodox",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2831,
    35),
    )
)

public val Res.string.filter_public: StringResource
  get() = CommonMainString0.filter_public

private fun init_filter_public(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:filter_public", "filter_public",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2867,
    29),
    )
)

public val Res.string.holiday_derg: StringResource
  get() = CommonMainString0.holiday_derg

private fun init_holiday_derg(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:holiday_derg", "holiday_derg",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 3950,
    44),
    )
)

public val Res.string.holiday_derg_celebration: StringResource
  get() = CommonMainString0.holiday_derg_celebration

private fun init_holiday_derg_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_derg_celebration", "holiday_derg_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 2920,
    552),
    )
)

public val Res.string.holiday_derg_history: StringResource
  get() = CommonMainString0.holiday_derg_history

private fun init_holiday_derg_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_derg_history", "holiday_derg_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 3473,
    476),
    )
)

public val Res.string.holiday_info_dialog_checkbox: StringResource
  get() = CommonMainString0.holiday_info_dialog_checkbox

private fun init_holiday_info_dialog_checkbox(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_info_dialog_checkbox", "holiday_info_dialog_checkbox",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 3995,
    84),
    )
)

public val Res.string.holiday_info_dialog_message: StringResource
  get() = CommonMainString0.holiday_info_dialog_message

private fun init_holiday_info_dialog_message(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_info_dialog_message", "holiday_info_dialog_message",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 4080,
    111),
    )
)

public val Res.string.holiday_info_dialog_title: StringResource
  get() = CommonMainString0.holiday_info_dialog_title

private fun init_holiday_info_dialog_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_info_dialog_title", "holiday_info_dialog_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 4192,
    57),
    )
)

public val Res.string.holiday_muslim_arafat: StringResource
  get() = CommonMainString0.holiday_muslim_arafat

private fun init_holiday_muslim_arafat(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_arafat", "holiday_muslim_arafat",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 4834,
    49),
    )
)

public val Res.string.holiday_muslim_arafat_celebration: StringResource
  get() = CommonMainString0.holiday_muslim_arafat_celebration

private fun init_holiday_muslim_arafat_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_arafat_celebration", "holiday_muslim_arafat_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 4250,
    297),
    )
)

public val Res.string.holiday_muslim_arafat_history: StringResource
  get() = CommonMainString0.holiday_muslim_arafat_history

private fun init_holiday_muslim_arafat_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_arafat_history", "holiday_muslim_arafat_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 4548,
    285),
    )
)

public val Res.string.holiday_muslim_ashura: StringResource
  get() = CommonMainString0.holiday_muslim_ashura

private fun init_holiday_muslim_ashura(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_ashura", "holiday_muslim_ashura",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 5500,
    37),
    )
)

public val Res.string.holiday_muslim_ashura_celebration: StringResource
  get() = CommonMainString0.holiday_muslim_ashura_celebration

private fun init_holiday_muslim_ashura_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_ashura_celebration", "holiday_muslim_ashura_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 4884,
    309),
    )
)

public val Res.string.holiday_muslim_ashura_history: StringResource
  get() = CommonMainString0.holiday_muslim_ashura_history

private fun init_holiday_muslim_ashura_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_ashura_history", "holiday_muslim_ashura_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 5194,
    305),
    )
)

public val Res.string.holiday_muslim_eid_adha: StringResource
  get() = CommonMainString0.holiday_muslim_eid_adha

private fun init_holiday_muslim_eid_adha(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_eid_adha", "holiday_muslim_eid_adha",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 6198,
    47),
    )
)

public val Res.string.holiday_muslim_eid_adha_celebration: StringResource
  get() = CommonMainString0.holiday_muslim_eid_adha_celebration

private fun init_holiday_muslim_eid_adha_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_eid_adha_celebration", "holiday_muslim_eid_adha_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 5538,
    331),
    )
)

public val Res.string.holiday_muslim_eid_adha_history: StringResource
  get() = CommonMainString0.holiday_muslim_eid_adha_history

private fun init_holiday_muslim_eid_adha_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_eid_adha_history", "holiday_muslim_eid_adha_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 5870,
    327),
    )
)

public val Res.string.holiday_muslim_eid_fitr: StringResource
  get() = CommonMainString0.holiday_muslim_eid_fitr

private fun init_holiday_muslim_eid_fitr(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_eid_fitr", "holiday_muslim_eid_fitr",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 6978,
    47),
    )
)

public val Res.string.holiday_muslim_eid_fitr_celebration: StringResource
  get() = CommonMainString0.holiday_muslim_eid_fitr_celebration

private fun init_holiday_muslim_eid_fitr_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_eid_fitr_celebration", "holiday_muslim_eid_fitr_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 6246,
    335),
    )
)

public val Res.string.holiday_muslim_eid_fitr_history: StringResource
  get() = CommonMainString0.holiday_muslim_eid_fitr_history

private fun init_holiday_muslim_eid_fitr_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_eid_fitr_history", "holiday_muslim_eid_fitr_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 6582,
    395),
    )
)

public val Res.string.holiday_muslim_hijri_new_year: StringResource
  get() = CommonMainString0.holiday_muslim_hijri_new_year

private fun init_holiday_muslim_hijri_new_year(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_hijri_new_year", "holiday_muslim_hijri_new_year",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 7714,
    61),
    )
)

public val Res.string.holiday_muslim_hijri_new_year_celebration: StringResource
  get() = CommonMainString0.holiday_muslim_hijri_new_year_celebration

private fun init_holiday_muslim_hijri_new_year_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_hijri_new_year_celebration", "holiday_muslim_hijri_new_year_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 7026,
    341),
    )
)

public val Res.string.holiday_muslim_hijri_new_year_history: StringResource
  get() = CommonMainString0.holiday_muslim_hijri_new_year_history

private fun init_holiday_muslim_hijri_new_year_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_hijri_new_year_history", "holiday_muslim_hijri_new_year_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 7368,
    345),
    )
)

public val Res.string.holiday_muslim_isra_miraj: StringResource
  get() = CommonMainString0.holiday_muslim_isra_miraj

private fun init_holiday_muslim_isra_miraj(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_isra_miraj", "holiday_muslim_isra_miraj",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 8452,
    57),
    )
)

public val Res.string.holiday_muslim_isra_miraj_celebration: StringResource
  get() = CommonMainString0.holiday_muslim_isra_miraj_celebration

private fun init_holiday_muslim_isra_miraj_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_isra_miraj_celebration", "holiday_muslim_isra_miraj_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 7776,
    337),
    )
)

public val Res.string.holiday_muslim_isra_miraj_history: StringResource
  get() = CommonMainString0.holiday_muslim_isra_miraj_history

private fun init_holiday_muslim_isra_miraj_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_isra_miraj_history", "holiday_muslim_isra_miraj_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 8114,
    337),
    )
)

public val Res.string.holiday_muslim_mawlid: StringResource
  get() = CommonMainString0.holiday_muslim_mawlid

private fun init_holiday_muslim_mawlid(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_mawlid", "holiday_muslim_mawlid",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 9166,
    49),
    )
)

public val Res.string.holiday_muslim_mawlid_celebration: StringResource
  get() = CommonMainString0.holiday_muslim_mawlid_celebration

private fun init_holiday_muslim_mawlid_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_mawlid_celebration", "holiday_muslim_mawlid_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 8510,
    389),
    )
)

public val Res.string.holiday_muslim_mawlid_history: StringResource
  get() = CommonMainString0.holiday_muslim_mawlid_history

private fun init_holiday_muslim_mawlid_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_mawlid_history", "holiday_muslim_mawlid_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 8900,
    265),
    )
)

public val Res.string.holiday_muslim_mid_shaban: StringResource
  get() = CommonMainString0.holiday_muslim_mid_shaban

private fun init_holiday_muslim_mid_shaban(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_mid_shaban", "holiday_muslim_mid_shaban",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 9884,
    49),
    )
)

public val Res.string.holiday_muslim_mid_shaban_celebration: StringResource
  get() = CommonMainString0.holiday_muslim_mid_shaban_celebration

private fun init_holiday_muslim_mid_shaban_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_mid_shaban_celebration", "holiday_muslim_mid_shaban_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 9216,
    321),
    )
)

public val Res.string.holiday_muslim_mid_shaban_history: StringResource
  get() = CommonMainString0.holiday_muslim_mid_shaban_history

private fun init_holiday_muslim_mid_shaban_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_mid_shaban_history", "holiday_muslim_mid_shaban_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 9538,
    345),
    )
)

public val Res.string.holiday_muslim_ramadan_start: StringResource
  get() = CommonMainString0.holiday_muslim_ramadan_start

private fun init_holiday_muslim_ramadan_start(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_ramadan_start", "holiday_muslim_ramadan_start",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 10636,
    60),
    )
)

public val Res.string.holiday_muslim_ramadan_start_celebration: StringResource
  get() = CommonMainString0.holiday_muslim_ramadan_start_celebration

private fun init_holiday_muslim_ramadan_start_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_ramadan_start_celebration", "holiday_muslim_ramadan_start_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 9934,
    348),
    )
)

public val Res.string.holiday_muslim_ramadan_start_history: StringResource
  get() = CommonMainString0.holiday_muslim_ramadan_start_history

private fun init_holiday_muslim_ramadan_start_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_muslim_ramadan_start_history", "holiday_muslim_ramadan_start_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 10283,
    352),
    )
)

public val Res.string.holiday_orthodox_abiy_tsom: StringResource
  get() = CommonMainString0.holiday_orthodox_abiy_tsom

private fun init_holiday_orthodox_abiy_tsom(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_abiy_tsom", "holiday_orthodox_abiy_tsom",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 11743,
    66),
    )
)

public val Res.string.holiday_orthodox_abiy_tsom_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_abiy_tsom_celebration

private fun init_holiday_orthodox_abiy_tsom_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_abiy_tsom_celebration", "holiday_orthodox_abiy_tsom_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 10697,
    534),
    )
)

public val Res.string.holiday_orthodox_abiy_tsom_history: StringResource
  get() = CommonMainString0.holiday_orthodox_abiy_tsom_history

private fun init_holiday_orthodox_abiy_tsom_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_abiy_tsom_history", "holiday_orthodox_abiy_tsom_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 11232,
    510),
    )
)

public val Res.string.holiday_orthodox_debre_tabor: StringResource
  get() = CommonMainString0.holiday_orthodox_debre_tabor

private fun init_holiday_orthodox_debre_tabor(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_debre_tabor", "holiday_orthodox_debre_tabor",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 12740,
    76),
    )
)

public val Res.string.holiday_orthodox_debre_tabor_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_debre_tabor_celebration

private fun init_holiday_orthodox_debre_tabor_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_debre_tabor_celebration", "holiday_orthodox_debre_tabor_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 11810,
    432),
    )
)

public val Res.string.holiday_orthodox_debre_tabor_history: StringResource
  get() = CommonMainString0.holiday_orthodox_debre_tabor_history

private fun init_holiday_orthodox_debre_tabor_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_debre_tabor_history", "holiday_orthodox_debre_tabor_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 12243,
    496),
    )
)

public val Res.string.holiday_orthodox_debre_zeit: StringResource
  get() = CommonMainString0.holiday_orthodox_debre_zeit

private fun init_holiday_orthodox_debre_zeit(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_debre_zeit", "holiday_orthodox_debre_zeit",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 13761,
    75),
    )
)

public val Res.string.holiday_orthodox_debre_zeit_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_debre_zeit_celebration

private fun init_holiday_orthodox_debre_zeit_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_debre_zeit_celebration", "holiday_orthodox_debre_zeit_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 12817,
    439),
    )
)

public val Res.string.holiday_orthodox_debre_zeit_history: StringResource
  get() = CommonMainString0.holiday_orthodox_debre_zeit_history

private fun init_holiday_orthodox_debre_zeit_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_debre_zeit_history", "holiday_orthodox_debre_zeit_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 13257,
    503),
    )
)

public val Res.string.holiday_orthodox_erget: StringResource
  get() = CommonMainString0.holiday_orthodox_erget

private fun init_holiday_orthodox_erget(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_erget", "holiday_orthodox_erget",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 14795,
    54),
    )
)

public val Res.string.holiday_orthodox_erget_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_erget_celebration

private fun init_holiday_orthodox_erget_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_erget_celebration", "holiday_orthodox_erget_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 13837,
    458),
    )
)

public val Res.string.holiday_orthodox_erget_history: StringResource
  get() = CommonMainString0.holiday_orthodox_erget_history

private fun init_holiday_orthodox_erget_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_erget_history", "holiday_orthodox_erget_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 14296,
    498),
    )
)

public val Res.string.holiday_orthodox_fasika: StringResource
  get() = CommonMainString0.holiday_orthodox_fasika

private fun init_holiday_orthodox_fasika(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_fasika", "holiday_orthodox_fasika",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 15962,
    51),
    )
)

public val Res.string.holiday_orthodox_fasika_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_fasika_celebration

private fun init_holiday_orthodox_fasika_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_fasika_celebration", "holiday_orthodox_fasika_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 14850,
    599),
    )
)

public val Res.string.holiday_orthodox_fasika_history: StringResource
  get() = CommonMainString0.holiday_orthodox_fasika_history

private fun init_holiday_orthodox_fasika_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_fasika_history", "holiday_orthodox_fasika_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 15450,
    511),
    )
)

public val Res.string.holiday_orthodox_filseta: StringResource
  get() = CommonMainString0.holiday_orthodox_filseta

private fun init_holiday_orthodox_filseta(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_filseta", "holiday_orthodox_filseta",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 16984,
    72),
    )
)

public val Res.string.holiday_orthodox_filseta_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_filseta_celebration

private fun init_holiday_orthodox_filseta_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_filseta_celebration", "holiday_orthodox_filseta_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 16014,
    504),
    )
)

public val Res.string.holiday_orthodox_filseta_history: StringResource
  get() = CommonMainString0.holiday_orthodox_filseta_history

private fun init_holiday_orthodox_filseta_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_filseta_history", "holiday_orthodox_filseta_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 16519,
    464),
    )
)

public val Res.string.holiday_orthodox_ghad: StringResource
  get() = CommonMainString0.holiday_orthodox_ghad

private fun init_holiday_orthodox_ghad(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_ghad", "holiday_orthodox_ghad",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 17885,
    61),
    )
)

public val Res.string.holiday_orthodox_ghad_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_ghad_celebration

private fun init_holiday_orthodox_ghad_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_ghad_celebration", "holiday_orthodox_ghad_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 17057,
    377),
    )
)

public val Res.string.holiday_orthodox_ghad_history: StringResource
  get() = CommonMainString0.holiday_orthodox_ghad_history

private fun init_holiday_orthodox_ghad_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_ghad_history", "holiday_orthodox_ghad_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 17435,
    449),
    )
)

public val Res.string.holiday_orthodox_hosanna: StringResource
  get() = CommonMainString0.holiday_orthodox_hosanna

private fun init_holiday_orthodox_hosanna(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_hosanna", "holiday_orthodox_hosanna",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 18833,
    60),
    )
)

public val Res.string.holiday_orthodox_hosanna_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_hosanna_celebration

private fun init_holiday_orthodox_hosanna_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_hosanna_celebration", "holiday_orthodox_hosanna_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 17947,
    444),
    )
)

public val Res.string.holiday_orthodox_hosanna_history: StringResource
  get() = CommonMainString0.holiday_orthodox_hosanna_history

private fun init_holiday_orthodox_hosanna_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_hosanna_history", "holiday_orthodox_hosanna_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 18392,
    440),
    )
)

public val Res.string.holiday_orthodox_kana_zegelila: StringResource
  get() = CommonMainString0.holiday_orthodox_kana_zegelila

private fun init_holiday_orthodox_kana_zegelila(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_kana_zegelila", "holiday_orthodox_kana_zegelila",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 19800,
    82),
    )
)

public val Res.string.holiday_orthodox_kana_zegelila_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_kana_zegelila_celebration

private fun init_holiday_orthodox_kana_zegelila_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_kana_zegelila_celebration", "holiday_orthodox_kana_zegelila_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 18894,
    402),
    )
)

public val Res.string.holiday_orthodox_kana_zegelila_history: StringResource
  get() = CommonMainString0.holiday_orthodox_kana_zegelila_history

private fun init_holiday_orthodox_kana_zegelila_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_kana_zegelila_history", "holiday_orthodox_kana_zegelila_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 19297,
    502),
    )
)

public val Res.string.holiday_orthodox_lideta_mariam: StringResource
  get() = CommonMainString0.holiday_orthodox_lideta_mariam

private fun init_holiday_orthodox_lideta_mariam(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_lideta_mariam", "holiday_orthodox_lideta_mariam",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 20793,
    78),
    )
)

public val Res.string.holiday_orthodox_lideta_mariam_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_lideta_mariam_celebration

private fun init_holiday_orthodox_lideta_mariam_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_lideta_mariam_celebration", "holiday_orthodox_lideta_mariam_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 19883,
    390),
    )
)

public val Res.string.holiday_orthodox_lideta_mariam_history: StringResource
  get() = CommonMainString0.holiday_orthodox_lideta_mariam_history

private fun init_holiday_orthodox_lideta_mariam_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_lideta_mariam_history", "holiday_orthodox_lideta_mariam_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 20274,
    518),
    )
)

public val Res.string.holiday_orthodox_nineveh: StringResource
  get() = CommonMainString0.holiday_orthodox_nineveh

private fun init_holiday_orthodox_nineveh(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_nineveh", "holiday_orthodox_nineveh",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 21878,
    44),
    )
)

public val Res.string.holiday_orthodox_nineveh_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_nineveh_celebration

private fun init_holiday_orthodox_nineveh_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_nineveh_celebration", "holiday_orthodox_nineveh_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 20872,
    464),
    )
)

public val Res.string.holiday_orthodox_nineveh_history: StringResource
  get() = CommonMainString0.holiday_orthodox_nineveh_history

private fun init_holiday_orthodox_nineveh_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_nineveh_history", "holiday_orthodox_nineveh_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 21337,
    540),
    )
)

public val Res.string.holiday_orthodox_peraklitos: StringResource
  get() = CommonMainString0.holiday_orthodox_peraklitos

private fun init_holiday_orthodox_peraklitos(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_peraklitos", "holiday_orthodox_peraklitos",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 22867,
    67),
    )
)

public val Res.string.holiday_orthodox_peraklitos_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_peraklitos_celebration

private fun init_holiday_orthodox_peraklitos_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_peraklitos_celebration", "holiday_orthodox_peraklitos_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 21923,
    451),
    )
)

public val Res.string.holiday_orthodox_peraklitos_history: StringResource
  get() = CommonMainString0.holiday_orthodox_peraklitos_history

private fun init_holiday_orthodox_peraklitos_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_peraklitos_history", "holiday_orthodox_peraklitos_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 22375,
    491),
    )
)

public val Res.string.holiday_orthodox_rikbe_kahinat: StringResource
  get() = CommonMainString0.holiday_orthodox_rikbe_kahinat

private fun init_holiday_orthodox_rikbe_kahinat(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_rikbe_kahinat", "holiday_orthodox_rikbe_kahinat",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 23857,
    86),
    )
)

public val Res.string.holiday_orthodox_rikbe_kahinat_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_rikbe_kahinat_celebration

private fun init_holiday_orthodox_rikbe_kahinat_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_rikbe_kahinat_celebration", "holiday_orthodox_rikbe_kahinat_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 22935,
    462),
    )
)

public val Res.string.holiday_orthodox_rikbe_kahinat_history: StringResource
  get() = CommonMainString0.holiday_orthodox_rikbe_kahinat_history

private fun init_holiday_orthodox_rikbe_kahinat_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_rikbe_kahinat_history", "holiday_orthodox_rikbe_kahinat_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 23398,
    458),
    )
)

public val Res.string.holiday_orthodox_siklet: StringResource
  get() = CommonMainString0.holiday_orthodox_siklet

private fun init_holiday_orthodox_siklet(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_siklet", "holiday_orthodox_siklet",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 24924,
    59),
    )
)

public val Res.string.holiday_orthodox_siklet_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_siklet_celebration

private fun init_holiday_orthodox_siklet_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_siklet_celebration", "holiday_orthodox_siklet_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 23944,
    519),
    )
)

public val Res.string.holiday_orthodox_siklet_history: StringResource
  get() = CommonMainString0.holiday_orthodox_siklet_history

private fun init_holiday_orthodox_siklet_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_siklet_history", "holiday_orthodox_siklet_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 24464,
    459),
    )
)

public val Res.string.holiday_orthodox_tensae: StringResource
  get() = CommonMainString0.holiday_orthodox_tensae

private fun init_holiday_orthodox_tensae(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_tensae", "holiday_orthodox_tensae",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 26020,
    59),
    )
)

public val Res.string.holiday_orthodox_tensae_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_tensae_celebration

private fun init_holiday_orthodox_tensae_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_tensae_celebration", "holiday_orthodox_tensae_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 24984,
    507),
    )
)

public val Res.string.holiday_orthodox_tensae_history: StringResource
  get() = CommonMainString0.holiday_orthodox_tensae_history

private fun init_holiday_orthodox_tensae_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_tensae_history", "holiday_orthodox_tensae_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 25492,
    527),
    )
)

public val Res.string.holiday_orthodox_tsome_dihnet: StringResource
  get() = CommonMainString0.holiday_orthodox_tsome_dihnet

private fun init_holiday_orthodox_tsome_dihnet(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_tsome_dihnet", "holiday_orthodox_tsome_dihnet",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 26952,
    77),
    )
)

public val Res.string.holiday_orthodox_tsome_dihnet_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_tsome_dihnet_celebration

private fun init_holiday_orthodox_tsome_dihnet_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_tsome_dihnet_celebration", "holiday_orthodox_tsome_dihnet_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 26080,
    417),
    )
)

public val Res.string.holiday_orthodox_tsome_dihnet_history: StringResource
  get() = CommonMainString0.holiday_orthodox_tsome_dihnet_history

private fun init_holiday_orthodox_tsome_dihnet_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_tsome_dihnet_history", "holiday_orthodox_tsome_dihnet_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 26498,
    453),
    )
)

public val Res.string.holiday_orthodox_tsome_hawariat: StringResource
  get() = CommonMainString0.holiday_orthodox_tsome_hawariat

private fun init_holiday_orthodox_tsome_hawariat(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_tsome_hawariat", "holiday_orthodox_tsome_hawariat",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 28002,
    83),
    )
)

public val Res.string.holiday_orthodox_tsome_hawariat_celebration: StringResource
  get() = CommonMainString0.holiday_orthodox_tsome_hawariat_celebration

private fun init_holiday_orthodox_tsome_hawariat_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_tsome_hawariat_celebration",
    "holiday_orthodox_tsome_hawariat_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 27030,
    507),
    )
)

public val Res.string.holiday_orthodox_tsome_hawariat_history: StringResource
  get() = CommonMainString0.holiday_orthodox_tsome_hawariat_history

private fun init_holiday_orthodox_tsome_hawariat_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_orthodox_tsome_hawariat_history", "holiday_orthodox_tsome_hawariat_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 27538,
    463),
    )
)

public val Res.string.holiday_public_adwa: StringResource
  get() = CommonMainString0.holiday_public_adwa

private fun init_holiday_public_adwa(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_adwa", "holiday_public_adwa",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 30254,
    51),
    )
)

public val Res.string.holiday_public_adwa_celebration: StringResource
  get() = CommonMainString0.holiday_public_adwa_celebration

private fun init_holiday_public_adwa_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_adwa_celebration", "holiday_public_adwa_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 28086,
    591),
    )
)

public val Res.string.holiday_public_adwa_history: StringResource
  get() = CommonMainString0.holiday_public_adwa_history

private fun init_holiday_public_adwa_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_adwa_history", "holiday_public_adwa_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 28678,
    1575),
    )
)

public val Res.string.holiday_public_christian_genna: StringResource
  get() = CommonMainString0.holiday_public_christian_genna

private fun init_holiday_public_christian_genna(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_christian_genna", "holiday_public_christian_genna",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 31260,
    46),
    )
)

public val Res.string.holiday_public_christian_genna_celebration: StringResource
  get() = CommonMainString0.holiday_public_christian_genna_celebration

private fun init_holiday_public_christian_genna_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_christian_genna_celebration", "holiday_public_christian_genna_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 30306,
    366),
    )
)

public val Res.string.holiday_public_christian_genna_history: StringResource
  get() = CommonMainString0.holiday_public_christian_genna_history

private fun init_holiday_public_christian_genna_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_christian_genna_history", "holiday_public_christian_genna_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 30673,
    586),
    )
)

public val Res.string.holiday_public_christian_meskel: StringResource
  get() = CommonMainString0.holiday_public_christian_meskel

private fun init_holiday_public_christian_meskel(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_christian_meskel", "holiday_public_christian_meskel",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 32391,
    47),
    )
)

public val Res.string.holiday_public_christian_meskel_celebration: StringResource
  get() = CommonMainString0.holiday_public_christian_meskel_celebration

private fun init_holiday_public_christian_meskel_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_christian_meskel_celebration",
    "holiday_public_christian_meskel_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 31307,
    655),
    )
)

public val Res.string.holiday_public_christian_meskel_history: StringResource
  get() = CommonMainString0.holiday_public_christian_meskel_history

private fun init_holiday_public_christian_meskel_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_christian_meskel_history", "holiday_public_christian_meskel_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 31963,
    427),
    )
)

public val Res.string.holiday_public_christian_timket: StringResource
  get() = CommonMainString0.holiday_public_christian_timket

private fun init_holiday_public_christian_timket(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_christian_timket", "holiday_public_christian_timket",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 34095,
    47),
    )
)

public val Res.string.holiday_public_christian_timket_celebration: StringResource
  get() = CommonMainString0.holiday_public_christian_timket_celebration

private fun init_holiday_public_christian_timket_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_christian_timket_celebration",
    "holiday_public_christian_timket_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 32439,
    1207),
    )
)

public val Res.string.holiday_public_christian_timket_history: StringResource
  get() = CommonMainString0.holiday_public_christian_timket_history

private fun init_holiday_public_christian_timket_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_christian_timket_history", "holiday_public_christian_timket_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 33647,
    447),
    )
)

public val Res.string.holiday_public_enkutatash: StringResource
  get() = CommonMainString0.holiday_public_enkutatash

private fun init_holiday_public_enkutatash(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_enkutatash", "holiday_public_enkutatash",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 35499,
    49),
    )
)

public val Res.string.holiday_public_enkutatash_celebration: StringResource
  get() = CommonMainString0.holiday_public_enkutatash_celebration

private fun init_holiday_public_enkutatash_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_enkutatash_celebration", "holiday_public_enkutatash_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 34143,
    593),
    )
)

public val Res.string.holiday_public_enkutatash_history: StringResource
  get() = CommonMainString0.holiday_public_enkutatash_history

private fun init_holiday_public_enkutatash_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_enkutatash_history", "holiday_public_enkutatash_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 34737,
    761),
    )
)

public val Res.string.holiday_public_labour_day: StringResource
  get() = CommonMainString0.holiday_public_labour_day

private fun init_holiday_public_labour_day(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_labour_day", "holiday_public_labour_day",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 36633,
    49),
    )
)

public val Res.string.holiday_public_labour_day_celebration: StringResource
  get() = CommonMainString0.holiday_public_labour_day_celebration

private fun init_holiday_public_labour_day_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_labour_day_celebration", "holiday_public_labour_day_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 35549,
    557),
    )
)

public val Res.string.holiday_public_labour_day_history: StringResource
  get() = CommonMainString0.holiday_public_labour_day_history

private fun init_holiday_public_labour_day_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_labour_day_history", "holiday_public_labour_day_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 36107,
    525),
    )
)

public val Res.string.holiday_public_patriots_day: StringResource
  get() = CommonMainString0.holiday_public_patriots_day

private fun init_holiday_public_patriots_day(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_patriots_day", "holiday_public_patriots_day",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 37767,
    55),
    )
)

public val Res.string.holiday_public_patriots_day_celebration: StringResource
  get() = CommonMainString0.holiday_public_patriots_day_celebration

private fun init_holiday_public_patriots_day_celebration(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_patriots_day_celebration", "holiday_public_patriots_day_celebration",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 36683,
    535),
    )
)

public val Res.string.holiday_public_patriots_day_history: StringResource
  get() = CommonMainString0.holiday_public_patriots_day_history

private fun init_holiday_public_patriots_day_history(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:holiday_public_patriots_day_history", "holiday_public_patriots_day_history",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 37219,
    547),
    )
)

public val Res.string.hour_before_1: StringResource
  get() = CommonMainString0.hour_before_1

private fun init_hour_before_1(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:hour_before_1", "hour_before_1",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 37823,
    41),
    )
)

public val Res.string.label_date_difference: StringResource
  get() = CommonMainString0.label_date_difference

private fun init_label_date_difference(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_date_difference", "label_date_difference",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 37865,
    49),
    )
)

public val Res.string.label_day_off: StringResource
  get() = CommonMainString0.label_day_off

private fun init_label_day_off(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:label_day_off", "label_day_off",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 37915,
    33),
    )
)

public val Res.string.label_difference_result: StringResource
  get() = CommonMainString0.label_difference_result

private fun init_label_difference_result(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_difference_result", "label_difference_result",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 37949,
    47),
    )
)

public val Res.string.label_ec_suffix: StringResource
  get() = CommonMainString0.label_ec_suffix

private fun init_label_ec_suffix(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:label_ec_suffix", "label_ec_suffix",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 37997,
    31),
    )
)

public val Res.string.label_end_date: StringResource
  get() = CommonMainString0.label_end_date

private fun init_label_end_date(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:label_end_date", "label_end_date",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38029,
    34),
    )
)

public val Res.string.label_ethiopian_date: StringResource
  get() = CommonMainString0.label_ethiopian_date

private fun init_label_ethiopian_date(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_ethiopian_date", "label_ethiopian_date",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38064,
    48),
    )
)

public val Res.string.label_filter_by_type: StringResource
  get() = CommonMainString0.label_filter_by_type

private fun init_label_filter_by_type(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_filter_by_type", "label_filter_by_type",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38113,
    48),
    )
)

public val Res.string.label_from_ethiopian: StringResource
  get() = CommonMainString0.label_from_ethiopian

private fun init_label_from_ethiopian(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_from_ethiopian", "label_from_ethiopian",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38162,
    48),
    )
)

public val Res.string.label_from_gregorian: StringResource
  get() = CommonMainString0.label_from_gregorian

private fun init_label_from_gregorian(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_from_gregorian", "label_from_gregorian",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38211,
    48),
    )
)

public val Res.string.label_gregorian_date: StringResource
  get() = CommonMainString0.label_gregorian_date

private fun init_label_gregorian_date(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_gregorian_date", "label_gregorian_date",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38260,
    48),
    )
)

public val Res.string.label_holidays: StringResource
  get() = CommonMainString0.label_holidays

private fun init_label_holidays(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:label_holidays", "label_holidays",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38309,
    34),
    )
)

public val Res.string.label_start_date: StringResource
  get() = CommonMainString0.label_start_date

private fun init_label_start_date(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_start_date", "label_start_date",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38344,
    40),
    )
)

public val Res.string.label_theme_color: StringResource
  get() = CommonMainString0.label_theme_color

private fun init_label_theme_color(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_theme_color", "label_theme_color",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38385,
    41),
    )
)

public val Res.string.label_theme_mode: StringResource
  get() = CommonMainString0.label_theme_mode

private fun init_label_theme_mode(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:label_theme_mode", "label_theme_mode",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38427,
    40),
    )
)

public val Res.string.menu_about_us: StringResource
  get() = CommonMainString0.menu_about_us

private fun init_menu_about_us(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:menu_about_us", "menu_about_us",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38468,
    33),
    )
)

public val Res.string.menu_additional_settings: StringResource
  get() = CommonMainString0.menu_additional_settings

private fun init_menu_additional_settings(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:menu_additional_settings", "menu_additional_settings",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38502,
    60),
    )
)

public val Res.string.menu_color_theme: StringResource
  get() = CommonMainString0.menu_color_theme

private fun init_menu_color_theme(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:menu_color_theme", "menu_color_theme",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38563,
    44),
    )
)

public val Res.string.menu_language: StringResource
  get() = CommonMainString0.menu_language

private fun init_menu_language(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:menu_language", "menu_language",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38608,
    69),
    )
)

public val Res.string.menu_notifications: StringResource
  get() = CommonMainString0.menu_notifications

private fun init_menu_notifications(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:menu_notifications", "menu_notifications",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38678,
    46),
    )
)

public val Res.string.menu_privacy_policy: StringResource
  get() = CommonMainString0.menu_privacy_policy

private fun init_menu_privacy_policy(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:menu_privacy_policy", "menu_privacy_policy",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38725,
    47),
    )
)

public val Res.string.menu_share_app: StringResource
  get() = CommonMainString0.menu_share_app

private fun init_menu_share_app(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:menu_share_app", "menu_share_app",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38773,
    34),
    )
)

public val Res.string.minutes_before_15: StringResource
  get() = CommonMainString0.minutes_before_15

private fun init_minutes_before_15(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:minutes_before_15", "minutes_before_15",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38808,
    49),
    )
)

public val Res.string.minutes_before_30: StringResource
  get() = CommonMainString0.minutes_before_30

private fun init_minutes_before_30(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:minutes_before_30", "minutes_before_30",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38858,
    49),
    )
)

public val Res.string.minutes_before_5: StringResource
  get() = CommonMainString0.minutes_before_5

private fun init_minutes_before_5(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:minutes_before_5", "minutes_before_5",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38908,
    48),
    )
)

public val Res.string.nav_convert: StringResource
  get() = CommonMainString0.nav_convert

private fun init_nav_convert(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:nav_convert", "nav_convert",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38957,
    31),
    )
)

public val Res.string.nav_event: StringResource
  get() = CommonMainString0.nav_event

private fun init_nav_event(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:nav_event", "nav_event",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 38989,
    25),
    )
)

public val Res.string.nav_holiday: StringResource
  get() = CommonMainString0.nav_holiday

private fun init_nav_holiday(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:nav_holiday", "nav_holiday",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39015,
    31),
    )
)

public val Res.string.nav_month: StringResource
  get() = CommonMainString0.nav_month

private fun init_nav_month(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:nav_month", "nav_month",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39047,
    25),
    )
)

public val Res.string.nav_more: StringResource
  get() = CommonMainString0.nav_more

private fun init_nav_more(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:nav_more", "nav_more",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39073,
    24),
    )
)

public val Res.string.never: StringResource
  get() = CommonMainString0.never

private fun init_never(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:never", "never",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39098,
    21),
    )
)

public val Res.string.no_events: StringResource
  get() = CommonMainString0.no_events

private fun init_no_events(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:no_events", "no_events",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39120,
    37),
    )
)

public val Res.string.no_repeat: StringResource
  get() = CommonMainString0.no_repeat

private fun init_no_repeat(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:no_repeat", "no_repeat",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39158,
    29),
    )
)

public val Res.string.notification_banner_dismiss: StringResource
  get() = CommonMainString0.notification_banner_dismiss

private fun init_notification_banner_dismiss(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:notification_banner_dismiss", "notification_banner_dismiss",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39188,
    47),
    )
)

public val Res.string.notification_banner_enable: StringResource
  get() = CommonMainString0.notification_banner_enable

private fun init_notification_banner_enable(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:notification_banner_enable", "notification_banner_enable",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39236,
    42),
    )
)

public val Res.string.notification_banner_message: StringResource
  get() = CommonMainString0.notification_banner_message

private fun init_notification_banner_message(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:notification_banner_message", "notification_banner_message",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39279,
    111),
    )
)

public val Res.string.notification_banner_title: StringResource
  get() = CommonMainString0.notification_banner_title

private fun init_notification_banner_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:notification_banner_title", "notification_banner_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39391,
    61),
    )
)

public val Res.string.ok: StringResource
  get() = CommonMainString0.ok

private fun init_ok(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:ok", "ok",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39453,
    14),
    )
)

public val Res.string.onboarding_calendar_description: StringResource
  get() = CommonMainString0.onboarding_calendar_description

private fun init_onboarding_calendar_description(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_calendar_description", "onboarding_calendar_description",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39468,
    95),
    )
)

public val Res.string.onboarding_calendar_dual_display: StringResource
  get() = CommonMainString0.onboarding_calendar_dual_display

private fun init_onboarding_calendar_dual_display(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_calendar_dual_display", "onboarding_calendar_dual_display",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39658,
    68),
    )
)

public val Res.string.onboarding_calendar_dual_display_desc: StringResource
  get() = CommonMainString0.onboarding_calendar_dual_display_desc

private fun init_onboarding_calendar_dual_display_desc(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_calendar_dual_display_desc", "onboarding_calendar_dual_display_desc",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39564,
    93),
    )
)

public val Res.string.onboarding_calendar_tip: StringResource
  get() = CommonMainString0.onboarding_calendar_tip

private fun init_onboarding_calendar_tip(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_calendar_tip", "onboarding_calendar_tip",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39727,
    115),
    )
)

public val Res.string.onboarding_calendar_title: StringResource
  get() = CommonMainString0.onboarding_calendar_title

private fun init_onboarding_calendar_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_calendar_title", "onboarding_calendar_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39843,
    57),
    )
)

public val Res.string.onboarding_done: StringResource
  get() = CommonMainString0.onboarding_done

private fun init_onboarding_done(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:onboarding_done", "onboarding_done",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39901,
    31),
    )
)

public val Res.string.onboarding_feature_date_converter: StringResource
  get() = CommonMainString0.onboarding_feature_date_converter

private fun init_onboarding_feature_date_converter(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_feature_date_converter", "onboarding_feature_date_converter",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 39933,
    69),
    )
)

public val Res.string.onboarding_feature_dual_calendar: StringResource
  get() = CommonMainString0.onboarding_feature_dual_calendar

private fun init_onboarding_feature_dual_calendar(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_feature_dual_calendar", "onboarding_feature_dual_calendar",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40003,
    68),
    )
)

public val Res.string.onboarding_feature_holidays: StringResource
  get() = CommonMainString0.onboarding_feature_holidays

private fun init_onboarding_feature_holidays(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_feature_holidays", "onboarding_feature_holidays",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40072,
    75),
    )
)

public val Res.string.onboarding_feature_multilingual: StringResource
  get() = CommonMainString0.onboarding_feature_multilingual

private fun init_onboarding_feature_multilingual(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_feature_multilingual", "onboarding_feature_multilingual",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40148,
    67),
    )
)

public val Res.string.onboarding_feature_reminders: StringResource
  get() = CommonMainString0.onboarding_feature_reminders

private fun init_onboarding_feature_reminders(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_feature_reminders", "onboarding_feature_reminders",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40216,
    68),
    )
)

public val Res.string.onboarding_get_started: StringResource
  get() = CommonMainString0.onboarding_get_started

private fun init_onboarding_get_started(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_get_started", "onboarding_get_started",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40285,
    46),
    )
)

public val Res.string.onboarding_holidays_description: StringResource
  get() = CommonMainString0.onboarding_holidays_description

private fun init_onboarding_holidays_description(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_description", "onboarding_holidays_description",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40332,
    79),
    )
)

public val Res.string.onboarding_holidays_muslim: StringResource
  get() = CommonMainString0.onboarding_holidays_muslim

private fun init_onboarding_holidays_muslim(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_muslim", "onboarding_holidays_muslim",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40496,
    70),
    )
)

public val Res.string.onboarding_holidays_muslim_desc: StringResource
  get() = CommonMainString0.onboarding_holidays_muslim_desc

private fun init_onboarding_holidays_muslim_desc(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_muslim_desc", "onboarding_holidays_muslim_desc",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40412,
    83),
    )
)

public val Res.string.onboarding_holidays_orthodox: StringResource
  get() = CommonMainString0.onboarding_holidays_orthodox

private fun init_onboarding_holidays_orthodox(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_orthodox", "onboarding_holidays_orthodox",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40842,
    60),
    )
)

public val Res.string.onboarding_holidays_orthodox_days: StringResource
  get() = CommonMainString0.onboarding_holidays_orthodox_days

private fun init_onboarding_holidays_orthodox_days(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_orthodox_days", "onboarding_holidays_orthodox_days",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40670,
    65),
    )
)

public val Res.string.onboarding_holidays_orthodox_days_desc: StringResource
  get() = CommonMainString0.onboarding_holidays_orthodox_days_desc

private fun init_onboarding_holidays_orthodox_days_desc(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_orthodox_days_desc", "onboarding_holidays_orthodox_days_desc",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40567,
    102),
    )
)

public val Res.string.onboarding_holidays_orthodox_desc: StringResource
  get() = CommonMainString0.onboarding_holidays_orthodox_desc

private fun init_onboarding_holidays_orthodox_desc(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_orthodox_desc", "onboarding_holidays_orthodox_desc",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40736,
    105),
    )
)

public val Res.string.onboarding_holidays_public: StringResource
  get() = CommonMainString0.onboarding_holidays_public

private fun init_onboarding_holidays_public(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_public", "onboarding_holidays_public",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40983,
    54),
    )
)

public val Res.string.onboarding_holidays_public_desc: StringResource
  get() = CommonMainString0.onboarding_holidays_public_desc

private fun init_onboarding_holidays_public_desc(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_public_desc", "onboarding_holidays_public_desc",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 40903,
    79),
    )
)

public val Res.string.onboarding_holidays_title: StringResource
  get() = CommonMainString0.onboarding_holidays_title

private fun init_onboarding_holidays_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_holidays_title", "onboarding_holidays_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41038,
    57),
    )
)

public val Res.string.onboarding_language_description: StringResource
  get() = CommonMainString0.onboarding_language_description

private fun init_onboarding_language_description(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_language_description", "onboarding_language_description",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41096,
    75),
    )
)

public val Res.string.onboarding_language_title: StringResource
  get() = CommonMainString0.onboarding_language_title

private fun init_onboarding_language_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_language_title", "onboarding_language_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41172,
    53),
    )
)

public val Res.string.onboarding_next: StringResource
  get() = CommonMainString0.onboarding_next

private fun init_onboarding_next(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:onboarding_next", "onboarding_next",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41226,
    31),
    )
)

public val Res.string.onboarding_skip: StringResource
  get() = CommonMainString0.onboarding_skip

private fun init_onboarding_skip(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:onboarding_skip", "onboarding_skip",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41258,
    31),
    )
)

public val Res.string.onboarding_theme_dark: StringResource
  get() = CommonMainString0.onboarding_theme_dark

private fun init_onboarding_theme_dark(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_theme_dark", "onboarding_theme_dark",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41361,
    41),
    )
)

public val Res.string.onboarding_theme_dark_desc: StringResource
  get() = CommonMainString0.onboarding_theme_dark_desc

private fun init_onboarding_theme_dark_desc(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_theme_dark_desc", "onboarding_theme_dark_desc",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41290,
    70),
    )
)

public val Res.string.onboarding_theme_description: StringResource
  get() = CommonMainString0.onboarding_theme_description

private fun init_onboarding_theme_description(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_theme_description", "onboarding_theme_description",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41403,
    72),
    )
)

public val Res.string.onboarding_theme_light: StringResource
  get() = CommonMainString0.onboarding_theme_light

private fun init_onboarding_theme_light(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_theme_light", "onboarding_theme_light",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41548,
    46),
    )
)

public val Res.string.onboarding_theme_light_desc: StringResource
  get() = CommonMainString0.onboarding_theme_light_desc

private fun init_onboarding_theme_light_desc(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_theme_light_desc", "onboarding_theme_light_desc",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41476,
    71),
    )
)

public val Res.string.onboarding_theme_system: StringResource
  get() = CommonMainString0.onboarding_theme_system

private fun init_onboarding_theme_system(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_theme_system", "onboarding_theme_system",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41668,
    51),
    )
)

public val Res.string.onboarding_theme_system_desc: StringResource
  get() = CommonMainString0.onboarding_theme_system_desc

private fun init_onboarding_theme_system_desc(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_theme_system_desc", "onboarding_theme_system_desc",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41595,
    72),
    )
)

public val Res.string.onboarding_theme_title: StringResource
  get() = CommonMainString0.onboarding_theme_title

private fun init_onboarding_theme_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_theme_title", "onboarding_theme_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41720,
    50),
    )
)

public val Res.string.onboarding_welcome_subtitle: StringResource
  get() = CommonMainString0.onboarding_welcome_subtitle

private fun init_onboarding_welcome_subtitle(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_welcome_subtitle", "onboarding_welcome_subtitle",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41771,
    63),
    )
)

public val Res.string.onboarding_welcome_title: StringResource
  get() = CommonMainString0.onboarding_welcome_title

private fun init_onboarding_welcome_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:onboarding_welcome_title", "onboarding_welcome_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41835,
    56),
    )
)

public val Res.string.permission_banner_message: StringResource
  get() = CommonMainString0.permission_banner_message

private fun init_permission_banner_message(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_banner_message", "permission_banner_message",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41892,
    97),
    )
)

public val Res.string.permission_banner_title: StringResource
  get() = CommonMainString0.permission_banner_title

private fun init_permission_banner_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_banner_title", "permission_banner_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 41990,
    55),
    )
)

public val Res.string.permission_close: StringResource
  get() = CommonMainString0.permission_close

private fun init_permission_close(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_close", "permission_close",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42046,
    32),
    )
)

public val Res.string.permission_exact_alarm_instructions: StringResource
  get() = CommonMainString0.permission_exact_alarm_instructions

private fun init_permission_exact_alarm_instructions(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_exact_alarm_instructions", "permission_exact_alarm_instructions",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42079,
    143),
    )
)

public val Res.string.permission_exact_alarm_rationale: StringResource
  get() = CommonMainString0.permission_exact_alarm_rationale

private fun init_permission_exact_alarm_rationale(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_exact_alarm_rationale", "permission_exact_alarm_rationale",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42223,
    168),
    )
)

public val Res.string.permission_exact_alarm_title: StringResource
  get() = CommonMainString0.permission_exact_alarm_title

private fun init_permission_exact_alarm_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_exact_alarm_title", "permission_exact_alarm_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42392,
    64),
    )
)

public val Res.string.permission_fix: StringResource
  get() = CommonMainString0.permission_fix

private fun init_permission_fix(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:permission_fix", "permission_fix",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42621,
    26),
    )
)

public val Res.string.permission_fix_message: StringResource
  get() = CommonMainString0.permission_fix_message

private fun init_permission_fix_message(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_fix_message", "permission_fix_message",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42457,
    110),
    )
)

public val Res.string.permission_fix_title: StringResource
  get() = CommonMainString0.permission_fix_title

private fun init_permission_fix_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_fix_title", "permission_fix_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42568,
    52),
    )
)

public val Res.string.permission_grant: StringResource
  get() = CommonMainString0.permission_grant

private fun init_permission_grant(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_grant", "permission_grant",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42793,
    48),
    )
)

public val Res.string.permission_grant_exact_alarms: StringResource
  get() = CommonMainString0.permission_grant_exact_alarms

private fun init_permission_grant_exact_alarms(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_grant_exact_alarms", "permission_grant_exact_alarms",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42648,
    65),
    )
)

public val Res.string.permission_grant_notifications: StringResource
  get() = CommonMainString0.permission_grant_notifications

private fun init_permission_grant_notifications(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_grant_notifications", "permission_grant_notifications",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42714,
    78),
    )
)

public val Res.string.permission_not_now: StringResource
  get() = CommonMainString0.permission_not_now

private fun init_permission_not_now(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_not_now", "permission_not_now",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42842,
    38),
    )
)

public val Res.string.permission_notification_rationale: StringResource
  get() = CommonMainString0.permission_notification_rationale

private fun init_permission_notification_rationale(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_notification_rationale", "permission_notification_rationale",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 42881,
    221),
    )
)

public val Res.string.permission_notification_title: StringResource
  get() = CommonMainString0.permission_notification_title

private fun init_permission_notification_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_notification_title", "permission_notification_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43103,
    65),
    )
)

public val Res.string.permission_open_settings: StringResource
  get() = CommonMainString0.permission_open_settings

private fun init_permission_open_settings(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_open_settings", "permission_open_settings",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43169,
    52),
    )
)

public val Res.string.permission_warning_compact: StringResource
  get() = CommonMainString0.permission_warning_compact

private fun init_permission_warning_compact(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:permission_warning_compact", "permission_warning_compact",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43222,
    94),
    )
)

public val Res.string.pick_ethiopian: StringResource
  get() = CommonMainString0.pick_ethiopian

private fun init_pick_ethiopian(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:pick_ethiopian", "pick_ethiopian",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43317,
    42),
    )
)

public val Res.string.pick_gregorian: StringResource
  get() = CommonMainString0.pick_gregorian

private fun init_pick_gregorian(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:pick_gregorian", "pick_gregorian",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43360,
    46),
    )
)

public val Res.string.placeholder_coming_soon: StringResource
  get() = CommonMainString0.placeholder_coming_soon

private fun init_placeholder_coming_soon(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:placeholder_coming_soon", "placeholder_coming_soon",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43407,
    51),
    )
)

public val Res.string.placeholder_news_events: StringResource
  get() = CommonMainString0.placeholder_news_events

private fun init_placeholder_news_events(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:placeholder_news_events", "placeholder_news_events",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43459,
    63),
    )
)

public val Res.string.pref_language_dialog_title: StringResource
  get() = CommonMainString0.pref_language_dialog_title

private fun init_pref_language_dialog_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:pref_language_dialog_title", "pref_language_dialog_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43523,
    46),
    )
)

public val Res.string.recurring: StringResource
  get() = CommonMainString0.recurring

private fun init_recurring(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:recurring", "recurring",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43614,
    29),
    )
)

public val Res.string.recurring_event: StringResource
  get() = CommonMainString0.recurring_event

private fun init_recurring_event(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:recurring_event", "recurring_event",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43570,
    43),
    )
)

public val Res.string.reminder: StringResource
  get() = CommonMainString0.reminder

private fun init_reminder(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:reminder", "reminder",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43696,
    28),
    )
)

public val Res.string.reminder_minutes_before: StringResource
  get() = CommonMainString0.reminder_minutes_before

private fun init_reminder_minutes_before(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:reminder_minutes_before", "reminder_minutes_before",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43644,
    51),
    )
)

public val Res.string.repeat: StringResource
  get() = CommonMainString0.repeat

private fun init_repeat(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:repeat", "repeat",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43797,
    22),
    )
)

public val Res.string.repeat_on: StringResource
  get() = CommonMainString0.repeat_on

private fun init_repeat_on(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:repeat_on", "repeat_on",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43725,
    29),
    )
)

public val Res.string.repeat_weekly: StringResource
  get() = CommonMainString0.repeat_weekly

private fun init_repeat_weekly(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:repeat_weekly", "repeat_weekly",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43755,
    41),
    )
)

public val Res.string.screen_title_date_converter: StringResource
  get() = CommonMainString0.screen_title_date_converter

private fun init_screen_title_date_converter(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:screen_title_date_converter", "screen_title_date_converter",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43820,
    55),
    )
)

public val Res.string.screen_title_events: StringResource
  get() = CommonMainString0.screen_title_events

private fun init_screen_title_events(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:screen_title_events", "screen_title_events",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43876,
    35),
    )
)

public val Res.string.screen_title_holidays: StringResource
  get() = CommonMainString0.screen_title_holidays

private fun init_screen_title_holidays(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:screen_title_holidays", "screen_title_holidays",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43912,
    53),
    )
)

public val Res.string.screen_title_month_calendar: StringResource
  get() = CommonMainString0.screen_title_month_calendar

private fun init_screen_title_month_calendar(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:screen_title_month_calendar", "screen_title_month_calendar",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 43966,
    59),
    )
)

public val Res.string.screen_title_more: StringResource
  get() = CommonMainString0.screen_title_more

private fun init_screen_title_more(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:screen_title_more", "screen_title_more",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44026,
    33),
    )
)

public val Res.string.screen_title_settings: StringResource
  get() = CommonMainString0.screen_title_settings

private fun init_screen_title_settings(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:screen_title_settings", "screen_title_settings",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44060,
    41),
    )
)

public val Res.string.select_end_date: StringResource
  get() = CommonMainString0.select_end_date

private fun init_select_end_date(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:select_end_date", "select_end_date",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44102,
    43),
    )
)

public val Res.string.settings_calendar_display: StringResource
  get() = CommonMainString0.settings_calendar_display

private fun init_settings_calendar_display(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_calendar_display", "settings_calendar_display",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44229,
    57),
    )
)

public val Res.string.settings_calendar_display_dialog_title: StringResource
  get() = CommonMainString0.settings_calendar_display_dialog_title

private fun init_settings_calendar_display_dialog_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_calendar_display_dialog_title", "settings_calendar_display_dialog_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44146,
    82),
    )
)

public val Res.string.settings_calendar_ethiopian: StringResource
  get() = CommonMainString0.settings_calendar_ethiopian

private fun init_settings_calendar_ethiopian(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_calendar_ethiopian", "settings_calendar_ethiopian",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44287,
    47),
    )
)

public val Res.string.settings_calendar_gregorian: StringResource
  get() = CommonMainString0.settings_calendar_gregorian

private fun init_settings_calendar_gregorian(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_calendar_gregorian", "settings_calendar_gregorian",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44335,
    47),
    )
)

public val Res.string.settings_calendar_hirji: StringResource
  get() = CommonMainString0.settings_calendar_hirji

private fun init_settings_calendar_hirji(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_calendar_hirji", "settings_calendar_hirji",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44383,
    39),
    )
)

public val Res.string.settings_display_dual_calendar: StringResource
  get() = CommonMainString0.settings_display_dual_calendar

private fun init_settings_display_dual_calendar(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_display_dual_calendar", "settings_display_dual_calendar",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44423,
    66),
    )
)

public val Res.string.settings_display_two_clocks: StringResource
  get() = CommonMainString0.settings_display_two_clocks

private fun init_settings_display_two_clocks(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_display_two_clocks", "settings_display_two_clocks",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44490,
    59),
    )
)

public val Res.string.settings_ethiopian_gregorian_display: StringResource
  get() = CommonMainString0.settings_ethiopian_gregorian_display

private fun init_settings_ethiopian_gregorian_display(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_ethiopian_gregorian_display", "settings_ethiopian_gregorian_display",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44550,
    84),
    )
)

public val Res.string.settings_holidays_display: StringResource
  get() = CommonMainString0.settings_holidays_display

private fun init_settings_holidays_display(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_holidays_display", "settings_holidays_display",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44710,
    61),
    )
)

public val Res.string.settings_holidays_display_dialog_title: StringResource
  get() = CommonMainString0.settings_holidays_display_dialog_title

private fun init_settings_holidays_display_dialog_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_holidays_display_dialog_title", "settings_holidays_display_dialog_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44635,
    74),
    )
)

public val Res.string.settings_orthodox_day_names_button: StringResource
  get() = CommonMainString0.settings_orthodox_day_names_button

private fun init_settings_orthodox_day_names_button(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_orthodox_day_names_button", "settings_orthodox_day_names_button",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44772,
    66),
    )
)

public val Res.string.settings_orthodox_day_names_dialog_title: StringResource
  get() = CommonMainString0.settings_orthodox_day_names_dialog_title

private fun init_settings_orthodox_day_names_dialog_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_orthodox_day_names_dialog_title", "settings_orthodox_day_names_dialog_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44839,
    72),
    )
)

public val Res.string.settings_primary_calendar: StringResource
  get() = CommonMainString0.settings_primary_calendar

private fun init_settings_primary_calendar(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_primary_calendar", "settings_primary_calendar",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44912,
    69),
    )
)

public val Res.string.settings_primary_timezone: StringResource
  get() = CommonMainString0.settings_primary_timezone

private fun init_settings_primary_timezone(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_primary_timezone", "settings_primary_timezone",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 44982,
    65),
    )
)

public val Res.string.settings_secondary_calendar: StringResource
  get() = CommonMainString0.settings_secondary_calendar

private fun init_settings_secondary_calendar(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_secondary_calendar", "settings_secondary_calendar",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45048,
    75),
    )
)

public val Res.string.settings_secondary_timezone: StringResource
  get() = CommonMainString0.settings_secondary_timezone

private fun init_settings_secondary_timezone(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_secondary_timezone", "settings_secondary_timezone",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45124,
    71),
    )
)

public val Res.string.settings_show_cultural_holidays: StringResource
  get() = CommonMainString0.settings_show_cultural_holidays

private fun init_settings_show_cultural_holidays(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_show_cultural_holidays", "settings_show_cultural_holidays",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45196,
    71),
    )
)

public val Res.string.settings_show_day_off_holidays: StringResource
  get() = CommonMainString0.settings_show_day_off_holidays

private fun init_settings_show_day_off_holidays(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_show_day_off_holidays", "settings_show_day_off_holidays",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45268,
    66),
    )
)

public val Res.string.settings_show_muslim_working_holidays: StringResource
  get() = CommonMainString0.settings_show_muslim_working_holidays

private fun init_settings_show_muslim_working_holidays(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_show_muslim_working_holidays", "settings_show_muslim_working_holidays",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45335,
    89),
    )
)

public val Res.string.settings_show_orthodox_day_names: StringResource
  get() = CommonMainString0.settings_show_orthodox_day_names

private fun init_settings_show_orthodox_day_names(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_show_orthodox_day_names", "settings_show_orthodox_day_names",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45425,
    72),
    )
)

public val Res.string.settings_show_orthodox_working_holidays: StringResource
  get() = CommonMainString0.settings_show_orthodox_working_holidays

private fun init_settings_show_orthodox_working_holidays(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_show_orthodox_working_holidays", "settings_show_orthodox_working_holidays",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45498,
    95),
    )
)

public val Res.string.settings_show_us_holidays: StringResource
  get() = CommonMainString0.settings_show_us_holidays

private fun init_settings_show_us_holidays(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_show_us_holidays", "settings_show_us_holidays",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45594,
    57),
    )
)

public val Res.string.settings_timezone_hint: StringResource
  get() = CommonMainString0.settings_timezone_hint

private fun init_settings_timezone_hint(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_timezone_hint", "settings_timezone_hint",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45652,
    82),
    )
)

public val Res.string.settings_use_24_hour_format: StringResource
  get() = CommonMainString0.settings_use_24_hour_format

private fun init_settings_use_24_hour_format(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_use_24_hour_format", "settings_use_24_hour_format",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45735,
    75),
    )
)

public val Res.string.settings_use_geez_numbers: StringResource
  get() = CommonMainString0.settings_use_geez_numbers

private fun init_settings_use_geez_numbers(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_use_geez_numbers", "settings_use_geez_numbers",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45811,
    57),
    )
)

public val Res.string.settings_use_transparent_background: StringResource
  get() = CommonMainString0.settings_use_transparent_background

private fun init_settings_use_transparent_background(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_use_transparent_background", "settings_use_transparent_background",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45869,
    79),
    )
)

public val Res.string.settings_widget_dialog_title: StringResource
  get() = CommonMainString0.settings_widget_dialog_title

private fun init_settings_widget_dialog_title(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_widget_dialog_title", "settings_widget_dialog_title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 45949,
    56),
    )
)

public val Res.string.settings_widget_options: StringResource
  get() = CommonMainString0.settings_widget_options

private fun init_settings_widget_options(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:settings_widget_options", "settings_widget_options",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46006,
    51),
    )
)

public val Res.string.show_all: StringResource
  get() = CommonMainString0.show_all

private fun init_show_all(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:show_all", "show_all",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46058,
    28),
    )
)

public val Res.string.show_reminder_before: StringResource
  get() = CommonMainString0.show_reminder_before

private fun init_show_reminder_before(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:show_reminder_before", "show_reminder_before",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46087,
    56),
    )
)

public val Res.string.showing_all_events: StringResource
  get() = CommonMainString0.showing_all_events

private fun init_showing_all_events(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:showing_all_events", "showing_all_events",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46144,
    50),
    )
)

public val Res.string.showing_from: StringResource
  get() = CommonMainString0.showing_from

private fun init_showing_from(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:showing_from", "showing_from",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46255,
    44),
    )
)

public val Res.string.showing_from_to: StringResource
  get() = CommonMainString0.showing_from_to

private fun init_showing_from_to(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:showing_from_to", "showing_from_to",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46195,
    59),
    )
)

public val Res.string.showing_until: StringResource
  get() = CommonMainString0.showing_until

private fun init_showing_until(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:showing_until", "showing_until",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46300,
    45),
    )
)

public val Res.string.start_time: StringResource
  get() = CommonMainString0.start_time

private fun init_start_time(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:start_time", "start_time",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46346,
    34),
    )
)

public val Res.string.tap_plus_to_add_event: StringResource
  get() = CommonMainString0.tap_plus_to_add_event

private fun init_tap_plus_to_add_event(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:tap_plus_to_add_event", "tap_plus_to_add_event",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46381,
    85),
    )
)

public val Res.string.time_ago: StringResource
  get() = CommonMainString0.time_ago

private fun init_time_ago(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:time_ago", "time_ago",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46467,
    36),
    )
)

public val Res.string.time_day_plural: StringResource
  get() = CommonMainString0.time_day_plural

private fun init_time_day_plural(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:time_day_plural", "time_day_plural",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46504,
    31),
    )
)

public val Res.string.time_day_singular: StringResource
  get() = CommonMainString0.time_day_singular

private fun init_time_day_singular(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_day_singular", "time_day_singular",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46536,
    29),
    )
)

public val Res.string.time_hour_plural: StringResource
  get() = CommonMainString0.time_hour_plural

private fun init_time_hour_plural(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_hour_plural", "time_hour_plural",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46566,
    32),
    )
)

public val Res.string.time_hour_singular: StringResource
  get() = CommonMainString0.time_hour_singular

private fun init_time_hour_singular(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_hour_singular", "time_hour_singular",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46599,
    34),
    )
)

public val Res.string.time_in_a_moment: StringResource
  get() = CommonMainString0.time_in_a_moment

private fun init_time_in_a_moment(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_in_a_moment", "time_in_a_moment",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46634,
    40),
    )
)

public val Res.string.time_in_future: StringResource
  get() = CommonMainString0.time_in_future

private fun init_time_in_future(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:time_in_future", "time_in_future",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46675,
    38),
    )
)

public val Res.string.time_just_now: StringResource
  get() = CommonMainString0.time_just_now

private fun init_time_just_now(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:time_just_now", "time_just_now",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46714,
    33),
    )
)

public val Res.string.time_minute_plural: StringResource
  get() = CommonMainString0.time_minute_plural

private fun init_time_minute_plural(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_minute_plural", "time_minute_plural",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46748,
    38),
    )
)

public val Res.string.time_minute_singular: StringResource
  get() = CommonMainString0.time_minute_singular

private fun init_time_minute_singular(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_minute_singular", "time_minute_singular",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46787,
    36),
    )
)

public val Res.string.time_month_plural: StringResource
  get() = CommonMainString0.time_month_plural

private fun init_time_month_plural(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_month_plural", "time_month_plural",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46824,
    33),
    )
)

public val Res.string.time_month_singular: StringResource
  get() = CommonMainString0.time_month_singular

private fun init_time_month_singular(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_month_singular", "time_month_singular",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46858,
    35),
    )
)

public val Res.string.time_week_plural: StringResource
  get() = CommonMainString0.time_week_plural

private fun init_time_week_plural(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_week_plural", "time_week_plural",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46894,
    32),
    )
)

public val Res.string.time_week_singular: StringResource
  get() = CommonMainString0.time_week_singular

private fun init_time_week_singular(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_week_singular", "time_week_singular",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46927,
    34),
    )
)

public val Res.string.time_year_plural: StringResource
  get() = CommonMainString0.time_year_plural

private fun init_time_year_plural(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_year_plural", "time_year_plural",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46962,
    32),
    )
)

public val Res.string.time_year_singular: StringResource
  get() = CommonMainString0.time_year_singular

private fun init_time_year_singular(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:time_year_singular", "time_year_singular",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 46995,
    34),
    )
)

public val Res.string.title: StringResource
  get() = CommonMainString0.title

private fun init_title(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:title", "title",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 47077,
    21),
    )
)

public val Res.string.title_required: StringResource
  get() = CommonMainString0.title_required

private fun init_title_required(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:title_required", "title_required",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 47030,
    46),
    )
)

public val Res.string.until: StringResource
  get() = CommonMainString0.until

private fun init_until(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:until", "until",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 47134,
    21),
    )
)

public val Res.string.until_date: StringResource
  get() = CommonMainString0.until_date

private fun init_until_date(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:until_date", "until_date",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 47099,
    34),
    )
)

public val Res.string.update: StringResource
  get() = CommonMainString0.update

private fun init_update(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:update", "update",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 47156,
    22),
    )
)

public val Res.string.weekly: StringResource
  get() = CommonMainString0.weekly

private fun init_weekly(): StringResource = org.jetbrains.compose.resources.StringResource(
  "string:weekly", "weekly",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 47179,
    22),
    )
)

public val Res.string.widget_description: StringResource
  get() = CommonMainString0.widget_description

private fun init_widget_description(): StringResource =
    org.jetbrains.compose.resources.StringResource(
  "string:widget_description", "widget_description",
    setOf(
      org.jetbrains.compose.resources.ResourceItem(setOf(),
    "composeResources/com.shalom.calendar.shared.resources/values/strings.commonMain.cvr", 47202,
    114),
    )
)
