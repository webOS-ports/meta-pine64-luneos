DESCRIPTION = "PinePhonePro Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

COMPATIBLE_MACHINE = "pinephonepro"

LINUX_VERSION ?= "6.5.2"
LINUX_VERSION_EXTENSION = "-pinephonepro"

PV = "${LINUX_VERSION}-git"

KERNEL_VERSION_SANITY_SKIP="1"

LINUX_KMETA_BRANCH = "yocto-dev"
SRCREV_machine = "309ad54d4c29193d272ed0b3903718b9a241c8b3" 
SRCREV_meta = "94bfc55e50d9962af2da6d3bc5ee7c205d0df323"
KMETA = "kernel-meta"
SRC_URI = " \
    git://github.com/herrie82/kernel-megi.git;branch=herrie/LuneOS-6.5;protocol=https;name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=master;destsuffix=${KMETA};name=meta \
    file://defconfig \
    file://0001-dts-pinephone-pro-add-modem-ri.patch \
    file://0002-dts-pinephone-pro-remove-modem-node.patch \
    file://0003-Revert-usb-quirks-Add-USB_QUIRK_RESET-for-Quectel-EG25G-Modem.patch \
    file://0004-usb-serial-option-add-reset-resume-callback-for-WWAN.patch \
"

KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"
