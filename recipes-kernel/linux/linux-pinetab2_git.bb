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
# NOTE: 0006-...-restore-LuneOS-WiFi-power.patch is deliberately NOT applied.
#
# It swapped megi's mmc-pwrseq-bes for mmc-pwrseq-simple, but pwrseq-simple can
# only drive reset-gpios. That silently dropped the two lines that actually
# power the BES2600:
#
#   pwrkey-gpios = <&gpio3 RK_PD3>     (dropped)
#   pwren-gpios  = <&gpio0 RK_PA0>     (dropped)
#   reset-gpios  = <&gpio3 RK_PD2>     (kept)
#
# and the replacement vcc_wl node is a regulator-fixed with no gpio property, so
# it does not drive RK_PA0 either - it just references the pinctrl. The chip is
# therefore never powered and never appears on the SDIO bus at all:
#
#   mmc2: Failed to initialize a non-removable card
#   (NULL device *): pdata->inited = 0, platform data must be inited at this point
#   modprobe: ERROR: could not insert 'bes2600': Operation canceled
#
# With pwrseq-bes the chip does enumerate and we get as far as the DPD
# calibration failing ("wait dpd data ready failed:-110"), which is a much
# later stage. Keeping pwrseq-bes also means the bes2600_factory.txt fixes
# (correct /lib/firmware path, added select_efuse field) finally get exercised -
# they landed in the same build as 0006 and so have never actually been tested.
#
# The patch file is kept in-tree for the analysis it carries; re-apply only if
# it also drives pwren and pwrkey.
SRC_URI += " \
    file://0005-arm64-dts-rockchip-pinetab2-Use-the-LuneOS-BES2600-dr.patch \
    file://0006-arm64-dts-rockchip-pinetab2-use-DanctNIX-wifi-power.patch \
    file://0009-arm64-dts-rockchip-pinetab2-let-the-phy-drive-otg.patch \
    file://0010-arm64-dts-rockchip-pinetab2-limit-otg-to-full-speed.patch \
    file://0011-arm64-dts-rockchip-pinetab2-connect-the-rear-camera.patch \
    file://0012-media-i2c-ov5648-add-the-controls-libcamera-requires.patch \
    file://0013-arm64-dts-rockchip-pinetab2-mux-the-camera-master-clock-and-fix-its-gpios.patch \
    file://0014-arm64-dts-rockchip-pinetab2-keep-PD_VI-on-while-the-camera-is-powered.patch \
    file://0015-media-i2c-ov5648-do-not-refuse-analogue-gain-while-agc-reads-enabled.patch \
    file://0016-media-dw-mipi-csi2rx-always-report-progressive-frames.patch \
    file://0017-arm64-dts-rockchip-pinetab2-let-the-camera-use-its-internal-avdd-regulator.patch \
    file://0018-media-rkcif-honour-the-mbus_code-filter-when-enumerating-formats.patch \
    file://0019-media-i2c-ov5648-keep-cached-control-values-while-powered-down.patch \
    file://0020-media-i2c-add-a-driver-for-the-GalaxyCore-GC02M2.patch \
    file://0021-phy-rockchip-inno-csidphy-support-the-rk3568-split-mode-lane-pair.patch \
    file://0022-arm64-dts-rockchip-pinetab2-describe-the-front-camera.patch \
    file://0023-media-i2c-ov5648-expose-the-orientation-and-rotation-properties.patch \
    file://0035-media-rkcif-do-not-service-streams-while-suspended-or-stopped.patch \
    file://0036-media-rkisp1-add-rk3566-rk3568-support.patch \
    file://0037-phy-rockchip-inno-csidphy-route-a-split-lane-pair-to-the-ISP.patch \
    file://0038-arm64-dts-rockchip-pinetab2-bring-the-rear-camera-up-through-the-ISP.patch \
    file://0007-Patch-linux-framebuffer-logo-for-LuneOS.patch \
    file://defconfig \
    file://extra.cfg \
"
