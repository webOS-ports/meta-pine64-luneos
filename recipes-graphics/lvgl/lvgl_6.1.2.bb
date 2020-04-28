DESCRIPTION = "Open-source Embedded GUI Library"
HOMEPAGE = "https://littlevgl.com/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# DEPENDS = ""

SRC_URI = " \
	git://github.com/littlevgl/lvgl.git;name=lvgl;protocol=https;destsuffix=git/lvgl \
	git://github.com/littlevgl/lv_drivers.git;name=lvdrivers;protocol=https;destsuffix=git/lv_drivers \
	\
	file://CMakeLists.txt;subdir=git/ \
	file://lv_drv_conf.h;subdir=git/ \
	file://lv_conf.h;subdir=git/ \
"
SRCREV_lvgl = "2ead4959038fcfd01eaee0f124b98a8ae3efec21"
SRCREV_lvdrivers = "24cedaa756b83c50c36f4aa0c625fcdfba89431b"

S = "${WORKDIR}/git"

inherit cmake

do_install_append() {
    cp -v ${S}/lv_drv_conf.h ${S}/lv_conf.h ${D}/${includedir}/
}

# We build only static library (needed to include in SDK)
ALLOW_EMPTY_${PN} = "1"
