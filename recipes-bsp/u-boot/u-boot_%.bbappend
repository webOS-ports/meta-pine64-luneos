FILESEXTRAPATHS_prepend_pinephone := "${THISDIR}/files:"

SRC_URI_append_pinephone = " file://boot.txt"

DEPENDS_append_pinephone = " arm-trusted-firmware u-boot-tools-native"

EXTRA_DEP_pinephone = "arm-trusted-firmware:do_deploy"
EXTRA_DEP = ""

do_configure[depends] += "${EXTRA_DEP}"

do_configure_prepend_pinephone() {
    if [ ! -f ${B}/bl31.bin ]; then
        ln ${DEPLOY_DIR}/images/${MACHINE}/bl31-${MACHINE}.bin ${B}/bl31.bin
    fi

    mkimage -A arm -O linux -T script -C none -n "U-Boot boot script" \
        -d ${WORKDIR}/boot.txt ${WORKDIR}/boot.scr
}

FILES_${PN}_append_pinephone = " /boot/boot.scr"
