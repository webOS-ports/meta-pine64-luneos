# MESA_EGL_NO_X11_HEADERS was renamed to EGL_NO_X11 in:
# https://github.com/mesa3d/mesa/commit/6202a13b71e18dc31ba7e2f4ea915b67eacc1ddb

# Otherwise there is conflict between None defined in Xlib.h and
# qtdeclarative's /usr/include/qt5/QtQuick/qsgtexture.h:59
# see http://lists.openembedded.org/pipermail/openembedded-core/2015-June/106351.html
# for details
CXXFLAGS_append_pinephone = " -DEGL_NO_X11=1 "
