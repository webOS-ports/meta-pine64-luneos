# Use even newer mesa for pinephone
PV_pinephone = "19.3.99-git${SRCPV}"
SRCREV_pinephone = "2ebfc6db16137b16663bf563c32fe1932917b22c"

# Currently all these patches are provided by oe-core recipe backported
# from 3.1 Dunfell to meta-webos-ports/meta-luneos-backports-3.1/recipes-graphics/mesa
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

PACKAGECONFIG_append_pinephone = " kmsro lima"
