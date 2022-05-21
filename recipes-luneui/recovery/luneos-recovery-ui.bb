DESCRIPTION = "LuneOS Recovery UI"
HOMEPAGE = "https://www.webos-ports.org/"

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

DEPENDS = "lvgl lv-drivers"

SRC_URI = "git://github.com/Tofee/luneos-recovery-ui.git;branch=master;protocol=https"
SRCREV = "7a82c1ad1821dcd8b9d0bf0500dae505824ae0e7"

S = "${WORKDIR}/git/recovery"

inherit cmake

TARGET_CFLAGS += "-DLV_CONF_INCLUDE_SIMPLE=1"
TARGET_CFLAGS += "-I${RECIPE_SYSROOT}/${includedir}/lvgl -I${RECIPE_SYSROOT}/${includedir}/lvgl/lv_drivers"
