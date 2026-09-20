SUMMARY = "brcmfmac module options for the PinePhone Pro"
DESCRIPTION = "Disables the brcmfmac firmware supplicant so wpa_supplicant \
performs the WPA2 four-way handshake in software. See the shipped \
modprobe.d fragment for the reasoning."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

COMPATIBLE_MACHINE = "pinephonepro"

SRC_URI = "file://brcmfmac.conf"
S = "${UNPACKDIR}"

do_install() {
    install -d ${D}${sysconfdir}/modprobe.d
    install -m 0644 ${UNPACKDIR}/brcmfmac.conf ${D}${sysconfdir}/modprobe.d/brcmfmac.conf
}

FILES:${PN} = "${sysconfdir}/modprobe.d/brcmfmac.conf"
