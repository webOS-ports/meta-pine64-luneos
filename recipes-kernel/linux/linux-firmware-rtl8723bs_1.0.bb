DESCRIPTION = "Firmware files for RTL8723BS and RTL8723CS"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://LICENCE.rtlwifi_firmware.txt;md5=00d06cfd3eddd5a2698948ead2ad54a5"

COMPATIBLE_MACHINE = "pinephone"

RDEPENDS:${PN} = "wireless-regdb"

SRCREV = "e6b9001e91110c654573b8f8e2db6155d10d3b57"
SRCREV_megous = "4ec2645b007ba4c3f2962e38b50c06f274abbf7c"
SRC_URI = " \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_ap_wowlan.bin?id=${SRCREV};downloadfilename=rtl8723bs_ap_wowlan.bin;name=rtl8723bs_ap_wowlan \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_wowlan.bin?id=${SRCREV};downloadfilename=rtl8723bs_wowlan.bin;name=rtl8723bs_wowlan \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_nic.bin?id=${SRCREV};downloadfilename=rtl8723bs_nic.bin;name=rtl8723bs_nic \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_bt.bin?id=${SRCREV};downloadfilename=rtl8723bs_bt.bin;name=rtl8723bs_bt \
    https://megous.com/git/linux-firmware/plain/rtl_bt/rtl8723cs_xx_fw.bin?id=${SRCREV_megous};downloadfilename=rtl8723cs_xx_fw.bin;name=rtl8723cs_xx_fw \
    https://megous.com/git/linux-firmware/plain/rtl_bt/rtl8723cs_xx_config-pinephone.bin?id=${SRCREV_megous};downloadfilename=rtl8723cs_xx_config-pinephone.bin;name=rtl8723cs_xx_config \
    https://megous.com/git/linux-firmware/plain/ov5640_af.bin?id=${SRCREV_megous};downloadfilename=ov5640_af.bin;name=ov5640_af \
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
SRC_URI[rtl8723cs_xx_fw.md5sum] = "d51efca9486c57d08a3b5be408e633b9"
SRC_URI[rtl8723cs_xx_fw.sha256sum] = "c68091565d90c29735bedf72d0bf6590c186ab802ef4fef4caa66ef5af25b870"
SRC_URI[rtl8723cs_xx_config.md5sum] = "2344554fed8337eb1d3e7bc10e9b307f"
SRC_URI[rtl8723cs_xx_config.sha256sum] = "492531d5a0a44ed5d0e174476543735eafe2cacc5ff5ce9e8e10092d303b563c"
SRC_URI[ov5640_af.md5sum] = "7c381ff7c0238d2510c9662b3310dd61"
SRC_URI[ov5640_af.sha256sum] = "439245623bc99f3b0d8c44d47baed3cc17cad01b9191509c89bb8d92a98949c9"
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
    install -m 0644 ${WORKDIR}/rtl8723cs_xx_fw.bin ${D}/lib/firmware/rtl_bt/rtl8723cs_xx_fw.bin
    install -m 0644 ${WORKDIR}/rtl8723cs_xx_config-pinephone.bin ${D}/lib/firmware/rtl_bt/rtl8723cs_xx_config-pinephone.bin
}

FILES:${PN} = "/lib/firmware"

