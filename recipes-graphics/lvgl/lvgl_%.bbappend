FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " file://0001-lv_conf_template.h-Patch-for-LuneOS.patch"
