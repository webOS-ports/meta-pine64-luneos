FILESEXTRAPATHS:prepend:pinephone := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinephonepro := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinetab2 := "${THISDIR}/files:"

# megi's ppp-* branches are frozen at 2023; the maintained line was renamed to
# megi-*, and megi-2026.04 (2026-04-23) carries pinephone-pro-rk3399_defconfig,
# pinetab2-rk3566_defconfig and cmd/tmenu.c, with panel video on both boards.
#
# The PineTab2 uses it. The PinePhone Pro does NOT, for now: moving it there
# produced a completely dead display (see the note above SRCREV:pinephonepro),
# so it stays on the revision that was working. Retry once the PPP is otherwise
# healthy, and change u-boot on its own rather than alongside a kernel jump.
PV:pinephonepro = "2023.07"
PV:pinetab2 = "2026.04"

# meta-rockchip points BL31 at meta-arm's trusted-firmware-a deploy layout
# (trusted-firmware-a/bl31.elf), but pinephonepro builds this layer's TF-A 2.6
# recipe instead, which deploys bl31-rk3399.elf at the top of DEPLOY_DIR_IMAGE.
BL31:pinephonepro = "${DEPLOY_DIR_IMAGE}/bl31-rk3399.elf"

# megi moved his repos to Codeberg: xff.cz/git/u-boot now 302s to codeberg.org/megi/u-boot,
# which git refuses to follow ("unable to update url base from redirection"), and megous.com --
# the host the old clone URL pointed at -- no longer resolves at all.
# REVERTED to the 2023.07 pin that was running before this migration.
#
# megi-2026.04 was flashed together with the 6.5 -> 7.2 kernel jump, and the
# PinePhone Pro then showed no display at all - not even tmenu, which that
# branch's defconfig draws on the panel (CONFIG_CMD_TMENU=y plus
# VIDEO_LCD_HIMAX_HX8394 at 720x1440). No tmenu means u-boot itself is not
# reaching the panel, so the two changes are separated again: keep the kernel at
# 7.2 and put u-boot back to the exact revision that worked.
#
# Worth noting megi-2026.04's PPP defconfig also sets ROCKCHIP_SPI_IMAGE and
# ENV_IS_IN_SPI_FLASH, a different boot/env layout from this one - a likely
# culprit and the thing to check before trying that branch again.
SRCREV:pinephonepro = "222aa75acee7f4140a2ca5c502e536419d4ff735"
SRC_URI:pinephonepro = " \
    git://codeberg.org/megi/u-boot;protocol=https;branch=ppp-2023.07 \
"

# Kwiboo's rk35xx-2024.01 was the pre-mainline staging ground for rk3566/rk3568;
# that work has since landed upstream and megi's branch carries it plus the
# PineTab2 panel/display config, so the fork is no longer needed.
SRCREV:pinetab2 = "5277f43b48bfd260e49199f5ccad1b5bef50e731"
SRC_URI:pinetab2 = " \
    git://codeberg.org/megi/u-boot;protocol=https;branch=megi-2026.04 \
    file://rk3568_bl31_v1.42.elf \
    file://rk3566_ddr_1056MHz_v1.18.bin \
"

SRC_URI:append:pinephone = " \
    file://boot.cmd    \
    file://0001-pinephone-Add-volume_key-environment-variable.patch \
"
UBOOT_MAKE_TARGET:pinephone = "pinephone_defconfig all"
EXTRA_OEMAKE:append:pinetab2 = " EXTRAVERSION=1 BL31=${UNPACKDIR}/rk3568_bl31_v1.42.elf ROCKCHIP_TPL=${UNPACKDIR}/rk3566_ddr_1056MHz_v1.18.bin"

DEPENDS:append:pinephone = " trusted-firmware-a u-boot-tools-native python3-setuptools-native"
DEPENDS:append:pinephonepro = " trusted-firmware-a u-boot-tools-native python3-setuptools-native python3-pyelftools-native"
DEPENDS:append:pinetab2 = " u-boot-tools-native python3-setuptools-native python3-pyelftools-native"
DEPENDS:append:pinephone = " crust-firmware"

TFA_DEPENDS:pinetab2 = ""

EXTRA_DEP:pinephone = "trusted-firmware-a:do_deploy crust-firmware:do_deploy"
EXTRA_DEP:pinephonepro = "trusted-firmware-a:do_deploy"
EXTRA_DEP = ""

do_configure[depends] += "${EXTRA_DEP}"

do_configure:prepend:pinephone() {
    # Insert the ATF binary
    if [ ! -f ${B}/bl31.bin ]; then
        ln ${DEPLOY_DIR}/images/${MACHINE}/bl31-${MACHINE}.bin ${B}/bl31.bin
    fi

    # Insert the Crust binary
    if [ ! -f ${B}/scp.bin ]; then
        ln ${DEPLOY_DIR}/images/${MACHINE}/scp-${MACHINE}.bin ${B}/scp.bin
    fi
}

FILES:${PN}:append:pinephone = " /boot/boot.scr"

# megi's tmenu is the boot menu on both the PinePhone Pro and the PineTab2
# (CONFIG_CMD_TMENU=y in both defconfigs, plus CMD_TMENU_ROTATED on the tablet).
# cmd/tmenu.c calls cli_simple_run_command() but only includes cli_hush.h, and
# GCC 15 makes that implicit declaration an error. Still unfixed on megi-2026.04.
#
# The pylibfdt and binman workarounds are needed only by the older forks:
# megi-2026.04 no longer calls SWIG_Python_AppendOutput in libfdt.i_shipped at
# all, and its tools/binman/control.py already uses importlib.resources. So the
# PineTab2 (2026.04) does not want them, while the PinePhone Pro (2023.07) still
# does - hence the two different append lists below.
SRC_URI:append:pinephonepro = " \
    file://0001-pylibfdt-build-with-SWIG-4.3-and-newer.patch \
    file://0002-binman-use-importlib.resources-instead-of-pkg_resourc.patch \
    file://0002-cmd-tmenu-include-cli.h-for-cli_simple_run_command.patch \
"
SRC_URI:append:pinetab2 = " \
    file://0002-cmd-tmenu-include-cli.h-for-cli_simple_run_command.patch \
"
