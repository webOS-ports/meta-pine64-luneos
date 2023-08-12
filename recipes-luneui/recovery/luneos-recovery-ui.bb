DESCRIPTION = "LuneOS Recovery UI"
HOMEPAGE = "https://www.webos-ports.org/"

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

DEPENDS = "lvgl lv-drivers"

SRC_URI = "git://github.com/Tofee/luneos-recovery-ui.git;branch=master;protocol=https \
    file://0001-recovery-update-for-lvgl-v8.3.0-fbdev_get_sizes-lv_t.patch;patchdir=.. \
"
SRCREV = "6003b1828706c249cccb490e29268c9ec18f262c"

S = "${WORKDIR}/git/recovery"

inherit cmake

TARGET_CFLAGS += "-DLV_CONF_INCLUDE_SIMPLE=1"
TARGET_CFLAGS += "-I${RECIPE_SYSROOT}/${includedir}/lvgl -I${RECIPE_SYSROOT}/${includedir}/lvgl/lv_drivers"
