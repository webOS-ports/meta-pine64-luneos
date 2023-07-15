# Copyright (c) 2023, Herman van Hazendonk
# Released under the MIT license (see COPYING.MIT for the terms)

SOC_FAMILY ?= "rk3566"

require conf/machine/include/soc-family.inc
require conf/machine/include/arm/armv8a/tune-cortexa72-cortexa53.inc
require conf/machine/include/rockchip-defaults.inc
require conf/machine/include/rockchip-wic.inc

KBUILD_DEFCONFIG ?= "defconfig"
KERNEL_CLASSES = "kernel-fitimage"
KERNEL_IMAGETYPE = "fitImage"

TFA_PLATFORM = "rk3566"
TFA_BUILD_TARGET = "bl31"

UBOOT_SUFFIX ?= "itb"
UBOOT_ENTRYPOINT ?= "0x06000000"