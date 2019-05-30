FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "\
    file://0002-lima_screen-add-PIPE_TEXTURE_CUBE.patch \
"

PACKAGECONFIG += "lima"

GALLIUMDRIVERS_append = ",kmsro,lima"
