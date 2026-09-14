DESCRIPTION = "PinePhone Linux Kernel (megi)"

require linux-megi.inc

COMPATIBLE_MACHINE = "pinephone"
LINUX_VERSION_EXTENSION = "-pinephone"

SRC_URI += " \
    file://0001-dts-pinephone-drop-modem-power-node.patch \
    file://0002-dts-pinephone-jack-detection.patch \
    file://defconfig \
    file://extra.cfg \
"
