DESCRIPTION = "Pine64 Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.0"
LINUX_VERSION_EXTENSION = "-pinephone"

KERNEL_VERSION_SANITY_SKIP="1"

BRANCH = "pinephone-dontbeevil-5.1-rc7"
SRCREV = "90589e5043bf6bc5860e4e20daea9ffcb58e84b8"
SRC_URI = " \
          git://gitlab.com/pine64-org/linux.git;protocol=https;branch=${BRANCH} \
	  https://gitlab.com/pine64-org/linux/raw/${SRCREV}/pinephone-config;downloadfilename=defconfig \
	  file://extra.cfg \
	  "
SRC_URI[md5sum] = "c254fc82545764bd003b5a49ef68e933"

COMPATIBLE_MACHINE = "pinephone"
