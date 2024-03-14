DESCRIPTION = "LuneOS Recovery UI"
HOMEPAGE = "https://www.webos-ports.org/"

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

DEPENDS = "lvgl"

SRC_URI = "git://github.com/Tofee/luneos-recovery-ui.git;branch=lvgl-9.0;protocol=https \
"
SRCREV = "d9973a191e249d5ee436072c405da0433882e4c7"

S = "${WORKDIR}/git/recovery"

inherit cmake

TARGET_CFLAGS += "-DLV_CONF_INCLUDE_SIMPLE=1"
TARGET_CFLAGS += "-I${RECIPE_SYSROOT}/${includedir}/lvgl"

# Because meta-pine64-luneos/recipes-graphics/lvgl/lvgl_%.bbappend makes lvgl MACHINE_ARCH as well
PACKAGE_ARCH = "${MACHINE_ARCH}"
