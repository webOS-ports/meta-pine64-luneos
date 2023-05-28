FILESEXTRAPATHS:prepend:pinephone := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinephonepro := "${THISDIR}/files:"

LIC_FILES_CHKSUM:pinephonepro = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

PV:pinephonepro ="2021.10"

SRCREV:pinephonepro = "eaf7654d278c8c08653690e0e5a4c24ca0e705a9"
SRC_URI:pinephonepro = " \
    git://github.com/herrie82/u-boot.git;protocol=https;branch=herrie/ppp \
"

SRC_URI:append:pinephone = " \
    file://boot.cmd    \
    file://0001-pinephone-Add-volume_key-environment-variable.patch \
"
UBOOT_MAKE_TARGET:pinephone = "pinephone_defconfig all"

DEPENDS:append:pinephone = " trusted-firmware-a u-boot-tools-native python3-setuptools-native"
DEPENDS:append:pinephonepro = " trusted-firmware-a u-boot-tools-native python3-setuptools-native"
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

FILES:${PN}:append:pinephone = " /boot/boot.scr"
