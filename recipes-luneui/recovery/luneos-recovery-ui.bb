DESCRIPTION = "LuneOS Recovery UI"
HOMEPAGE = "https://www.webos-ports.org/"

LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://../LICENSE;md5=1ebbd3e34237af26da5dc08a4e440464"

DEPENDS = "lvgl"

SRC_URI = "git://github.com/Tofee/luneos-recovery-ui.git;branch=master;protocol=https"
SRCREV = "7cf85653ab8ac65cb0d8b0df0b120a452097af0a"

S = "${WORKDIR}/git/recovery"

inherit cmake
