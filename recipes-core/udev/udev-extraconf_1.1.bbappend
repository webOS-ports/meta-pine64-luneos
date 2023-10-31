FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH:pinephone = "${MACHINE_ARCH}"
PACKAGE_ARCH:pinephonepro = "${MACHINE_ARCH}"

RDEPENDS:${PN}:append:pinephone = " eg25-manager"
RDEPENDS:${PN}:append:pinephonepro = " eg25-manager"

SRC_URI:append:pinephone = " \
                            file://pinephone/70-wifi-pm.rules \
                            file://pinephone/90-modem-eg25.rules \
"

SRC_URI:append:pinephonepro = " \
                            file://pinephonepro/90-modem-eg25.rules \
"

do_install:append:pinephone() {
    # setup wifi power saving mode when charger is (dis)connected
    install -m 0644 ${WORKDIR}/pinephone/70-wifi-pm.rules ${D}${sysconfdir}/udev/rules.d/70-wifi-pm.rules
    # configure EG25 modem nodes
    install -m 0644 ${WORKDIR}/pinephone/90-modem-eg25.rules ${D}${sysconfdir}/udev/rules.d/90-modem-eg25.rules
}

do_install:append:pinephonepro() {
    # configure EG25 modem nodes
    install -m 0644 ${WORKDIR}/pinephonepro/90-modem-eg25.rules ${D}${sysconfdir}/udev/rules.d/90-modem-eg25.rules
}
