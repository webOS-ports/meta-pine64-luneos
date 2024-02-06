DESCRIPTION = "PineTab2 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

COMPATIBLE_MACHINE = "pinetab2"

LINUX_VERSION ?= "6.6.13"
LINUX_VERSION_EXTENSION = "-pinetab2"

PV = "${LINUX_VERSION}-git${SRCPV}"

KERNEL_VERSION_SANITY_SKIP="1"

LINUX_KMETA_BRANCH = "yocto-dev"

SRCREV_machine = "cd9a1db16fc2b558c72939a6e430b0699717c2b8"
SRCREV_meta = "11390e802ca72f3549b9356f036b17e54afd7a34"
KMETA = "kernel-meta"
SRC_URI = " \
    git://github.com/dreemurrs-embedded/linux-pinetab2.git;nobranch=1;protocol=https;name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=${KMETA};name=meta \
    file://0001-bes2600-Use-fixed-mac-address-hack.patch \
    file://0002-Patch-linux-framebuffer-logo-for-LuneOS.patch \
    file://defconfig \
"

KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"
