SUMMARY = "Tool for tracing OpenGL, Direct3D, and other graphics APIs"
SECTION = "devel" 
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aeb969185a143c3c25130bc2c3ef9a50" 
PV = "8.0+git${SRCPV}"

DEPENDS = "libpng procps virtual/egl"

SRC_URI = "git://github.com/apitrace/apitrace.git;protocol=https \
           file://0001-Disable-multiarch-support-to-prevent-invalid-install.patch \
           file://0002-Use-GL-headers-from-sysroot.patch \
           file://0003-Exclude-retrace-from-build.patch \
           file://0004-Fix-APIs-conflicting-definitions.patch \
           file://0005-Fix-dlsym-reload.patch \
          "
SRCREV = "6857eccdc4364116c2e4a7817d2dd26ff705f205"

S = "${WORKDIR}/git"

inherit cmake pythonnative

EXTRA_OECMAKE += "-DENABLE_X11=NO -DENABLE_GUI=NO"
