DESCRIPTION = "Pine64 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.13"
LINUX_VERSION_EXTENSION = "-pinephone"

PV = "${LINUX_VERSION}-git${SRCPV}"

KERNEL_VERSION_SANITY_SKIP="1"

# KBUILD_DEFCONFIG:pinephone = "pinephone_defconfig"

LINUX_BRANCH = "orange-pi-${LINUX_VERSION}"
LINUX_KMETA_BRANCH = "yocto-${LINUX_VERSION}"
SRCREV_machine = "98029b03d3bba7d49ae89c9ab6d806422a0e5f80"
SRCREV_meta = "de9584096b41f34805b9a2f3151cfbd844326148"
KMETA = "kernel-meta"
SRC_URI = " \
    git://github.com/megous/linux;protocol=https;branch=${LINUX_BRANCH};name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=${LINUX_KMETA_BRANCH};destsuffix=${KMETA};name=meta \
    file://0001-dts-pinephone-drop-modem-power-node.patch \
    file://0002-dts-pinephone-jack-detection.patch \
    file://defconfig \
    file://extra.cfg \
"

COMPATIBLE_MACHINE = "pinephone"
