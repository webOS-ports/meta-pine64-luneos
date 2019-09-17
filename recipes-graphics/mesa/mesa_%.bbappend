# Use even newer mesa for pinephone
PV_pinephone = "19.0.99+19.1.6-git${SRCPV}"
SRCREV_pinephone = "aa77fc309a87bc263ebacaa9c69cd623ba5c7e23"
LIC_FILES_CHKSUM_pinephone = "file://docs/license.html;md5=3a4999caf82cc503ac8b9e37c235782e"

FILESEXTRAPATHS_prepend_pinephone := "${THISDIR}/${BPN}:"
SRC_URI_pinephone = " \
    git://gitlab.freedesktop.org/mesa/mesa.git;branch=master;protocol=https \
    file://0001-Allow-enable-DRI-without-DRI-drivers.patch \
    file://0002-meson.build-check-for-all-linux-host_os-combinations.patch \
    file://0003-meson.build-make-TLS-GLX-optional-again.patch \
    file://0004-lima_screen-add-PIPE_TEXTURE_CUBE.patch \
    file://0006-Fix_scissor_test.patch \
    file://0007-Support-for-branching.patch \
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