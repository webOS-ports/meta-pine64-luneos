SUMMARY = "Boot-time hardware state dump for the Pine64 devices"
DESCRIPTION = "Writes /var/log/luneos-diag.txt once per boot with the sysfs \
state that the kernel log does not show: dwc3/UDC/gadget binding, Type-C \
roles, extcon cables, power supplies, DRM nodes and who has them open, \
whether libEGL has wl_drm, and the wireless regulatory database."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

COMPATIBLE_MACHINE = "pinephonepro|pinephone|pinetab2"

SRC_URI = " \
    file://luneos-diag.sh \
    file://luneos-diag.service \
    file://luneos-diag-event.service \
    file://90-luneos-diag.rules \
"

S = "${UNPACKDIR}"

# Not allarch: allarch recipes are shared across machines, which contradicts
# COMPATIBLE_MACHINE and confuses sstate reuse. The package is tiny either way.
inherit systemd

# luneos-diag-event.service is started by udev (SYSTEMD_WANTS), never enabled.
SYSTEMD_SERVICE:${PN} = "luneos-diag.service"
SYSTEMD_AUTO_ENABLE = "enable"

# iw is only used if present - the script degrades gracefully without it, but
# "iw reg get" is the quickest way to see which regulatory domain won.
RRECOMMENDS:${PN} = "iw"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/luneos-diag.sh ${D}${sbindir}/luneos-diag.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/luneos-diag.service ${D}${systemd_system_unitdir}/luneos-diag.service
    install -m 0644 ${UNPACKDIR}/luneos-diag-event.service ${D}${systemd_system_unitdir}/luneos-diag-event.service

    install -d ${D}${nonarch_base_libdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/90-luneos-diag.rules ${D}${nonarch_base_libdir}/udev/rules.d/90-luneos-diag.rules
}

FILES:${PN} += " \
    ${systemd_system_unitdir}/luneos-diag.service \
    ${systemd_system_unitdir}/luneos-diag-event.service \
    ${nonarch_base_libdir}/udev/rules.d/90-luneos-diag.rules \
"
