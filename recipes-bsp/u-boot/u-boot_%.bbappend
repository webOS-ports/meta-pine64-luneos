FILESEXTRAPATHS:prepend:pinephone := "${THISDIR}/files:"
FILESEXTRAPATHS:prepend:pinephonepro := "${THISDIR}/files:"

LIC_FILES_CHKSUM:pinephone = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"
LIC_FILES_CHKSUM:pinephonepro = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

PV:pinephonepro ="2021.10"

SRCREV:pinephonepro = "eaf7654d278c8c08653690e0e5a4c24ca0e705a9"
SRC_URI:pinephonepro = " \
    git://github.com/herrie82/u-boot.git;protocol=https;branch=herrie/ppp \
"

SRCREV:pinephone = "d80bb749fab53da72c4a0e09b8c2d2aaa3103c91"
SRC_URI:pinephone = " \
    git://github.com/trini/u-boot.git;protocol=https;branch=master \
    file://0010-sunxi-DT-H6-update-device-tree-files.patch \
    file://0020-sunxi-DT-H6-Add-USB3-to-Pine-H64-DTS.patch \
    file://0023-pinephone-Add-volume_key-environment-variable.patch \
    file://0024-Enable-led-on-boot-to-notify-user-of-boot-status.patch \
    file://0027-mmc-sunxi-Add-support-for-DMA-transfers.patch \
    file://0028-mmc-sunxi-DDR-DMA-support-for-SPL.patch \
    file://0029-spl-ARM-Enable-CPU-caches.patch \
    file://0030-common-expose-DRAM-clock-speed.patch \
    file://0031-Improve-Allwinner-A64-timer-workaround.patch \
    file://boot.txt \
"

DEPENDS:append = " trusted-firmware-a u-boot-tools-native python3-setuptools-native"
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
    
    mkimage -A arm -O linux -T script -C none -n "U-Boot boot script" \
        -d ${WORKDIR}/boot.txt ${WORKDIR}/boot.scr
}

FILES:${PN}:append:pinephone = " /boot/boot.scr"
