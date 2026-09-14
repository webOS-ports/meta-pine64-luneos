DESCRIPTION = "PineTab2 Linux Kernel (megi)"

require linux-megi.inc

COMPATIBLE_MACHINE = "pinetab2"
LINUX_VERSION_EXTENSION = "-pinetab2"

# 0001 (PineTab2 device trees) and 0003 (BOE TH101MB31IG002-28A panel driver) were
# dropped: both are upstream as of 6.9 and are in megi's tree already.
#
# The BES2600 node is ours, not megi's: his orange-pi-7.2 comments out the vendor
# bes2600 driver in favour of the WIP cw1200-based bring-up, whose cw1200_sdio.c
# claims the same SDIO device (0x2002) as our out-of-tree bes2600-module. See the
# patch header and CONFIG_CW1200 in the defconfig.
SRC_URI += " \
    file://0005-arm64-dts-rockchip-pinetab2-Use-the-LuneOS-BES2600-dr.patch \
    file://0007-Patch-linux-framebuffer-logo-for-LuneOS.patch \
    file://defconfig \
    file://extra.cfg \
"
