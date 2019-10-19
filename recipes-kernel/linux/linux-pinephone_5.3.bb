DESCRIPTION = "Pine64 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.3"
LINUX_VERSION_EXTENSION = "-pinephone"

KERNEL_VERSION_SANITY_SKIP="1"

KBUILD_DEFCONFIG_pinephone = "pinephone_defconfig"

BRANCH = "pine64-kernel"
SRCREV = "59d7c1893540d73581cf5679d5dc79cf84668606"
SRC_URI = " \
          git://gitlab.com/pine64-org/linux.git;protocol=https;branch=${BRANCH} \
	  file://0001-dts-touchscreen-fix.patch \
          file://extra.cfg \
	  "

COMPATIBLE_MACHINE = "pinephone"
