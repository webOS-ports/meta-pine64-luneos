SUMMARY = "Quectel EG25 management daemon"

LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "glib-2.0 libgpiod libgudev libusb glib-2.0-native curl"
RDEPENDS:${PN} = "atinout"

inherit meson systemd pkgconfig

PV = "0.4.2-1+git${SRCPV}"

SRCREV = "88c68b9933f47f863e6685b2b6502d5d7ed1871c"
SRC_URI = " \
    git://gitlab.com/mobian1/devices/eg25-manager.git;protocol=https;branch=master \
    file://0001-Fix-udev-dir-for-LuneOS.patch \
    file://0002-Add-VoLTE-configuration.patch \
    file://0003-Fix-not-existing-mm_modem-when-not-using-modemmanage.patch \
    file://eg25-manager.service \
"
S = "${WORKDIR}/git"

SYSTEMD_SERVICE:${PN} = "eg25-manager.service"

do_install:append() {
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/eg25-manager.service ${D}${systemd_system_unitdir}/eg25-manager.service
}

FILES:${PN} = "/usr/udev \
               /usr/udev/rules.d \
               /usr/udev/rules.d/80-modem-eg25.rules \
               ${libdir} \
               ${datadir}/eg25-manager \
               ${bindir}/eg25manager \
               ${bindir}/eg25-configure-usb \
               ${systemd_system_unitdir}/eg25-manager.service \
"
