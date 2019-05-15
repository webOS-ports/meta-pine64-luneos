FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = "\
    file://0001-lima-fix-tile-buffer-reloading.patch \
"

PACKAGECONFIG += "lima"

GALLIUMDRIVERS_append = ",kmsro,lima"
