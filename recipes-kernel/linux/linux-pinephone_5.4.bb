DESCRIPTION = "Pine64 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.4"
LINUX_VERSION_EXTENSION = "-pinephone"

KERNEL_VERSION_SANITY_SKIP="1"

KBUILD_DEFCONFIG_pinephone = "pine64_defconfig"

BRANCH = "pine64-kernel"
SRCREV = "bbcb007bba2c10fa42d1c323585358c3b284bc52"
SRC_URI = " \
          git://gitlab.com/pine64-org/linux.git;protocol=https;branch=${BRANCH} \
          file://extra.cfg \
	  "

COMPATIBLE_MACHINE = "pinephone"
