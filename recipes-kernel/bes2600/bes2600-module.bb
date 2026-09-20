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
SRCREV = "23523d9b1119987b15a51b897067374d883b99fe"

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/Herrie82/bes2600.git;branch=herrie/fixes;protocol=https"

MODULES_MODULE_SYMVERS_LOCATION = "bes2600"
EXTRA_OEMAKE += "KERN_DIR=${STAGING_KERNEL_BUILDDIR} -C ${STAGING_KERNEL_BUILDDIR} M=${S}/bes2600"

# The driver reads (and rewrites) its factory calibration data from a path baked
# in at build time - bes2600/Makefile has
#
#     FACTORY_PATH ?= /lib/firmware/bes2600_factory.txt
#
# but linux-firmware-pine64 installs that file under ${nonarch_base_libdir}/firmware/bes2600/,
# next to the rest of the BES2600 blobs. Without this the probe gets as far as
# loading the firmware and then dies:
#
#     read and check /lib/firmware/bes2600_factory.txt error
#     factory cali data get failed.
#     wait dpd data ready failed:-110
#     bes2600_sdio_probe failed, func:1
#
# The Makefile uses ?= so a make-level override is all it takes.
# The driver defaults to /lib/firmware/bes2600_factory.txt while the firmware
# recipe installs the file to /lib/firmware/bes2600/, so without this the
# lookup fails with "get factory data fali, check whether the file exists".
#
# Tested with the file renamed away on 2026-09-08: the driver then reports
# "factory cali data get failed" and the DPD stage fails in exactly the same
# way ("wait dpd data ready failed:-110"), so the factory calibration data is
# NOT what makes the chip stop answering during firmware download. Keeping the
# override because finding the file is correct in its own right.
EXTRA_OEMAKE += "FACTORY_PATH=${nonarch_base_libdir}/firmware/bes2600/bes2600_factory.txt"

# The Makefile greps the kernel headers to decide which mac80211/timer API shape
# to compile against, and a pattern that stops matching fails silently into the
# wrong one. Print what it resolved so a future kernel bump is visible in the log.
do_compile:prepend() {
    bbnote "bes2600 API probes: $(grep -o 'BES2600_HAVE_[A-Z_]*' ${S}/bes2600/Makefile | sort -u | tr '\n' ' ')"
}

# NOTE: do not enable CONFIG_BES2600_ENABLE_DEVEL_LOGS for normal builds.
#
# It is invaluable for diagnosing firmware download problems, but the driver
# emits ~715 debug lines a second once associated. Formatting all of that
# through journald onto a microSD card slows the RX path enough that
# association takes 11 seconds instead of milliseconds, by which point the AP
# has aged out the authentication and answers with reason 2
# (PREV_AUTH_NOT_VALID). Several days of "WiFi associates then drops" were
# this, not a driver bug.
#
# To turn it back on temporarily:
#   EXTRA_OEMAKE += "CONFIG_BES2600_ENABLE_DEVEL_LOGS=y"
#   plus bes2600-dbg-config (options bes2600 dyndbg=+p) on the machine.
