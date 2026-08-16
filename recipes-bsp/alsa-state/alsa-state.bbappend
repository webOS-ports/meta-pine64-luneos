FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# A PineTab2 boots silent without this.
#
# pulseaudio.service restores the mixer before starting the daemon:
#
#   ExecStartPre=/bin/sh -euc 'if test -x /usr/sbin/alsactl -a -f \
#       /var/lib/alsa/asound.state -a "`cat /var/lib/alsa/asound.state | wc -l`" \
#       -gt 1; then /usr/sbin/alsactl -f /var/lib/alsa/asound.state restore; fi'
#
# and the asound.state alsa-state ships by default is the one-line "# Dummy
# file, do not delete", which that -gt 1 test deliberately skips. So the rk817
# comes up on whatever the driver left in its registers, and whatever any later
# "alsactl store" happens to have written - a value nothing in the image sets on
# purpose. The state restored here is a known-good one captured from a working
# device instead.
#
# Two values matter:
#
#   Playback Mux = HP. Counterintuitive, but correct: the internal speakers hang
#   off a discrete amplifier that is wired to the codec's *headphone* outputs
#   (see the "speaker-amplifier" component under /sys/kernel/debug/asoc, whose
#   INL/INR come from HPOL/HPOR). Selecting SPK routes to a codec speaker output
#   this board does not use, and DAPM then powers the DACs down entirely -
#   /sys/kernel/debug/asoc/rk817_ext/rk817-codec/dapm/DAC\ L reads "Off in 1
#   out 0" and there is no sound, with every mixer control still looking right.
#
#   Master Playback Volume = 255. The scale is -95..0 dB over 0..255, so it is
#   far more aggressive than the percentage suggests: the 25% an earlier state
#   file carried is -71 dB, which is inaudible, not merely quiet. This speaker
#   is weak enough that 0 dB is a sensible ceiling; audiod does the user-facing
#   attenuation on top, so leaving the hardware open gives the volume keys their
#   full range.
#
# Keyed by card name (state.rk817ext, state.HDMI), which matters because the two
# cards do not enumerate in a stable order - rk817ext has been seen as both card
# 0 and card 1 across reboots of the same device.
PACKAGE_ARCH = "${MACHINE_ARCH}"
