# Because meta-pine64-luneos/recipes-graphics/lvgl/lvgl_%.bbappend makes lvgl MACHINE_ARCH as well
PACKAGE_ARCH = "${MACHINE_ARCH}"
