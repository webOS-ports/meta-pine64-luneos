FILESEXTRAPATHS:prepend:pinephone := "${THISDIR}/files:"

LIC_FILES_CHKSUM:pinephone = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

SRCREV:pinephone = "c784be467aa7d111f149c2a54557e8828bc5775a"
SRC_URI:pinephone = " \
    git://gitlab.com/pine64-org/u-boot.git;protocol=https;branch=crust \
    file://0001-sunxi-h3-Fix-PLL1-setup-to-never-use-dividers.patch \
    file://boot.txt \
"

DEPENDS:append:pinephone = " arm-trusted-firmware crust-firmware u-boot-tools-native python3-setuptools-native"

EXTRA_DEP:pinephone = "arm-trusted-firmware:do_deploy crust-firmware:do_deploy"
EXTRA_DEP = ""

do_configure[depends] += "${EXTRA_DEP}"

do_configure:prepend_pinephone() {
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
