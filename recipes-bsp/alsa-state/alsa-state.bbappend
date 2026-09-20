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

# The PinePhone Pro needs the same treatment, for the same reason.
#
# Its rt5640 powers up with the whole output path muted (8 of 102 controls on)
# and nothing unmutes it: alsa-state was not installed on this machine at all,
# so /var/lib/alsa/asound.state did not exist and the ExecStartPre above skipped
# the restore silently. PulseAudio still loads pcm_output and reports it RUNNING
# as the default sink, so the failure looks like nothing at all - no error, just
# silence.
#
# The board is wired like the PineTab2:
#
#   Internal Earpiece    <- SPOLP / SPOLN        codec SPEAKER outs -> EARPIECE
#   Speaker Amp INL/INR  <- HPOL / HPOR          codec HEADPHONE outs -> amp
#   Internal Speaker     <- Speaker Amp OUTL/OUTR
#
# so the loudspeaker hangs off the HEADPHONE outputs via the
# simple-audio-amplifier, and the codec's speaker outputs drive the earpiece.
# Enabling only the SPK/SPO path powers the earpiece and leaves the amplifier
# dark - "Internal Speaker: Off in 0 out 1", every mixer control looking fine.
#
# pinephonepro/asound.state was captured with alsactl from this device with the
# loudspeaker confirmed working.

# umi owns /etc/asound.conf - it is the one that matters, routing the default
# pcm at PulseAudio and defining the PseudoMixer softvol slaves. alsa-state's
# copy is the single comment line "# Global alsa-lib configuration", and with
# both packages in the image opkg fails the whole rootfs:
#
#   error: check_data_file_clashes: Package libumi1 wants to install file
#   /etc/asound.conf. But that file is already provided by package alsa-state
#
# libumi1 is in every image, so drop the file here instead. Without sysvinit in
# DISTRO_FEATURES the init script is not installed either, which leaves the
# alsa-state package with no files at all - and an empty package is not emitted,
# so a MACHINE_EXTRA_RRECOMMENDS on it would resolve to nothing and take the
# alsa-states state file down with it, silently. ALLOW_EMPTY keeps the package
# so it can go on carrying the dependencies that are the point of installing it:
# alsa-utils-alsactl (RDEPENDS) and alsa-states (RRECOMMENDS, the state file).
do_install:append() {
    rm -f ${D}${sysconfdir}/asound.conf
    # do_install created ${sysconfdir} for it; without sysvinit nothing else
    # lands there, and an unshipped directory is a fatal installed-vs-shipped QA
    # error.
    rmdir --ignore-fail-on-non-empty ${D}${sysconfdir}
}

FILES:${PN} = "${sysconfdir}/init.d"
CONFFILES:${PN} = ""
ALLOW_EMPTY:${PN} = "1"
