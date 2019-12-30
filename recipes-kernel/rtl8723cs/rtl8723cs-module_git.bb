SECTION = "kernel"
SUMMARY = "rtl8723cs kernel module for mainline"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://core/rtw_ap.c;beginline=5;endline=16;md5=489f99735a5c5aabb3579f49eb06aeb0"

DEPENDS = "virtual/kernel"

inherit module

PV = "1.0.20190730+git${SRCPV}"
SRCREV = "d7db077004f1497800faabb0e6da775391393711"

SRC_URI = "git://github.com/Icenowy/rtl8723cs.git;branch=master;protocol=git"
S = "${WORKDIR}/git"

EXTRA_OEMAKE += "KVER=${KERNEL_VERSION} KSRC=${STAGING_KERNEL_DIR} SUBARCH=${@map_kernel_arch(d.getVar('TARGET_ARCH'), d)}"

KERNEL_MODULE_AUTOLOAD += "${MODULE_NAME}"
