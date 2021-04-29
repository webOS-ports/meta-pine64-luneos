FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH_pinephone = "${MACHINE_ARCH}"

SRC_URI_append_pinephone = " file://10-pinephone-proximity.rules \
                             file://70-modem-eg25.rules \
                             file://70-wifi-pm.rules \
"

do_install_append_pinephone() {
    # to help configure proximity sensor
    install -m 0644 ${WORKDIR}/10-pinephone-proximity.rules ${D}${sysconfdir}/udev/rules.d/10-pinephone-proximity.rules
    # configure EG25 modem nodes
    install -m 0644 ${WORKDIR}/70-modem-eg25.rules ${D}${sysconfdir}/udev/rules.d/70-modem-eg25.rules
    # setup wifi power saving mode when charger is (dis)connected
    install -m 0644 ${WORKDIR}/70-wifi-pm.rules ${D}${sysconfdir}/udev/rules.d/70-wifi-pm.rules
}
