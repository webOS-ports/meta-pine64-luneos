FILESEXTRAPATHS_prepend_pinephone := "${THISDIR}/files:"

LIC_FILES_CHKSUM_pinephone = "file://Licenses/README;md5=5a7450c57ffe5ae63fd732446b988025"

SRCREV_pinephone = "495f85a398272e6d8ea8142790158afa1bb29c77"
SRC_URI_pinephone = " \
    git://gitlab.com/pine64-org/u-boot.git;protocol=https;branch=crust \
    file://boot.txt \
"

DEPENDS_append_pinephone = " arm-trusted-firmware crust-firmware u-boot-tools-native"

EXTRA_DEP_pinephone = "arm-trusted-firmware:do_deploy crust-firmware:do_deploy"
EXTRA_DEP = ""

do_configure[depends] += "${EXTRA_DEP}"

do_configure_prepend_pinephone() {
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

FILES_${PN}_append_pinephone = " /boot/boot.scr"
