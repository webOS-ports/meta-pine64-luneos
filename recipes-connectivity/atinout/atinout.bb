SUMMARY = "Execute AT commands in sequence and capture the response from the modem"
HOMEPAGE = "https://sourceforge.net/projects/atinout/"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://gplv3.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "util-linux"

PV = "0.9.1"

SRC_URI = "http://sourceforge.net/projects/atinout/files/v${PV}/atinout-${PV}.tar.gz \
           file://0001-do-not-rely-on-CR-in-modem-output.patch \
           file://0002-tr_lf_cr-take-a-non-const-string.patch \
"
SRC_URI[md5sum] = "4448694147cd630dde1abfd73a10d753"
SRC_URI[sha256sum] = "4d15c8288aca414e11cd304686b172696104c5e42bf776300311c005634854a2"

# DEBUG_PREFIX_MAP has to ride along on CC: the Makefile assigns CFLAGS itself
# (warning flags, -DVERSION, -g), so OE's CFLAGS never reach the compiler and
# the -g debug info keeps absolute TMPDIR paths. That is only a warning on
# scarthgap but fails do_package_qa on wrynose:
#   QA Issue: File /usr/bin/.debug/atinout in package atinout-dbg contains
#   reference to TMPDIR [buildpaths]
# Overriding CFLAGS wholesale would mean restating -DVERSION here and keeping it
# in sync with upstream's, so extend CC instead.
EXTRA_OEMAKE = "CC='${CC} ${DEBUG_PREFIX_MAP}' LDFLAGS='${LDFLAGS} -Werror=implicit-fallthrough=0'"

do_install() {
    oe_runmake install DESTDIR=${D}
}

