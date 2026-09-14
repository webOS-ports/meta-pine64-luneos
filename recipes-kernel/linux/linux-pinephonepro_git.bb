DESCRIPTION = "PinePhonePro Linux Kernel (megi)"

require linux-megi.inc

COMPATIBLE_MACHINE = "pinephonepro"
LINUX_VERSION_EXTENSION = "-pinephonepro"

SRC_URI += " \
    file://0002-dts-pinephone-pro-remove-modem-node.patch \
    file://0003-dts-pinephone-pro-poll-volume-keys-faster.patch \
    file://0004-dts-pinephone-pro-keep-the-4G-rails-powered.patch \
    file://0005-power-rk818_battery-stop-treating-every-boot-as-firs.patch \
    file://defconfig \
    file://extra.cfg \
"

# rk3399.inc (pulled in via rock-pi-4.inc) appends a Rockchip kmeta feature to
# KERNEL_FEATURES for every rk3399 machine:
#     KERNEL_FEATURES:append:rk3399 = " bsp/rockchip/remove-non-rockchip-arch-arm64.scc"
# That .scc lives in meta-rockchip's rockchip-kmeta, which only its linux-yocto
# bbappend puts on SRC_URI, so for this recipe it is a dangling feature and
# do_kernel_metadata fails with
#     ERROR: Feature 'bsp/rockchip/remove-non-rockchip-arch-arm64.scc' not found
# It is not wanted here either: this builds megi's tree against the complete
# defconfig shipped above, not a kernel-cache assembled config.
KERNEL_FEATURES:remove = "bsp/rockchip/remove-non-rockchip-arch-arm64.scc"
