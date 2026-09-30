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
# NOTE: the old "restore-LuneOS-WiFi-power" patch is deliberately NOT applied and
# is no longer carried in-tree; the analysis it prompted is preserved below.
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
# Any re-introduction of that approach must also drive pwren and pwrkey, not just
# reset - otherwise the BES2600 never powers on.
#
# Camera bring-up (rear OV5648 + front GC02M2) runs through the libcamera soft-ISP
# path (VICAP/rkcif). The abandoned HW-ISP experiments and the CPU-OPP/i2c tuning
# spikes are preserved under linux-pinetab2/wip/ but are deliberately NOT built.
SRC_URI += " \
    file://0001-media-i2c-ov5648-add-the-controls-libcamera-requires.patch \
    file://0002-media-i2c-ov5648-do-not-refuse-analogue-gain-while-A.patch \
    file://0003-media-i2c-ov5648-keep-cached-control-values-while-po.patch \
    file://0004-media-i2c-ov5648-expose-the-orientation-and-rotation.patch \
    file://0005-media-i2c-ov5648-report-the-subsampled-modes-GBRG-Ba.patch \
    file://0006-media-i2c-ov5648-clamp-exposure-to-the-mode-s-frame-.patch \
    file://0007-media-i2c-add-a-driver-for-the-GalaxyCore-GC02M2.patch \
    file://0008-media-dw-mipi-csi2rx-always-report-progressive-frame.patch \
    file://0009-media-rkcif-honour-the-mbus_code-filter-when-enumera.patch \
    file://0010-media-rkcif-do-not-service-streams-while-suspended-o.patch \
    file://0011-phy-rockchip-inno-csidphy-support-the-rk3568-split-m.patch \
    file://0012-media-dw-mipi-csi2rx-allow-two-sensors-on-the-sink-p.patch \
    file://0013-phy-rockchip-inno-csidphy-pick-lane-pair-from-the-ac.patch \
    file://0014-arm64-dts-rockchip-rk3566-pinetab2-add-front-and-rea.patch \
    file://0015-arm64-dts-rk3566-pinetab2-wire-WiFi-to-the-LuneOS-be.patch \
    file://0016-arm64-dts-rk3566-pinetab2-adopt-the-DanctNIX-WiFi-po.patch \
    file://0017-arm64-dts-rk3566-pinetab2-force-the-OTG-port-to-peri.patch \
    file://0018-arm64-dts-rk3566-pinetab2-limit-the-OTG-port-to-full.patch \
    file://0019-arm64-dts-rockchip-pinetab2-reserve-memory-for-ramoo.patch \
    file://0024-Patch-linux-framebuffer-logo-for-LuneOS.patch \
    file://0025-arm64-dts-rockchip-pinetab2-give-Bluetooth-back-its-.patch \
    file://0026-Bluetooth-degrade-instead-of-failing-on-a-bogus-LE-b.patch \
    file://0027-arm64-dts-rk3566-pinetab2-let-the-WiFi-chip-wake-the.patch \
    file://defconfig \
    file://extra.cfg \
"

#    file://0020-regulator-fan53555-let-the-device-tree-select-forced.patch
#    file://0021-arm64-dts-rockchip-pinetab2-run-vdd_cpu-in-forced-PW.patch
#    file://0022-regulator-fan53555-cache-the-registers.patch
#    file://0023-arm64-dts-rockchip-pinetab2-keep-vdd_cpu-flat-across.patch
