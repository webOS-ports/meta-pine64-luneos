FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:pinetab2 = " \
    file://wifi-module-load.service \
"

do_install:append:pinetab2() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/wifi-module-load.service ${D}${systemd_unitdir}/system
}

SYSTEMD_SERVICE:${PN}:pinetab2 = " \
    wifi-module-load.service \
"
