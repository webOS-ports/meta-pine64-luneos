FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH:pinephone = "${MACHINE_ARCH}"

RDEPENDS:${PN}:append:pinephone = " eg25-manager"

SRC_URI:append:pinephone = " file://10-pinephone-proximity.rules \
                             file://90-modem-eg25.rules \
                             file://70-wifi-pm.rules \
"

do_install:append_pinephone() {
    # to help configure proximity sensor
    install -m 0644 ${WORKDIR}/10-pinephone-proximity.rules ${D}${sysconfdir}/udev/rules.d/10-pinephone-proximity.rules
    # configure EG25 modem nodes
    install -m 0644 ${WORKDIR}/90-modem-eg25.rules ${D}${sysconfdir}/udev/rules.d/90-modem-eg25.rules
    # setup wifi power saving mode when charger is (dis)connected
    install -m 0644 ${WORKDIR}/70-wifi-pm.rules ${D}${sysconfdir}/udev/rules.d/70-wifi-pm.rules
}
