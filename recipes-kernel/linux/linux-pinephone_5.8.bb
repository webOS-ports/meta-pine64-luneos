DESCRIPTION = "Pine64 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.8"
LINUX_VERSION_EXTENSION = "-pinephone"

KERNEL_VERSION_SANITY_SKIP="1"

#KBUILD_DEFCONFIG_pinephone = "pine64_defconfig"

BRANCH = "pine64-kernel-5.8.y"
SRCREV = "e1c26b7bd643515d3be20268cd2385df2388f8b9"
SRC_URI = " \
          git://gitlab.com/pine64-org/linux.git;protocol=https;branch=${BRANCH} \
          file://reversed-disable-gold-linker.patch \
          file://defconfig \
          file://extra.cfg \
	  "

COMPATIBLE_MACHINE = "pinephone"
