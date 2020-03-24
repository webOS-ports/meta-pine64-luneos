FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

PACKAGE_ARCH_pinephone = "${MACHINE_ARCH}"

SRC_URI_append_pinephone = " file://70-modem-eg25.rules"

do_install_append_pinephone() {
    install -m 0644 ${WORKDIR}/70-modem-eg25.rules ${D}${sysconfdir}/udev/rules.d/70-modem-eg25.rules
}
