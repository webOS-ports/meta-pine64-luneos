DESCRIPTION = "Pine64 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.19"
LINUX_VERSION_EXTENSION = "-pinephone"

PV = "${LINUX_VERSION}-git${SRCPV}"

KERNEL_VERSION_SANITY_SKIP="1"

# KBUILD_DEFCONFIG:pinephone = "pinephone_defconfig"

LINUX_BRANCH = "orange-pi-${LINUX_VERSION}"
LINUX_KMETA_BRANCH = "yocto-${LINUX_VERSION}"
SRCREV_machine = "b8fd52d433a566c54a58fcec65fb479efd15bff2"
SRCREV_meta = "350b544d077955b599b54ab364f6227d96a90455"
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
