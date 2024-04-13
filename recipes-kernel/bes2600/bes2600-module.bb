SECTION = "kernel"
SUMMARY = "BES2600 Device Driver for Linux"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "virtual/kernel"

inherit module

PV = "0.6.9+git"
SRCREV = "1b61b519a3b5697f7a9bfff15cb957e0aad03d65"

SRC_URI = "git://github.com/cringeops/bes2600.git;branch=master;protocol=https \
           file://0001-Fix-build-with-Linux-6.6.9.patch \
"
S = "${WORKDIR}/git"

MODULES_MODULE_SYMVERS_LOCATION = "bes2600"
EXTRA_OEMAKE += "KERN_DIR=${STAGING_KERNEL_BUILDDIR} -C ${STAGING_KERNEL_BUILDDIR} M=${S}/bes2600"
