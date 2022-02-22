SUMMARY = "Execute AT commands in sequence and capture the response from the modem"
HOMEPAGE = "https://sourceforge.net/projects/atinout/"
LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://gplv3.txt;md5=d32239bcb673463ab874e80d47fae504"

DEPENDS = "util-linux"

PV = "0.9.1"

SRC_URI = "http://sourceforge.net/projects/atinout/files/v${PV}/atinout-${PV}.tar.gz \
           file://0001-do-not-rely-on-CR-in-modem-output.patch \
"
SRC_URI[md5sum] = "4448694147cd630dde1abfd73a10d753"
SRC_URI[sha256sum] = "4d15c8288aca414e11cd304686b172696104c5e42bf776300311c005634854a2"

EXTRA_OEMAKE = "CC='${CC}' LDFLAGS='${LDFLAGS} -Werror=implicit-fallthrough=0'"

do_install() {
    oe_runmake install DESTDIR=${D}
}

