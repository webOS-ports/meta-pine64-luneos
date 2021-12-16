SUMMARY = "Initramfs image to boot from internal flash with initramfs, using uboot"
DESCRIPTION = "Provides a minimal environment to bootstrap and run LuneOS from U-Boot"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

IMAGE_INSTALL = "busybox base-passwd bash util-linux-blkid"
IMAGE_INSTALL += "initramfs-scripts-simple luneos-recovery-ui"
IMAGE_FEATURES = ""
IMAGE_ROOTFS_SIZE = "8192"
IMAGE_ROOTFS_EXTRA_SPACE = "0"
export IMAGE_BASENAME = "initramfs-uboot-image"
IMAGE_LINGUAS = ""

IMAGE_FSTYPES:forcevariable = "cpio.gz.u-boot"
IMAGE_FSTYPES:forcevariable:pinephone = "cpio.gz.u-boot"

# We don't need depmod data here
KERNELDEPMODDEPEND = ""
USE_DEPMOD = "0"

inherit core-image
