SUMMARY = "Enable bes2600 firmware-download debug logging"
DESCRIPTION = "Turns on the bes2600 driver's dev_dbg() output via dynamic debug \
so the firmware download stages are visible while the DPD failure is diagnosed."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

COMPATIBLE_MACHINE = "pinetab2"

SRC_URI = "file://bes2600-dbg.conf"
S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/modprobe.d
    install -m 0644 ${UNPACKDIR}/bes2600-dbg.conf ${D}${sysconfdir}/modprobe.d/bes2600-dbg.conf
}

FILES:${PN} = "${sysconfdir}/modprobe.d/bes2600-dbg.conf"
