DESCRIPTION = "LuneOS Recovery UI"
HOMEPAGE = "https://www.webos-ports.org/"

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

DEPENDS = "lvgl"

SRC_URI = "git://github.com/Tofee/luneos-recovery-ui.git;branch=lvgl-9.0;protocol=https \
"
SRCREV = "fed0dccbfdc27db9b69a352fddfca49cf66620ba"

S = "${WORKDIR}/git/recovery"

inherit cmake

TARGET_CFLAGS:append = " -DLV_CONF_INCLUDE_SIMPLE=1 -I${RECIPE_SYSROOT}/${includedir}/lvgl"

TARGET_CFLAGS:pinetab2:append = " -DEMMC_PATH='\"/dev/mmcblk1\"'"

# Because meta-pine64-luneos/recipes-graphics/lvgl/lvgl_%.bbappend makes lvgl MACHINE_ARCH as well
PACKAGE_ARCH = "${MACHINE_ARCH}"
