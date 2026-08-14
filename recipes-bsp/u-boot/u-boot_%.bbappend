FILESEXTRAPATHS:prepend:pinephone := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinephonepro := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinetab2 := "${THISDIR}/files:"

PV:pinephonepro = "2023.07"
PV:pinetab2 = "2024.01"

SRCREV:pinephonepro = "222aa75acee7f4140a2ca5c502e536419d4ff735"
# megi moved his repos to Codeberg: xff.cz/git/u-boot now 302s to codeberg.org/megi/u-boot, which
# git refuses to follow ("unable to update url base from redirection"), and megous.com -- the host
# the old clone URL actually pointed at -- no longer resolves at all. Same SRCREV, which is still
# reachable on the ppp-2023.07 branch there.
SRC_URI:pinephonepro = " \
    git://codeberg.org/megi/u-boot;protocol=https;branch=ppp-2023.07 \
"

SRCREV:pinetab2 = "583d37d192d92c5e92efa68b1d97f8ecdef71c47"
SRC_URI:pinetab2 = " \
    git://github.com/Kwiboo/u-boot-rockchip.git;protocol=https;branch=rk35xx-2024.01 \
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

# pinetab2 is pinned to Kwiboo's rk35xx-2024.01 fork for RK3566 support, and that
# tree's bundled pylibfdt bindings predate the SWIG 4.3 change that gave
# SWIG_Python_AppendOutput a third (is_void) argument, so do_compile fails with
#
#   libfdt_wrap.c: error: too few arguments to function 'SWIG_Python_AppendOutput';
#   expected 3, have 2
#
# against wrynose's swig 4.4.1. u-boot's Makefile skips building both dtc and
# pylibfdt when DTC is supplied ("If DTC is provided, it is assumed the pylibfdt
# is available too"), and OE already stages both natively, so point it at those
# instead of rebuilding them.
DEPENDS:append:pinetab2 = " dtc-native python3-pylibfdt-native"
EXTRA_OEMAKE:append:pinetab2 = " DTC=${STAGING_BINDIR_NATIVE}/dtc"
