SUMMARY = "LuneOS configfs USB gadget (ECM network)"
DESCRIPTION = "Creates the configfs USB gadget and brings up usb0 at \
172.16.42.2 from the rootfs, for devices that cannot rely on the initramfs \
doing it. Idempotent: if a gadget is already bound to a UDC it does nothing."
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

# The PineTab2's initramfs already does this (initramfs-simple-image, bundled
# into the kernel, so it actually unpacks). The PinePhone Pro's initramfs is
# discarded by the kernel, and the PinePhone has no INITRAMFS_IMAGE at all -
# both end up with a registered but unbound UDC and no USB networking.
COMPATIBLE_MACHINE = "pinephonepro|pinephone"

SRC_URI = " \
    file://luneos-usb-gadget.sh \
    file://luneos-usb-gadget.service \
"

S = "${UNPACKDIR}"

inherit systemd

SYSTEMD_SERVICE:${PN} = "luneos-usb-gadget.service"
SYSTEMD_AUTO_ENABLE = "enable"

RDEPENDS:${PN} = "iproute2"

do_install() {
    install -d ${D}${sbindir}
    install -m 0755 ${UNPACKDIR}/luneos-usb-gadget.sh ${D}${sbindir}/luneos-usb-gadget.sh

    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${UNPACKDIR}/luneos-usb-gadget.service ${D}${systemd_system_unitdir}/luneos-usb-gadget.service
}

FILES:${PN} += "${systemd_system_unitdir}/luneos-usb-gadget.service"
