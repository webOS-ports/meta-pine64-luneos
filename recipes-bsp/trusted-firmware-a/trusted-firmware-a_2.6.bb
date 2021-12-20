SUMMARY = "Arm Trusted Firmware (ATF)"
DESCRIPTION = "Trusted Firmware-A (TF-A) provides a reference implementation of secure world software for Armv7-A and Armv8-A, including a Secure Monitor executing at Exception Level 3 (EL3)."
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = 'file://license.rst;md5=1dd070c98a281d18d9eefd938729b031'

inherit deploy

PROVIDES = "virtual/arm-trusted-firmware"

BRANCH = "master"
SRCREV ?= "a1f02f4f3daae7e21ee58b4c93ec3e46b8f28d15"
SRC_URI = "git://git.trustedfirmware.org/TF-A/trusted-firmware-a.git;protocol=https;branch=${BRANCH}"

SRC_URI:append:rk3399 = " \
    file://0001-rk3399-baudrate.patch \
    "

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "pine64|sopine-a64|pinephone|pinephonepro"

PLATFORM:pine64 = "sun50i_a64"
PLATFORM:sopine-a64 = "sun50i_a64"
PLATFORM:pinephone = "sun50i_a64"
PLATFORM:pinephonepro = "rk3399"

BL_FILENAME:pinephone = "${MACHINE}"
BL_FILENAME:pinephonepro = "${PLATFORM}"

# Let the Makefile handle setting up the CFLAGS and LDFLAGS as it is a standalone application
CFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_configure() {
	:
}

do_compile() {
	oe_runmake -C ${S} BUILD_BASE=${B} CROSS_COMPILE="${TARGET_PREFIX}" PLAT="${PLATFORM}" bl31
}

do_install() {
	:
}

do_deploy() {
	install -m 0644 ${S}/${PLATFORM}/release/bl31/bl31.elf ${DEPLOYDIR}/bl31-${BL_FILENAME}.elf
	if [ -f "${S}/${PLATFORM}/release/bl31.bin" ]; then
		install -m 0644 ${S}/${PLATFORM}/release/bl31.bin ${DEPLOYDIR}/bl31-${BL_FILENAME}.bin	
	fi
}

addtask deploy before do_build after do_compile
