# build package busybox-mdev, needed by initramfs scripts
FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

SRC_URI += " \
    file://telnetd.cfg \
"

