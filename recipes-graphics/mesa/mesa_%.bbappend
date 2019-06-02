# Use even newer mesa for pinephone
PV_pinephone = "19.0.99+19.1.0-rc4+git${SRCPV}"
SRCREV_pinephone = "dd9635c1d2c52caafba71bf6ad9259046d555803"

FILESEXTRAPATHS_prepend_pinephone := "${THISDIR}/${BPN}:"
SRC_URI_pinephone = " \
    git://gitlab.freedesktop.org/mesa/mesa.git;branch=19.1;protocol=https \
    file://0001-Allow-enable-DRI-without-DRI-drivers.patch \
    file://0002-meson.build-check-for-all-linux-host_os-combinations.patch \
    file://0003-meson.build-make-TLS-GLX-optional-again.patch \
    file://0004-lima_screen-add-PIPE_TEXTURE_CUBE.patch \
"
S_pinephone = "${WORKDIR}/git"

PACKAGECONFIG[lima] = ""
PACKAGECONFIG_append_pinephone = " kmsro lima"
GALLIUMDRIVERS_append_pinephone = "${@bb.utils.contains('PACKAGECONFIG', 'lima', ',lima', '', d)}"

# git/src/gallium/winsys/svga/drm/vmw_msg.c:87:4: error: output number 4 not directly addressable
GALLIUMDRIVERS_remove = ",svga"
# Unfortunatelly this didn't work:
# GALLIUMDRIVERS_LLVM_remove = ",svga"
# GALLIUMDRIVERS_LLVM="r300,svga,nouveau"
GALLIUMDRIVERS_LLVM = "r300,nouveau"
