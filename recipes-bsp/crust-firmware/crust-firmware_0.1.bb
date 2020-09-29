SUMMARY = "Crust Firmware"
DESCRIPTION = "Crust Libre SCP firmware for Allwinner sunxi SoCs."
LICENSE = "BSD-1-Clause & BSD-3-Clause & GPLv2 & MIT"
LIC_FILES_CHKSUM = 'file://LICENSE.md;md5=ef8480c8f81934ffe7a6fdadd085c012'

inherit deploy

DEPENDS = "flex-native bison-native"

BRANCH = "master"
SRCREV ?= "e63106c70bc4ddfa3575e4f1f3667de0795252a8"
SRC_URI = "git://github.com/crust-firmware/crust.git;protocol=git;branch=${BRANCH} \
           https://musl.cc/or1k-linux-musl-cross.tgz \
"

S = "${WORKDIR}/git"

SRC_URI[sha256sum] = "52ccae980fb1e14c55ff6435a42eb26021b1baa45558864d0b58c55fc88db097"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "pinephone"
PLATFORM_pinephone = "sun50i_a64"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_configure() {
	make BUILDCC=${BUILD_CC} BUILDAR=${BUILD_AR} LEX=flex YACC=yacc CROSS_COMPILE=${WORKDIR}/or1k-linux-musl-cross/bin/or1k-linux-musl- HOST_COMPILE=aarch64-webos-linux- pinephone_defconfig
}

do_compile() {
	make BUILDCC=${BUILD_CC} BUILDAR=${BUILD_AR} LEX=flex YACC=yacc CROSS_COMPILE=${WORKDIR}/or1k-linux-musl-cross/bin/or1k-linux-musl- HOST_COMPILE=aarch64-webos-linux- build/scp/scp.bin
}

do_install() {
	:
}

do_deploy() {
	install -m 0644 ${S}/build/scp/scp.elf ${DEPLOYDIR}/scp-${MACHINE}.elf
	install -m 0644 ${S}/build/scp/scp.bin ${DEPLOYDIR}/scp-${MACHINE}.bin
}

addtask deploy before do_build after do_compile
