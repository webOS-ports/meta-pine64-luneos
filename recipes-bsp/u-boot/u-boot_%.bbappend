FILESEXTRAPATHS:prepend:pinephone := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinephonepro := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinetab2 := "${THISDIR}/files:"

PV:pinephonepro ="2023.07"
PV:pinetab2 ="2023.07"

SRCREV:pinephonepro = "222aa75acee7f4140a2ca5c502e536419d4ff735"
SRC_URI:pinephonepro = " \
    git://xff.cz/git/u-boot;protocol=https;branch=ppp-2023.07 \
"

SRCREV:pinetab2 = "81dac0348fb1d1d8279634d1438bf2a88b1ca9b7"
SRC_URI:pinetab2 = " \
    git://github.com/Kwiboo/u-boot-rockchip.git;protocol=https;branch=rk3568-2023.07.02 \
    file://rk3568_bl31_v1.42.elf \
    file://rk3566_ddr_1056MHz_v1.18.bin \
"

SRC_URI:append:pinephone = " \
    file://boot.cmd    \
    file://0001-pinephone-Add-volume_key-environment-variable.patch \
"
UBOOT_MAKE_TARGET:pinephone = "pinephone_defconfig all"

DEPENDS:append:pinephone = " trusted-firmware-a u-boot-tools-native python3-setuptools-native"
DEPENDS:append:pinephonepro = " trusted-firmware-a u-boot-tools-native python3-setuptools-native python3-pyelftools-native"
DEPENDS:append:pinetab2 = " u-boot-tools-native python3-setuptools-native python3-pyelftools-native"
DEPENDS:append:pinephone = " crust-firmware"

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

EXTRA_OEMAKE:append:pinetab2 = " EXTRAVERSION=1 BL31=${WORKDIR}/rk3568_bl31_v1.42.elf ROCKCHIP_TPL=${WORKDIR}/rk3566_ddr_1056MHz_v1.18.bin"


FILES:${PN}:append:pinephone = " /boot/boot.scr"
