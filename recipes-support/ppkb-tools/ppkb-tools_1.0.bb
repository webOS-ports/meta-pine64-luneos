SUMMARY = "megi's PinePhone keyboard tools (firmware flasher, charger control, debugger)"
DESCRIPTION = "Userspace tools for the PinePhone keyboard case MCU: ppkb-i2c-flasher \
to update the keyboard firmware over I2C, ppkb-i2c-charger-ctl to talk to the case's \
IP5209 charger, and the I2C/USB debuggers."
HOMEPAGE = "https://xnux.eu/devices/pine64-pinephone-keyboard.html"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=8f0e2cd40e05189ec81232da84bd6e1a"

# Upstream moved: megous.com has lapsed and now 301s to an unrelated domain, so
# it must NOT be used as a source. xff.cz is megi's current host.
SRC_URI = "git://xff.cz/git/pinephone-keyboard;protocol=https;branch=master"
SRCREV = "aaffb9940a42ed61032571e8ab2b460e04787d94"

COMPATIBLE_MACHINE = "pinephone|pinephonepro"

# The Makefile links with "$(CC) $(CFLAGS) -o $@ $<" and never references
# LDFLAGS, so fold ours into CFLAGS or the binaries fail the GNU_HASH QA check.
CFLAGS:append = " ${LDFLAGS}"

# Build the tools individually rather than the "tools" target: that also builds
# ppkb-i2c-inputd, which generates kmap.h with "php keymaps/map-to-c.php" and
# would drag a php-native dependency in for a userspace input daemon we do not
# need (the kernel's pinephone-keyboard driver handles input).
PPKB_TOOLS = "ppkb-i2c-flasher ppkb-i2c-charger-ctl ppkb-i2c-debugger ppkb-usb-flasher ppkb-usb-debugger"

# VERSION is "$(shell git describe)" at parse time, which has no tags to find
# here; pass it explicitly so the build is reproducible and quiet.
do_compile() {
    for t in ${PPKB_TOOLS}; do
        oe_runmake VERSION="${PV}-${SRCREV}" build/$t
    done
}

do_install() {
    install -d ${D}${bindir}
    for t in ${PPKB_TOOLS}; do
        install -m 0755 ${B}/build/$t ${D}${bindir}/$t
    done
}
