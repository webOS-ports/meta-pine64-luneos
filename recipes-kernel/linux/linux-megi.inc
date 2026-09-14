# Common definitions for the Pine64 kernels, all of which are built from megi's
# integration branch.
#
# Hosting note: megous.com is gone - it now 301s to an unrelated domain - and
# xff.cz/git/linux 303s to codeberg.org/megi/linux, which is the canonical home.
# The old sailfish-on-dontbeevil GitHub mirror the PinePhone recipe used stops at
# orange-pi-6.7 and is not usable any more.
#
# megi rebases and force-pushes these branches, so a pinned SRCREV will eventually
# stop being reachable here. Mirror the revision we pin to Herrie82/kernel-megi and
# point MEGI_REPO at that once it is pushed:
#
#     git push github facc87117511221bb61410e3f92ad8579697f736:refs/heads/herrie/LuneOS-7.2
#     MEGI_REPO = "git://github.com/Herrie82/kernel-megi.git;branch=herrie/LuneOS-7.2"
#
# orange-pi-7.2 carries device trees for all three of our boards, so pinephone,
# pinephonepro and pinetab2 share one tree at one SRCREV. Bumping to megi's next
# release should be a change to MEGI_BRANCH and SRCREV_machine here and nothing else.

SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

LINUX_VERSION ?= "7.2"
PV = "${LINUX_VERSION}-git"

# megi's branch tracks 7.2.y but the tree does not always carry a matching
# EXTRAVERSION, so do not let the sanity check fail the build over it.
KERNEL_VERSION_SANITY_SKIP = "1"

MEGI_BRANCH ?= "orange-pi-7.2"
MEGI_REPO ?= "git://codeberg.org/megi/linux;branch=${MEGI_BRANCH}"

# megi's orange-pi-7.2 tip as of 2026-09-03.
SRCREV_machine ?= "facc87117511221bb61410e3f92ad8579697f736"

# yocto-kernel-cache: use the branch matching the kernel version rather than the
# master/yocto-dev the old recipes tracked.
KMETA = "kernel-meta"
LINUX_KMETA_BRANCH ?= "yocto-7.2"
SRCREV_meta ?= "31a9aee38a2827fac9db03afde5bc9fe88b49957"

SRC_URI = " \
    ${MEGI_REPO};protocol=https;name=machine \
    git://git.yoctoproject.org/yocto-kernel-cache;protocol=https;type=kmeta;name=meta;branch=${LINUX_KMETA_BRANCH};destsuffix=${KMETA} \
    file://luneos-crypto.cfg \
    file://luneos-squashfs.cfg \
    file://luneos-waydroid-anbox.cfg \
"

# do_kernel_configcheck runs symbol_why.py, which parses the tree with the
# kconfiglib bundled in kern-tools-native. That copy cannot parse the
# "depends on <sym> if <cond>" form:
#
#     drivers/usb/cdns3/Kconfig:5: error: couldn't parse
#     'depends on USB if !USB_GADGET': extra tokens at end of line
#
# The line is upstream v7.2 (identical in megi's tree) and the kernel's own
# scripts/kconfig accepts it - only kconfiglib does not - so the audit cannot
# run at all on a 7.2 tree and takes the build down with it. Turn it off here
# rather than globally, and drop this once kern-tools-native carries a
# kconfiglib that understands the syntax.
KMETA_AUDIT = ""

# These recipes ship a complete defconfig rather than assembling a config from
# kernel-cache fragments, so do not let a machine's KBUILD_DEFCONFIG override it.
KBUILD_DEFCONFIG = ""

# yaml and dtschema are required for 5.16+ device tree validation. libyaml is
# checked via pkgconfig so it must always be present; the others are wrapped.
DEPENDS += "gmp-native libmpc-native"
DEPENDS += "libyaml-native libyaml yaml-cpp python3-dtschema-wrapper-native"
