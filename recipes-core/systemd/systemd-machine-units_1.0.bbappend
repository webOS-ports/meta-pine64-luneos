FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_pinephone = " \
    file://touchscreen-initialize.service \
    file://touchscreen-initialize.sh \
"

do_install_append_pinephone() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/touchscreen-initialize.service ${D}${systemd_unitdir}/system

    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/touchscreen-initialize.sh ${D}${bindir}
}

SYSTEMD_SERVICE_${PN}_pinephone = " \
    touchscreen-initialize.service \
"
