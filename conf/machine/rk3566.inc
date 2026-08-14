# Copyright (c) 2023, Herman van Hazendonk
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "rk3566"

require conf/machine/include/soc-family.inc
require conf/machine/include/arm/armv8a/tune-cortexa72-cortexa53.inc
require conf/machine/include/rockchip-defaults.inc
require conf/machine/include/rockchip-wic.inc

KBUILD_DEFCONFIG ?= "defconfig"

# fitImage is set up by meta-rockchip's rockchip-fitimage.inc, which
# rockchip-defaults.inc requires above because RK_KERNEL_FITIMAGE defaults to
# True. Do not set KERNEL_CLASSES/KERNEL_IMAGETYPE here: oe-core dropped
# kernel-fitimage.bbclass and moved FIT generation into the separate
# linux-yocto-fitimage recipe, so the kernel itself now builds a plain Image and
# hard assignments here would both wipe kernel-fit-extra-artifacts and ask for a
# class that no longer exists ("Could not inherit file
# classes/kernel-fitimage.bbclass" at parse time).

TFA_PLATFORM = "rk3566"
TFA_BUILD_TARGET = "bl31"

UBOOT_SUFFIX ?= "itb"
UBOOT_ENTRYPOINT ?= "0x06000000"