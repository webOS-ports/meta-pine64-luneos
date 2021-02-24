SUMMARY = "Quectel EG25 management daemon"

LICENSE = "GPL-3.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "glib-2.0 libgpiod libgudev libusb glib-2.0-native"
RDEPENDS_${PN} = "atinout"

inherit meson
inherit systemd

PV = "0.2.0"

SRCREV = "40136c2a520d3be681aac4201a0e8ef324616140"
SRC_URI = " \
    git://gitlab.com/mobian1/devices/eg25-manager;protocol=https;branch=master \
    file://0001-Fix-udev-dir-for-LuneOS.patch \
    file://pinephone-setup-modem.sh \
    file://eg25-manager.service \
"
S = "${WORKDIR}/git"

SYSTEMD_SERVICE_${PN} = "eg25-manager.service"

do_install_append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/pinephone-setup-modem.sh ${D}${bindir}/pinephone-setup-modem.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/eg25-manager.service ${D}${systemd_system_unitdir}/eg25-manager.service
}

FILES_${PN} = "${sysconfdir}/udev/rules.d/80-modem-eg25.rules \
               ${datadir}/eg25-manager \
               ${bindir}/eg25manager \
               ${bindir}/eg25-configure-usb \
               ${bindir}/pinephone-setup-modem.sh \
               ${systemd_system_unitdir}/eg25-manager.service \
"
