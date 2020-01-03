DESCRIPTION = "Starting EG25 WWAN module"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"

PV = "1.0"

inherit systemd

SRC_URI = " \
    file://eg25-manager.service \
    file://eg25-manager.sh \
"

SYSTEMD_SERVICE_${PN} = "eg25-manager.service"

do_install() {
    install -d ${D}${base_sbindir}
    install -m 0755 ${WORKDIR}/eg25-manager.sh ${D}${base_sbindir}/eg25-manager.sh
    
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/eg25-manager.service ${D}${systemd_system_unitdir}/eg25-manager.service
}

FILES_${PN} = "${base_sbindir}/eg25-manager.sh ${systemd_system_unitdir}/eg25-manager.service"
