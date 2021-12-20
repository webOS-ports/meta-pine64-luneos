FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH:${MACHINE} = "${MACHINE_ARCH}"

RDEPENDS:${PN}:append:${MACHINE} = " eg25-manager"

SRC_URI:append:${MACHINE} = "file://${MACHINE}/10-${MACHINE}-proximity.rules \
                             file://${MACHINE}/90-modem-eg25.rules \
"

SRC_URI:append:pinephone = "file://${MACHINE}/70-wifi-pm.rules \
"

do_install:append:${MACHINE}() {
    # to help configure proximity sensor
    install -m 0644 ${WORKDIR}/${MACHINE}/10-${MACHINE}-proximity.rules ${D}${sysconfdir}/udev/rules.d/10-${MACHINE}-proximity.rules
    # configure EG25 modem nodes
    install -m 0644 ${WORKDIR}/${MACHINE}/90-modem-eg25.rules ${D}${sysconfdir}/udev/rules.d/90-modem-eg25.rules
}

do_install:append:pinephone() {
    # setup wifi power saving mode when charger is (dis)connected
    install -m 0644 ${WORKDIR}/${MACHINE}/70-wifi-pm.rules ${D}${sysconfdir}/udev/rules.d/70-wifi-pm.rules
}
