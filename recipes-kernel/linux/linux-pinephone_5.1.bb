DESCRIPTION = "Pine64 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.1"
LINUX_VERSION_EXTENSION = "-pinephone"

KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "pinephone-dontbeevil-5.1-rc7"
SRCREV = "bc1984d747095d285236f0d20f691e421f3bbfc3"
SRC_URI = " \
          git://gitlab.com/pine64-org/linux.git;protocol=https;branch=${BRANCH} \
	  file://defconfig \
	  file://extra.cfg \
	  "

COMPATIBLE_MACHINE = "pinephone"
