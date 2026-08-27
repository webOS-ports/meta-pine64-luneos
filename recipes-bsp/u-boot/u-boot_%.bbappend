FILESEXTRAPATHS:prepend:pinephone := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinephonepro := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinetab2 := "${THISDIR}/files:"

PV:pinephonepro = "2023.07"
PV:pinetab2 = "2024.01"

# meta-rockchip points BL31 at meta-arm's trusted-firmware-a deploy layout
# (trusted-firmware-a/bl31.elf), but pinephonepro builds this layer's TF-A 2.6
# recipe instead, which deploys bl31-rk3399.elf at the top of DEPLOY_DIR_IMAGE.
BL31:pinephonepro = "${DEPLOY_DIR_IMAGE}/bl31-rk3399.elf"

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

# pinetab2 (Kwiboo's rk35xx-2024.01 fork) and pinephonepro (megi's ppp-2023.07
# branch) are pinned to forks whose bundled pylibfdt bindings predate the SWIG
# 4.3 change that gave SWIG_Python_AppendOutput a third (is_void) argument, so
# do_compile fails against wrynose's swig 4.4.1. The boards need their own
# pylibfdt: CONFIG_BINMAN and CONFIG_PYLIBFDT are both set, and wrynose's
# dtc-native is built with -Dpython=disabled, so nothing in the tree stages one
# to borrow.
SRC_URI:append:pinetab2 = " \
    file://0001-pylibfdt-build-with-SWIG-4.3-and-newer.patch \
    file://0002-binman-use-importlib.resources-instead-of-pkg_resourc.patch \
"
SRC_URI:append:pinephonepro = " \
    file://0001-pylibfdt-build-with-SWIG-4.3-and-newer.patch \
    file://0002-binman-use-importlib.resources-instead-of-pkg_resourc.patch \
    file://0002-cmd-tmenu-include-cli.h-for-cli_simple_run_command.patch \
"
