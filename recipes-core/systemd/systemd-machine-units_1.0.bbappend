FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# The pinetab2 append below adds nothing but local files, so nothing lands in
# the default S = "${UNPACKDIR}/${BP}" and do_qa_unpack warns about it.
# Scoped to pinetab2 rather than set outright: this layer is in BBLAYERS for
# every build, so a bare S here would also apply to MACHINEs that get no
# SRC_URI from this layer at all.
S:pinetab2 = "${UNPACKDIR}"

SRC_URI:append:pinetab2 = " \
    file://wifi-module-load.service \
    file://wifi-macaddr-persister.service \
    file://persist-wifi-mac-addr.sh \
    file://hciattach.service \
    file://hciattach.sh \
"

do_install:append:pinetab2() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/wifi-macaddr-persister.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/wifi-module-load.service ${D}${systemd_unitdir}/system
    install -m 0644 ${UNPACKDIR}/hciattach.service ${D}${systemd_unitdir}/system
    
    install -d ${D}${systemd_unitdir}/system/scripts
    install -m 0755 ${UNPACKDIR}/persist-wifi-mac-addr.sh ${D}${systemd_unitdir}/system/scripts
    install -m 0755 ${UNPACKDIR}/hciattach.sh ${D}${systemd_unitdir}/system/scripts
}

SYSTEMD_SERVICE:${PN}:pinetab2 = " \
    wifi-module-load.service \
    wifi-macaddr-persister.service \
    hciattach.service \    
"

FILES:${PN}:pinetab2 += " \
    ${systemd_unitdir}/system/scripts \
"

# PinePhone: no login on the UART unless LUNEOS_DEBUG_ACCESS = "1" (see
# SERIAL_CONSOLES in pinephone.conf). systemd-getty-generator starts
# serial-getty@ttyS0 for the console=ttyS0 on the kernel command line whatever
# SERIAL_CONSOLES says, so mask the instance.
do_install:append:pinephone() {
    if [ "${@oe.utils.conditional('LUNEOS_DEBUG_ACCESS', '1', '1', '0', d)}" != "1" ]; then
        install -d ${D}${sysconfdir}/systemd/system
        ln -sf /dev/null ${D}${sysconfdir}/systemd/system/serial-getty@ttyS0.service
    fi
}

FILES:${PN}:append:pinephone = " ${sysconfdir}/systemd/system/serial-getty@ttyS0.service"
