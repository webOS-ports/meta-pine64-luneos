DESCRIPTION = "PineTab2 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

COMPATIBLE_MACHINE = "pinetab2"

LINUX_VERSION ?= "6.6.0"
LINUX_VERSION_EXTENSION = "-pinetab2"

PV = "${LINUX_VERSION}-git${SRCPV}"

KERNEL_VERSION_SANITY_SKIP="1"

LINUX_KMETA_BRANCH = "yocto-dev"

SRCREV_machine = "9ff6e780db90cde2439980d4da84d6f7d4e3d989"
SRCREV_meta = "90961048e63a4ddb3bc70cac6982930ac3b8631f"
KMETA = "kernel-meta"
SRC_URI = " \
    git://gitlab.com/ook37/linux.git;branch=okpine-6.6;protocol=https;name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=master;destsuffix=${KMETA};name=meta \
    file://defconfig \
"

KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"
