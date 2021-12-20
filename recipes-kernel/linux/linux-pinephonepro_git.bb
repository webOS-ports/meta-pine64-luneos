DESCRIPTION = "PinePhonePro Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

LINUX_VERSION ?= "5.16"
LINUX_VERSION_EXTENSION = "-pinephonepro"

PV = "${LINUX_VERSION}-git${SRCPV}"

KERNEL_VERSION_SANITY_SKIP="1"

#LINUX_BRANCH = "orange-pi-${LINUX_VERSION}"
#LINUX_KMETA_BRANCH = "yocto-${LINUX_VERSION}"
LINUX_KMETA_BRANCH = "yocto-dev"
# megous SRCREV_machine = "c6fda0a09217b8c183cf1ef4782746fcfcf055f1"
SRCREV_machine = "a7904a538933c525096ca2ccde1e60d0ee62c08e" 
SRCREV_meta = "94bfc55e50d9962af2da6d3bc5ee7c205d0df323"
KMETA = "kernel-meta"
SRC_URI = " \
    git://github.com/torvalds/linux.git;branch=master;protocol=https;name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=master;destsuffix=${KMETA};name=meta \
    file://defconfig \
    file://0001-base-property-Swap-order-of-search-for-connection-to.patch \
    file://0002-sdhci-arasan-Add-runtime-PM-support.patch \
    file://0003-clk-rk3399-Export-SCLK_CIF_OUT_SRC-to-device-tree.patch \
    file://0004-media-rockchip-rga-Fix-probe-bugs.patch \
    file://0005-drm-dw-mipi-dsi-rockchip-Ensure-that-lane-is-properl.patch \
    file://0006-drm-rockchip-dw-mipi-dsi-Fix-missing-clk_disable_unp.patch \
    file://0007-drm-bridge-dw-mipi-dsi-Fix-enable-disable-of-dsi-con.patch \
    file://0008-drm-dw-mipi-dsi-rockchip-Never-allow-lane-bandwidth-.patch \
    file://0009-drm-rockchip-cdn-dp-Disable-CDN-DP-on-disconnect.patch \
    file://0010-video-fbdev-Add-events-for-early-fb-event-support.patch \
    file://0011-power-rk818-Configure-rk808-clkout2-function.patch \
    file://0012-power-rk818-battery-Add-battery-driver-for-RK818.patch \
    file://0013-power-supply-rk818-battery-Use-a-more-propper-compat.patch \
    file://0014-power-supply-core-Don-t-ignore-max_current-of-0-when.patch \
    file://0015-power-supply-rk818-charger-Implement-charger-driver-.patch \
    file://0016-usb-typec-fusb302-Set-the-current-before-enabling-pu.patch \
    file://0017-usb-typec-fusb302-Extend-debugging-interface-with-dr.patch \
    file://0018-usb-typec-fusb302-Retry-reading-of-CC-pins-status-if.patch \
    file://0019-usb-typec-fusb302-More-useful-of-logging-status-on-i.patch \
    file://0020-usb-typec-fusb302-Update-VBUS-state-even-if-VBUS-int.patch \
    file://0021-usb-typec-fusb302-Make-tcpm-fusb302-logs-less-pollut.patch \
    file://0022-usb-typec-fusb302-Add-OF-extcon-support.patch \
    file://0023-usb-typec-fusb302-Fix-register-definitions.patch \
    file://0024-usb-typec-fusb302-Clear-interrupts-before-we-start-t.patch \
    file://0025-usb-typec-typec-extcon-Add-typec-extcon-bridge-drive.patch \
    file://0026-phy-rockchip-typec-Make-sure-the-plug-orientation-is.patch \
    file://0027-media-i2c-imx258-Add-support-for-powerdown-gpio.patch \
    file://0028-media-i2c-imx258-Don-t-be-too-strict-about-clock-rat.patch \
    file://0029-media-i2c-imx258-Add-support-for-reset-gpio.patch \
    file://0030-media-i2c-imx258-Add-support-for-power-supplies.patch \
    file://0031-media-ov5640-Add-more-framerates-to-the-driver-some-.patch \
    file://0032-media-ov5640-Experiment-Try-to-disable-denoising-sha.patch \
    file://0033-media-ov5640-Sleep-after-poweroff-to-ensure-next-pow.patch \
    file://0034-media-ov5640-Don-t-powerup-the-sensor-during-driver-.patch \
    file://0035-media-ov5640-Implement-autofocus.patch \
    file://0036-media-ov5640-set-default-ae-target-lower.patch \
    file://0037-drm-panel-hx8394-Add-driver-for-HX8394-based-HannSta.patch \
    file://0038-drm-panel-hx8394-Improve-the-panel-driver-make-it-wo.patch \
    file://0039-drm-panel-hx8394-Fix-mode-clock-for-the-pinephone-pr.patch \
    file://0040-input-goodix-Add-option-to-power-off-the-controller-.patch \
    file://0041-input-goodix-Don-t-disable-regulators-during-suspend.patch \
    file://0042-input-touchscreen-goodix-Respect-IRQ-flags-from-DT-w.patch \
    file://0043-input-touchscreen-goodix-Add-support-for-GT1158.patch \
    file://0044-arm64-dts-rk3399-pinephone-pro-Add-support-for-Pinep.patch \
    file://0045-arm64-dts-rk3399-pinephone-pro-Fixup-DT-validation-i.patch \
    file://0046-arm64-dts-rk3399-pinephone-pro-Make-charging-and-per.patch \
    file://0047-arm64-dts-rk3399-pinephone-pro-Fix-goodix-toucscreen.patch \
    file://0048-arm64-dts-rk3399-pinephone-pro-Correct-the-pmu1830-i.patch \
    file://0049-arm64-dts-rk3399-pinephone-pro-Power-off-goodix-touc.patch \
    file://0050-arm64-dts-rk3399-pinephone-pro-Add-support-for-both-.patch \
    file://0051-arm64-dts-rk3399-pinephone-pro-Fix-SD-card-power-sup.patch \
    file://0052-arm64-dts-rk3399-pinephone-pro-Correct-the-battery-s.patch \
    file://0053-arm64-dts-rk3399-pinephone-pro-Cleanup-some-USB-node.patch \
    file://0054-arm64-dts-rk3399-pinephone-pro-Fix-PDOs-to-be-more-r.patch \
    file://0055-arm64-dts-rk3399-pinephone-pro-Add-chassis-type-hand.patch \
    file://0056-arm64-dts-rk3399-pinephone-pro-Add-mmc-aliases-to-ge.patch \
    file://0057-arm64-dts-rk3399-pinephone-pro-Use-a-new-rk818-batte.patch \
    file://0058-arm64-dts-rk3399-pinephone-pro-Full-support-for-Type.patch \
    file://0059-arm64-dts-rk3399-pinephone-pro-Use-DCLK_VOP-_FRAC-to.patch \
    file://0060-arm64-dts-rk3399-pinephone-pro-Add-support-for-power.patch \
    file://0061-arm64-dts-rk3399-pinephone-pro-Add-audio-support.patch \
    file://0062-arm64-dts-rk3399-pinephone-pro-Add-flash-and-fix-led.patch \
    file://0063-arm64-dts-rk3399-pinephone-pro-add-modem-RI-pin.patch \
    file://0064-arm64-dts-rk3399-pinephone-pro-improve-sound-device.patch \
    file://d1d849cae12db71aa81ceedaedc1b17a34790367.patch \
    file://2423aac2d6f5db55da99e11fd799ee66fe6f54c6.patch \
    file://0001-revert-garbage-collect-fbdev-scrolling-acceleration.patch \
    file://0002-revert-fbcon-remove-now-unusued-softback_lines-cursor-argument.patch \
    file://0003-revert-fbcon-remove-no-op-fbcon_set_origin.patch \
    file://0004-revert-fbcon-remove-soft-scrollback-code.patch \
    file://0001-bootsplash.patch \
    file://0002-bootsplash.patch \
    file://0003-bootsplash.patch \
    file://0004-bootsplash.patch \
    file://0005-bootsplash.patch \
    file://0006-bootsplash.patch \
    file://0007-bootsplash.patch \
    file://0008-bootsplash.patch \
    file://0009-bootsplash.patch \
    file://0010-bootsplash.patch \
    file://0011-bootsplash.patch \
    file://0012-bootsplash.patch \
"

#    git://github.com/megous/linux.git;protocol=https;branch=${LINUX_BRANCH};name=machine 


KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation, libyaml is checked
# via pkgconfig, so must always be present, but we can wrap the others to make them
# conditional
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"

COMPATIBLE_MACHINE = "pinephonepro"
