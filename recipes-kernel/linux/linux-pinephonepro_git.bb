DESCRIPTION = "PinePhonePro Linux Kernel"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.17"
LINUX_VERSION_EXTENSION = "-pinephonepro"

PV = "${LINUX_VERSION}-git${SRCPV}"

KERNEL_VERSION_SANITY_SKIP="1"

LINUX_KMETA_BRANCH = "yocto-dev"
SRCREV_machine = "3c21ce2225ed70086783d87f8fbeb2d6026c7034" 
SRCREV_meta = "94bfc55e50d9962af2da6d3bc5ee7c205d0df323"
KMETA = "kernel-meta"
SRC_URI = " \
    git://github.com/megous/linux.git;branch=orange-pi-5.17;protocol=https;name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=master;destsuffix=${KMETA};name=meta \
    file://defconfig \
"

#    git://gitlab.com/pine64-org/linux.git;branch=pine64-kernel-ppp-5.16.y;protocol=https;name=machine 

KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"

COMPATIBLE_MACHINE = "pinephonepro"
