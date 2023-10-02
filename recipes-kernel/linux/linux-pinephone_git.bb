DESCRIPTION = "Pine64 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "6.3"
LINUX_VERSION_EXTENSION = "-pinephone"

PV = "${LINUX_VERSION}-git"

KERNEL_VERSION_SANITY_SKIP="1"

# KBUILD_DEFCONFIG:pinephone = "pinephone_defconfig"

LINUX_BRANCH = "orange-pi-${LINUX_VERSION}"
LINUX_KMETA_BRANCH = "yocto-${LINUX_VERSION}"
SRCREV_machine = "08ee78c9dfda5effd34848b6f1ae6ce10c466bad"
SRCREV_meta = "aeb1ad4a2a72e2acf206fac02da0551fbd29b9d5"
KMETA = "kernel-meta"
SRC_URI = " \
    git://github.com/sailfish-on-dontbeevil/kernel-megi.git;protocol=https;branch=${LINUX_BRANCH};name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=master;destsuffix=${KMETA};name=meta \
    file://0001-dts-pinephone-drop-modem-power-node.patch \
    file://0002-dts-pinephone-jack-detection.patch \
    file://defconfig \
    file://extra.cfg \
"

COMPATIBLE_MACHINE = "pinephone"
