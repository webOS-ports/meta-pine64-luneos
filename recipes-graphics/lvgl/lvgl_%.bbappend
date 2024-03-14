FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGECONFIG += "fbdev"

LVGL_CONFIG_LV_MEM_SIZE             = "(1024U * 1024U)"
LVGL_CONFIG_LV_USE_LOG              = "1"
LVGL_CONFIG_LV_LOG_LEVEL            = "LV_LOG_LEVEL_INFO"
LVGL_CONFIG_LV_LOG_PRINTF           = "1"
LVGL_CONFIG_LV_USE_FONT_COMPRESSED  = "1"
LVGL_CONFIG_LV_THEME_DEFAULT_DARK   = "1"

do_configure:append() {
    sed -e "s|#if 0 .*Set it to \"1\" to enable content.*|#if 1 // Enabled by ${PN} recipe|g" \
        -e "s|\(^ \+#define LV_MEM_SIZE \).*|\1${LVGL_CONFIG_LV_MEM_SIZE}|g" \
        -e "s|\(^ \+#define LV_USE_LOG \).*|\1${LVGL_CONFIG_LV_USE_LOG}|g" \
        -e "s|\(^ \+#define LV_LOG_LEVEL \).*|\1${LVGL_CONFIG_LV_LOG_LEVEL}|g" \
        -e "s|\(^ \+#define LV_LOG_PRINTF \).*|\1${LVGL_CONFIG_LV_LOG_PRINTF}|g" \
        -e "s|\(^ \+#define LV_USE_FONT_COMPRESSED \).*|\1${LVGL_CONFIG_LV_USE_FONT_COMPRESSED}|g" \
        -e "s|\(^ \+#define LV_THEME_DEFAULT_DARK \).*|\1${LVGL_CONFIG_LV_THEME_DEFAULT_DARK}|g" \
        \
        -i "${S}/lv_conf.h"
}

do_install:append() {
    install -d "${D}${includedir}/lvgl"
    install -m 0644 "${S}/lv_conf.h" "${D}${includedir}/lvgl/lv_conf.h"
}
