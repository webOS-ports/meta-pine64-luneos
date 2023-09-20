# Because meta-pine64-luneos/recipes-graphics/lvgl/lv-drivers_%.bbappend makes lv-drivers MACHINE_ARCH as well
PACKAGE_ARCH = "${MACHINE_ARCH}"
