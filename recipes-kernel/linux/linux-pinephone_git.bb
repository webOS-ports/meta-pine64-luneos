DESCRIPTION = "PinePhone Linux Kernel (megi)"

require linux-megi.inc

COMPATIBLE_MACHINE = "pinephone"
LINUX_VERSION_EXTENSION = "-pinephone"

SRC_URI += " \
    file://0001-dts-pinephone-drop-modem-power-node.patch \
    file://0002-dts-pinephone-jack-detection.patch \
    file://0003-dts-pinephone-keep-the-modem-rail-powered.patch \
    file://0004-media-gc2145-do-not-fail-volatile-reads-while-suspended.patch \
    file://0005-media-gc2145-do-not-log-the-expected-probe-time-i2c-failure.patch \
    file://defconfig \
    file://extra.cfg \
"
