DESCRIPTION = "Firmware files for RTL8723BS and RTL8723CS"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENCE.rtlwifi_firmware.txt;md5=00d06cfd3eddd5a2698948ead2ad54a5"

COMPATIBLE_MACHINE = "pinephone"

RDEPENDS_${PN} = "wireless-regdb"

SRCREV = "e6b9001e91110c654573b8f8e2db6155d10d3b57"
SRC_URI = " \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_ap_wowlan.bin?id=${SRCREV};downloadfilename=rtl8723bs_ap_wowlan.bin;name=rtl8723bs_ap_wowlan \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_wowlan.bin?id=${SRCREV};downloadfilename=rtl8723bs_wowlan.bin;name=rtl8723bs_wowlan \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_nic.bin?id=${SRCREV};downloadfilename=rtl8723bs_nic.bin;name=rtl8723bs_nic \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_bt.bin?id=${SRCREV};downloadfilename=rtl8723bs_bt.bin;name=rtl8723bs_bt \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtl_bt/rtl8723bs_fw.bin?id=${SRCREV};downloadfilename=rtl8723bs_fw.bin;name=rtl8723bs_fw \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtl_bt/rtl8723bs_config-OBDA8723.bin?id=${SRCREV};downloadfilename=rtl8723bs_config-OBDA8723.bin;name=rtl8723bs_config \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/LICENCE.rtlwifi_firmware.txt?id=${SRCREV};downloadfilename=LICENCE.rtlwifi_firmware.txt;name=LICENSE \
"
SRC_URI[rtl8723bs_ap_wowlan.md5sum] = "30240ec2230370617b3704cc2ed5895d"
SRC_URI[rtl8723bs_ap_wowlan.sha256sum] = "957707c7d6e01564685a801da1084a60b6b726c3d756d54dbe56ce064110e288"
SRC_URI[rtl8723bs_wowlan.md5sum] = "2bf10334c296ec239378933b4e6908b6"
SRC_URI[rtl8723bs_wowlan.sha256sum] = "534f6c4ad8a8068f7888db7af83408f0d2ef04e5d4fc8514e07522a4b0bab3a7"
SRC_URI[rtl8723bs_nic.md5sum] = "67d4aad0db155a701610b156473a75fd"
SRC_URI[rtl8723bs_nic.sha256sum] = "a52b5a7be4841b4b2839eddf5122b3300a3610866abe4bb3c3c9e444c8ab7969"
SRC_URI[rtl8723bs_bt.md5sum] = "f51dc85272c72353fb69f0d379dbd15a"
SRC_URI[rtl8723bs_bt.sha256sum] = "774f6628ae2cd7d6d8563cbf88f67f9a95e895cb44b55fb26ffdf895fdf26aea"
SRC_URI[rtl8723bs_fw.md5sum] = "521d95f75577c49eae09b00704c3823d"
SRC_URI[rtl8723bs_fw.sha256sum] = "580b240cac28ff0f47bcbf5e32c1ada9c82541707732d4d78a6bce6540a9c2b3"
SRC_URI[rtl8723bs_config.md5sum] = "57b61c775a51f7a4596950ded7d2d4c0"
SRC_URI[rtl8723bs_config.sha256sum] = "a6319ce368257b45820fcf11f8821b6eea7ddcc649f59a282498bf7a33210103"
SRC_URI[LICENSE.md5sum] = "00d06cfd3eddd5a2698948ead2ad54a5"
SRC_URI[LICENSE.sha256sum] = "a61351665b4f264f6c631364f85b907d8f8f41f8b369533ef4021765f9f3b62e"

S = "${WORKDIR}"

do_configure() {
}

do_compile() {
}

do_install() {
    install -d ${D}/lib/firmware/rtlwifi/
    install -m 0644 ${WORKDIR}/rtl8723bs_nic.bin ${D}/lib/firmware/rtlwifi/rtl8723bs_nic.bin
    install -m 0644 ${WORKDIR}/rtl8723bs_bt.bin ${D}/lib/firmware/rtlwifi/rtl8723bs_bt.bin
    install -m 0644 ${WORKDIR}/rtl8723bs_wowlan.bin ${D}/lib/firmware/rtlwifi/rtl8723bs_wowlan.bin
    install -d ${D}/lib/firmware/rtl_bt/
    install -m 0644 ${WORKDIR}/rtl8723bs_fw.bin ${D}/lib/firmware/rtl_bt/rtl8723bs_fw.bin
    install -m 0644 ${WORKDIR}/rtl8723bs_config-OBDA8723.bin ${D}/lib/firmware/rtl_bt/rtl8723bs_config-pine64.bin
}

FILES_${PN} = "/lib/firmware"

