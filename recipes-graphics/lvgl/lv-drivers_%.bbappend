FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += " file://lv_drv_conf.patch"

