FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGECONFIG += "fbdev"

SRC_URI += " file://0001-lv_conf_template.h-Patch-for-LuneOS.patch"

do_install:append() {
    install -d "${D}${includedir}/lvgl"
    install -m 0644 "${S}/lv_conf.h" "${D}${includedir}/lvgl/lv_conf.h"
}
