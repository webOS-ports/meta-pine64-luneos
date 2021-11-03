DESCRIPTION = "LuneOS Recovery UI"
HOMEPAGE = "https://www.webos-ports.org/"

LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

DEPENDS = "lvgl"

SRC_URI = "git://github.com/Tofee/luneos-recovery-ui.git;branch=master;protocol=https"
SRCREV = "e0a40ea1a12a03e97322634ee80ae390a2c23931"

S = "${WORKDIR}/git/recovery"

inherit cmake
