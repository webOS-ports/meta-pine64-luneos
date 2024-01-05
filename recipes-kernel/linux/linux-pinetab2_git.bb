DESCRIPTION = "PineTab2 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

COMPATIBLE_MACHINE = "pinetab2"

LINUX_VERSION ?= "6.6.9"
LINUX_VERSION_EXTENSION = "-pinetab2"

PV = "${LINUX_VERSION}-git"

KERNEL_VERSION_SANITY_SKIP="1"

LINUX_KMETA_BRANCH = "yocto-dev"

SRCREV_machine = "5e9df83a705290c4d974693097df1da9cbe25854"
SRCREV_meta = "11390e802ca72f3549b9356f036b17e54afd7a34"
KMETA = "kernel-meta"
SRC_URI = " \
    git://gitlab.com/linux-kernel/stable.git;branch=linux-6.6.y;protocol=https;name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=yocto-6.6;destsuffix=${KMETA};name=meta \
    file://defconfig \
    file://0001-arm64-dts-rockchip-Add-Pine64-PineTab2-device-trees.patch \
    file://0002-power-supply-rk817-Fix-battery-capacity-sanity-check.patch \
    file://0003-drm-panel-Add-BOE-TH101MB31IG002-28A-MIPI-DSI-LCD-pa.patch \
    file://0004-drm-panel-boe-th101mb31ig002-28a-Various-improvement.patch \
    file://0005-drivers-staging-Add-Bestechnic-BES2600-driver.patch \
    file://0006-arm64-dts-rockchip-pinetab2-Apply-BES-changes.patch \
    file://0007-drivers-staging-bes2600-Use-device-tree-for-platform.patch \
    file://0008-drivers-staging-bes2600-Use-lib-firmware-bes2600-for.patch \
    file://0009-arm64-dts-rockchip-pinetab2-Add-Bestechnic-BES2600-d.patch \
"

KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"
