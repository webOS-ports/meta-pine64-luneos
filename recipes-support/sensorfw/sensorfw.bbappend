FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

# Configuration files for the sensors on Pine64 devices
SRC_URI:append:pinephone = " \
    file://sensord-pinephone.conf \
"
SRC_URI:append:pinephonepro = " \
    file://sensord-pinephonepro.conf \
"

SRC_URI:append:pinetab2 = " \
    file://sensord-pinetab2.conf \
"
