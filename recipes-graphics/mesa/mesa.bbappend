# to debug some issues
# EXTRA_OEMESON:append:pinephone = " --buildtype=debug "

# enable both lima and panfrost to be able to reuse mesa sstate between pp and ppp
# meta-rockchip is enabling panfrost for rk3399:
# mesa_%.bbappend:PACKAGECONFIG:append:rk3399 = " kmsro panfrost"
PACKAGECONFIG:append = " panfrost lima libclc"

# mesa-native has to build the CLC compiler itself (libclc sets -Dmesa-clc=enabled
# there), and mesa refuses that with "Feature llvm cannot be disabled: CLC requires
# LLVM" unless -Dllvm=enabled, which only PACKAGECONFIG[gallium-llvm] sets.
# meta-luneui adds gallium-llvm for qemu machines only, so mesa-native fails
# do_configure for every other machine without this.
#
# Keep it class-native: the target build uses -Dmesa-clc=system and just consumes
# the mesa-clc that mesa-native installs, so it does not need llvm itself. Adding
# gallium-llvm unconditionally instead would make libgallium depend on llvm at
# runtime and pull LLVM into every device image, which is not what we shipped on
# scarthgap.
PACKAGECONFIG:append:class-native = " gallium-llvm"
