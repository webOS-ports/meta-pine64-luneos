DESCRIPTION = "PineTab2 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

COMPATIBLE_MACHINE = "pinetab2"

LINUX_VERSION ?= "6.9.0"
LINUX_VERSION_EXTENSION = "-pinetab2"

PV = "${LINUX_VERSION}-git"

KERNEL_VERSION_SANITY_SKIP="1"

LINUX_KMETA_BRANCH = "yocto-dev"

SRCREV_machine = "586b5dfb51b962c1b6c06495715e4c4f76a7fc5a"
SRCREV_meta = "6ae0f49095f26f3133d00772d8d9ca775fdacf9c"
KMETA = "kernel-meta"
SRC_URI = " \
    git://github.com/torvalds/linux.git;branch=master;protocol=https;name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=master;destsuffix=${KMETA};name=meta \
    file://defconfig \
    file://0001-rk3566-pinetab2.dtsi-Add-required-bits-for-WiFi.patch \
    file://0002-power-supply-rk817-Fix-battery-capacity-sanity-check.patch \
    file://0006-Patch-linux-framebuffer-logo-for-LuneOS.patch \
"

KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"
