# MESA_EGL_NO_X11_HEADERS was renamed to EGL_NO_X11 in:
# https://github.com/mesa3d/mesa/commit/6202a13b71e18dc31ba7e2f4ea915b67eacc1ddb

FILESEXTRAPATHS_prepend_pinephone := "${THISDIR}/${BPN}:"
SRC_URI_append_pinephone = "file://0001-dispatch_common.h-define-also-EGL_NO_X11.patch"
