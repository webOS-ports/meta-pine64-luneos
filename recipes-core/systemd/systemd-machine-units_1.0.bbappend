FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:pinetab2 = " \
    file://wifi-module-load.service \
    file://wifi-macaddr-persister.service \
    file://persist-wifi-mac-addr.sh \
    file://hciattach.service \
    file://hciattach.sh \
"

do_install:append:pinetab2() {
    install -d ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/wifi-macaddr-persister.service ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/wifi-module-load.service ${D}${systemd_unitdir}/system
    install -m 0644 ${WORKDIR}/hciattach.service ${D}${systemd_unitdir}/system
    
    install -d ${D}${systemd_unitdir}/system/scripts
    install -m 0755 ${WORKDIR}/persist-wifi-mac-addr.sh ${D}${systemd_unitdir}/system/scripts
    install -m 0755 ${WORKDIR}/hciattach.sh ${D}${systemd_unitdir}/system/scripts
}

SYSTEMD_SERVICE:${PN}:pinetab2 = " \
    wifi-module-load.service \
    wifi-macaddr-persister.service \
    hciattach.service \    
"

FILES:${PN}:pinetab2 += " \
    ${systemd_unitdir}/system/scripts \
"
