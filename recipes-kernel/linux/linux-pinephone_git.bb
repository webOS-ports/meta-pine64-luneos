DESCRIPTION = "Pine64 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.12"
LINUX_VERSION_EXTENSION = "-pinephone"

KERNEL_VERSION_SANITY_SKIP="1"

# KBUILD_DEFCONFIG_pinephone = "pinephone_defconfig"

BRANCH = "orange-pi-5.12"
SRCREV = "4b9f3342ecc64267d472fd555e93595e5bd33ae4"
SRC_URI = " \
    git://github.com/megous/linux;protocol=https;branch=${BRANCH} \
    file://0001-dts-pinephone-drop-modem-power-node.patch \
    file://0002-dts-pinephone-jack-detection.patch \
    file://defconfig \
    file://extra.cfg \
"

COMPATIBLE_MACHINE = "pinephone"
