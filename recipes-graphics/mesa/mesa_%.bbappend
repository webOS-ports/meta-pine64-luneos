# to debug some issues
# EXTRA_OEMESON:append:pinephone = " --buildtype=debug "

# enable both lima and panfrost to be able to reuse mesa sstate between pp and ppp
# meta-rockchip is enabling panfrost for rk3399:
# mesa_%.bbappend:PACKAGECONFIG:append:rk3399 = " kmsro panfrost"
PACKAGECONFIG:append:pinephone = " kmsro panfrost lima"
PACKAGECONFIG:append:pinephonepro = " lima"
