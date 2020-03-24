#!/bin/sh

function start()
{
	# GPIO35 is PWRKEY
	# GPIO68 is RESET_N
	# GPIO232 is W_DISABLE
	#
	for i in 35 68 232
	do
		[ -e /sys/class/gpio/gpio$i ] && continue
		echo $i > /sys/class/gpio/export || return 1
		echo out > /sys/class/gpio/gpio$i/direction || return 1
	done

	echo 0 > /sys/class/gpio/gpio68/value || return 1
	echo 0 > /sys/class/gpio/gpio232/value || return 1

	( echo 1 > /sys/class/gpio/gpio35/value && sleep 2 && echo 0 > /sys/class/gpio/gpio35/value ) || return 1
}

function setup_modem_audio()
{
    # Current modem routing
    #
    #  1 - Digital PCM
    #  0 - I2S master
    #  0 - Primary mode (short sync)
    #  2 - 512kHz clock (512kHz / 16bit = 32k samples/s)
    #  0 - 16bit linear format
    #  1 - 16k sample/s
    #  1 - 1 slot
    #  1 - map to first slot (the only slot)
    #
    QDAI_CONFIG="1,0,0,2,0,1,1,1"

    DEV=/dev/EG25.AT

    # Read current config
    RET=$(echo "AT+QDAI?" | atinout - $DEV -)

    if echo $RET | grep -q $QDAI_CONFIG
    then
	    echo "Modem audio already configured"
	    exit 0
    fi


    # Modem not configured, we need to send it the digital interface configuration,
    # then reboot it
    RET=$(echo "AT+QDAI=$QDAI_CONFIG" | atinout - $DEV -)

    if echo $RET | grep -q OK
    then
	    echo "Successfully configured modem audio"
    else
	    echo "Failed to set modem audio up: $RET"
	    exit 1
    fi

    # Reset module
    # 1 Set the mode to full functionality (vs 4: no RF, and 1: min functionality)
    # 1 Reset the modem before changing mode (only available with 1 above)
    #
    RET=$(echo "AT+CFUN=1,1" | atinout - $DEV -)

    if echo $RET | grep -q OK
    then
	    echo "Successfully reset the modem to apply audio configuration"
    else
	    echo "Failed to reset the modem to apply audio configuration: $RET"
    fi
}

function stop()
{
	echo 1 > /sys/class/gpio/gpio68/value
	echo 1 > /sys/class/gpio/gpio232/value

	echo 1 > /sys/class/gpio/gpio35/value && sleep 2 && echo 0 > /sys/class/gpio/gpio35/value
}

if [ "$1" = "start" ]; then
  echo "Starting EG25 modem..."
  start
  echo "Setup EG25 modem audio..."
  setup_modem_audio
elif [ "$1" = "stop" ]; then 
  echo "Stopping EG25 modem..."
  stop
fi


