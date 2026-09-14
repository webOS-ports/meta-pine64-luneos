SECTION = "kernel"
SUMMARY = "BES2600 Device Driver for Linux"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "virtual/kernel"

inherit module

# Our fork of cringeops' driver. herrie/fixes carries the LuneOS series on top of
# current cringeops master: bes2600_compat.h plus Makefile header probes so the
# driver builds against anything from v6.1 to v7.x, and ~30 real bug fixes found
# on the way (heap overflow in wsm_buf_reserve(), OOB read stripping WPS/P2P IEs,
# GFP_KERNEL under a spinlock, RCU and locking errors, the 5 GHz channel table).
#
# The old 0001-Fix-build-with-Linux-6.6.9.patch is gone; bes2600_compat.h replaces it.
PV = "0.6.9+git"
SRCREV = "4a10c0405aa125cd0f32f43a0aea71ed6796790b"

SRC_URI = "git://github.com/Herrie82/bes2600.git;branch=herrie/fixes;protocol=https"

MODULES_MODULE_SYMVERS_LOCATION = "bes2600"
EXTRA_OEMAKE += "KERN_DIR=${STAGING_KERNEL_BUILDDIR} -C ${STAGING_KERNEL_BUILDDIR} M=${S}/bes2600"

# The Makefile greps the kernel headers to decide which mac80211/timer API shape
# to compile against, and a pattern that stops matching fails silently into the
# wrong one. Print what it resolved so a future kernel bump is visible in the log.
do_compile:prepend() {
    bbnote "bes2600 API probes: $(grep -o 'BES2600_HAVE_[A-Z_]*' ${S}/bes2600/Makefile | sort -u | tr '\n' ' ')"
}
