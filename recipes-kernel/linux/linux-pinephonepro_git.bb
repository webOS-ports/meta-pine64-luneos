DESCRIPTION = "PinePhonePro Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.16"
LINUX_VERSION_EXTENSION = "-pinephonepro"

PV = "${LINUX_VERSION}-git${SRCPV}"

KERNEL_VERSION_SANITY_SKIP="1"

LINUX_BRANCH = "orange-pi-${LINUX_VERSION}"
#LINUX_KMETA_BRANCH = "yocto-${LINUX_VERSION}"
LINUX_KMETA_BRANCH = "yocto-dev"
SRCREV_machine = "c6fda0a09217b8c183cf1ef4782746fcfcf055f1"
SRCREV_meta = "94bfc55e50d9962af2da6d3bc5ee7c205d0df323"
KMETA = "kernel-meta"
SRC_URI = " \
    git://github.com/megous/linux.git;protocol=https;branch=${LINUX_BRANCH};name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=master;destsuffix=${KMETA};name=meta \
    file://defconfig \
"

KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"

COMPATIBLE_MACHINE = "pinephonepro"
