DESCRIPTION = "Various firmware files for Pine64 PinePhone, PinePhonePro and PineTab2"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENCE.rtlwifi_firmware.txt;md5=00d06cfd3eddd5a2698948ead2ad54a5"

COMPATIBLE_MACHINE = "pinephonepro|pinephone|pinetab2"

RDEPENDS:${PN} = "wireless-regdb"

SRCREV_kernel = "e6b9001e91110c654573b8f8e2db6155d10d3b57"
SRCREV_pinerock = "937f0d52d27d7712da6a008d35fd7c2819e2b077"
SRCREV_ap6256bt = "a30bf312b268eab42d38fab0cc3ed3177895ff5d"
SRCREV_wifinonfree = "f713a6054746bc61ece1c8696dce91a7b7e22dd9"
SRCREV_bes2600 = "32894ca7fa1326f364d919955b7f16121588d6ad"
SRCREV_ov5640cam = "61beaa4eb1ad87ad067cfbe123fbcd0a0cf01246"
SRCREV_megous = "6e8e591e17e207644dfe747e51026967bb1edab5"

SRC_URI = " \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_ap_wowlan.bin?id=${SRCREV_kernel};downloadfilename=rtl8723bs_ap_wowlan.bin;name=rtl8723bs_ap_wowlan \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_wowlan.bin?id=${SRCREV_kernel};downloadfilename=rtl8723bs_wowlan.bin;name=rtl8723bs_wowlan \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_nic.bin?id=${SRCREV_kernel};downloadfilename=rtl8723bs_nic.bin;name=rtl8723bs_nic \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/rtlwifi/rtl8723bs_bt.bin?id=${SRCREV_kernel};downloadfilename=rtl8723bs_bt.bin;name=rtl8723bs_bt \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/mediatek/mt7610u.bin?id=${SRCREV_kernel};downloadfilename=mt7610u.bin;name=mt7610u \
    https://megous.com/git/linux-firmware/plain/rtl_bt/rtl8723cs_xx_fw.bin?id=${SRCREV_megous};downloadfilename=rtl8723cs_xx_fw.bin;name=rtl8723cs_xx_fw \
    https://megous.com/git/linux-firmware/plain/rtl_bt/rtl8723cs_xx_config.bin?id=${SRCREV_megous};downloadfilename=rtl8723cs_xx_config.bin;name=rtl8723cs_xx_config \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/LICENCE.rtlwifi_firmware.txt?id=${SRCREV_kernel};downloadfilename=LICENCE.rtlwifi_firmware.txt;name=LICENSE_rtlwifi \
    https://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git/plain/LICENCE.mediatek?id=${SRCREV_kernel};downloadfilename=LICENCE.mediatek;name=LICENSE_mediatek \
    git://gitlab.manjaro.org/tsys/pinebook-firmware.git;branch=master;protocol=https;name=pinerock;destsuffix=git/pinerock \
    git://gitlab.manjaro.org/manjaro-arm/packages/community/ap6256-firmware.git;branch=master;protocol=https;name=ap6256bt;destsuffix=git/ap6256bt \
    git://github.com/herrie82/bes2600-firmware.git;branch=0.3.10_2022.1125;protocol=https;name=bes2600;destsuffix=git/bes2600 \
    git://github.com/RPi-Distro/firmware-nonfree.git;branch=buster;protocol=https;name=wifinonfree;destsuffix=git/wifinonfree \
    git://github.com/pmsourcedump/ov5640.git;branch=master;protocol=https;name=ov5640cam;destsuffix=git/ov5640cam \
"

SRC_URI[rtl8723bs_ap_wowlan.sha256sum] = "957707c7d6e01564685a801da1084a60b6b726c3d756d54dbe56ce064110e288"
SRC_URI[rtl8723bs_wowlan.sha256sum] = "534f6c4ad8a8068f7888db7af83408f0d2ef04e5d4fc8514e07522a4b0bab3a7"
SRC_URI[rtl8723bs_nic.sha256sum] = "a52b5a7be4841b4b2839eddf5122b3300a3610866abe4bb3c3c9e444c8ab7969"
SRC_URI[rtl8723bs_bt.sha256sum] = "774f6628ae2cd7d6d8563cbf88f67f9a95e895cb44b55fb26ffdf895fdf26aea"
SRC_URI[rtl8723cs_xx_fw.sha256sum] = "c68091565d90c29735bedf72d0bf6590c186ab802ef4fef4caa66ef5af25b870"
SRC_URI[rtl8723cs_xx_config.sha256sum] = "492531d5a0a44ed5d0e174476543735eafe2cacc5ff5ce9e8e10092d303b563c"
SRC_URI[ov5640_af.sha256sum] = "439245623bc99f3b0d8c44d47baed3cc17cad01b9191509c89bb8d92a98949c9"
SRC_URI[mt7610u.sha256sum] = "5a4268e9021bb587426ba624b425f1e660bfc82cd63b36ad3ce6fb9ce6751760"
SRC_URI[LICENSE_rtlwifi.sha256sum] = "a61351665b4f264f6c631364f85b907d8f8f41f8b369533ef4021765f9f3b62e"
SRC_URI[LICENSE_mediatek.sha256sum] = "a90d3f66704d85889945fec5525ea77622549da83aced1aac99828383f8f1805"

S = "${WORKDIR}/git"

do_configure() {
}

do_compile() {
}

do_install() {
    install -d ${D}${nonarch_base_libdir}/firmware/rtlwifi/
    install -m 0644 ${WORKDIR}/rtl8723bs_nic.bin ${D}${nonarch_base_libdir}/firmware/rtlwifi/rtl8723bs_nic.bin
    install -m 0644 ${WORKDIR}/rtl8723bs_bt.bin ${D}${nonarch_base_libdir}/firmware/rtlwifi/rtl8723bs_bt.bin
    install -m 0644 ${WORKDIR}/rtl8723bs_wowlan.bin ${D}${nonarch_base_libdir}/firmware/rtlwifi/rtl8723bs_wowlan.bin
    install -d ${D}${nonarch_base_libdir}/firmware/rtl_bt/
    install -m 0644 ${WORKDIR}/rtl8723cs_xx_fw.bin ${D}${nonarch_base_libdir}/firmware/rtl_bt/rtl8723cs_xx_fw.bin
    install -m 0644 ${WORKDIR}/rtl8723cs_xx_config.bin ${D}${nonarch_base_libdir}/firmware/rtl_bt/rtl8723cs_xx_config.bin
    install -m 0644 ${WORKDIR}/git/ov5640cam/ov5640_af.bin ${D}${nonarch_base_libdir}/firmware/ov5640_af.bin
}

do_install:append:pinephonepro() {
    install -d ${D}${nonarch_base_libdir}/firmware/rockchip/
    install -d ${D}${nonarch_base_libdir}/firmware/brcm/
    install -m 0644 ${WORKDIR}/git/pinerock/rockchip/dptx.bin ${D}${nonarch_base_libdir}/firmware/rockchip/dptx.bin
    install -m 0644 ${S}/ap6256bt/BCM4345C5.hcd ${D}${nonarch_base_libdir}/firmware/brcm
    install -m 0644 ${WORKDIR}/git/wifinonfree/brcm/brcmfmac4345* ${D}${nonarch_base_libdir}/firmware/brcm
    ln -s brcmfmac43456-sdio.txt ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43456-sdio.pine64,pinephone-pro.txt
}

do_install:append:pinetab2() {
    install -d ${D}${nonarch_base_libdir}/firmware/rockchip/
    install -m 0644 ${WORKDIR}/git/pinerock/rockchip/dptx.bin ${D}${nonarch_base_libdir}/firmware/rockchip/dptx.bin
    install -d ${D}${nonarch_base_libdir}/firmware/bes2600/
    install -m 0644 ${S}/bes2600/bes2600_factory.txt ${D}${nonarch_base_libdir}/firmware/bes2600/bes2600_factory.txt
    install -m 0644 ${S}/bes2600/best2002_fw_boot_sdio.bin ${D}${nonarch_base_libdir}/firmware/bes2600/best2002_fw_boot_sdio.bin
    install -m 0644 ${S}/bes2600/best2002_fw_sdio.bin ${D}${nonarch_base_libdir}/firmware/bes2600/best2002_fw_sdio.bin
    install -m 0644 ${S}/bes2600/best2002_fw_sdio_btrf.bin ${D}${nonarch_base_libdir}/firmware/bes2600/best2002_fw_sdio_btrf.bin
    install -m 0644 ${S}/bes2600/best2002_fw_sdio_nosignal.bin ${D}${nonarch_base_libdir}/firmware/bes2600/best2002_fw_sdio_nosignal.bin
    install -d ${D}${nonarch_base_libdir}/firmware/mediatek/
    install -m 0644 ${WORKDIR}/mt7610u.bin ${D}${nonarch_base_libdir}/firmware/mediatek/mt7610u.bin
}

FILES:${PN} = "${nonarch_base_libdir}/firmware"
