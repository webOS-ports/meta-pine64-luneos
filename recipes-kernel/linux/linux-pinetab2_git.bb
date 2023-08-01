DESCRIPTION = "PineTab2 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

COMPATIBLE_MACHINE = "pinetab2"

LINUX_VERSION ?= "6.5.0"
LINUX_VERSION_EXTENSION = "-pinetab2"

PV = "${LINUX_VERSION}-git${SRCPV}"

KERNEL_VERSION_SANITY_SKIP="1"

LINUX_KMETA_BRANCH = "yocto-dev"

# For the 6.4.7 branch
# SRCREV_machine = "cf58284c0b9966f96b416c979fd47005819d9d7c" 
# git://github.com/dreemurrs-embedded/linux-pinetab2;nobranch=1;protocol=https;name=machine

SRCREV_machine = "c719e0cb9aa1eeb349f45263c806b81c21f51edb"
SRCREV_meta = "90961048e63a4ddb3bc70cac6982930ac3b8631f"
KMETA = "kernel-meta"
SRC_URI = " \
    git://github.com/megous/linux;branch=pt2-6.5;protocol=https;name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=master;destsuffix=${KMETA};name=meta \
    file://defconfig \
"

KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"

