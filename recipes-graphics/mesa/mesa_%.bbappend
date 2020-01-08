# Use even newer mesa for pinephone
PV_pinephone = "19.3.99-git${SRCPV}"
SRCREV_pinephone = "3de2774dcb85fb2f87ae65a854fc5f25f0f34a91"

FILESEXTRAPATHS_prepend_pinephone := "${THISDIR}/${BPN}:"
LIC_FILES_CHKSUM_pinephone = "file://docs/license.html;md5=3a4999caf82cc503ac8b9e37c235782e"

SRC_URI_pinephone = " \
    git://gitlab.freedesktop.org/mesa/mesa.git;branch=master;protocol=https \
    file://0001-meson.build-check-for-all-linux-host_os-combinations.patch \
    file://0002-meson.build-make-TLS-ELF-optional.patch \
    file://0003-Allow-enable-DRI-without-DRI-drivers.patch \
    file://0004-Revert-mesa-Enable-asm-unconditionally-now-that-gen_.patch \
"

S_pinephone = "${WORKDIR}/git"

# to debug some issues
# EXTRA_OEMESON_append_pinephone = " --buildtype=debug "

PACKAGECONFIG[lima] = ""
PACKAGECONFIG_append_pinephone = " kmsro lima"
GALLIUMDRIVERS_append_pinephone = "${@bb.utils.contains('PACKAGECONFIG', 'lima', ',lima', '', d)}"
