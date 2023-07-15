FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH:pinephone = "${MACHINE_ARCH}"
PACKAGE_ARCH:pinephonepro = "${MACHINE_ARCH}"
PACKAGE_ARCH:pinetab2 = "${MACHINE_ARCH}"

RDEPENDS:${PN}:append:pinephone = " eg25-manager"
RDEPENDS:${PN}:append:pinephonepro = " eg25-manager"

SRC_URI:append:pinephone = "file://pinephone/10-pinephone-proximity.rules \
                            file://pinephone/70-wifi-pm.rules \
                            file://pinephone/90-modem-eg25.rules \
"

SRC_URI:append:pinephonepro = "file://pinephonepro/10-pinephonepro-proximity.rules \
                               file://pinephonepro/90-modem-eg25.rules \
"

SRC_URI:append:pinetab2 = "file://pinetab2/10-pinetab2-proximity.rules \
"


do_install:append:pinephone() {
    # to help configure proximity sensor
    install -m 0644 ${WORKDIR}/pinephone/10-pinephone-proximity.rules ${D}${sysconfdir}/udev/rules.d/10-pinephone-proximity.rules
    # setup wifi power saving mode when charger is (dis)connected
    install -m 0644 ${WORKDIR}/pinephone/70-wifi-pm.rules ${D}${sysconfdir}/udev/rules.d/70-wifi-pm.rules
    # configure EG25 modem nodes
    install -m 0644 ${WORKDIR}/pinephone/90-modem-eg25.rules ${D}${sysconfdir}/udev/rules.d/90-modem-eg25.rules
}

do_install:append:pinephonepro() {
    # to help configure proximity sensor
    install -m 0644 ${WORKDIR}/pinephonepro/10-pinephonepro-proximity.rules ${D}${sysconfdir}/udev/rules.d/10-pinephonepro-proximity.rules
    # configure EG25 modem nodes
    install -m 0644 ${WORKDIR}/pinephonepro/90-modem-eg25.rules ${D}${sysconfdir}/udev/rules.d/90-modem-eg25.rules
}

do_install:append:pinetab2() {
    # to help configure proximity sensor
    install -m 0644 ${WORKDIR}/pinetab2/10-pinetab2-proximity.rules ${D}${sysconfdir}/udev/rules.d/10-pinetab2-proximity.rules
}
