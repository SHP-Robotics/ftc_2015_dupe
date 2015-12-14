package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by andrew on Dec 11, 2015 as part of FtcRobotController in com.qualcomm.ftcrobotcontroller.opmodes.
 */
public class LightAutoTest extends SHPBase {

    private int v_state = 0;
    private int loops = 0;
    private int dozerEncPos = 0;

    public LightAutoTest() {

    }

    @Override
    public void init() {
        super.init();
        //dozerController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);

    }


    /**
     * Perform any actions that are necessary when the OpMode is enabled.
     * <p/>
     * The system calls this member once when the OpMode is enabled.
     */
    @Override
    public void start()

    {
        //
        // Call the PushBotHardware (super/base class) start method.
        //
        super.start();

        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_drive_encoders();
        brazoPos = 1;
        light.enableLed(true);

    } // start

    @Override
    public void loop()

    {
        //----------------------------------------------------------------------
        //
        // State: Initialize (i.e. state_0).
        //
        switch (v_state) {
            case 0:
                run_using_encoders();
                set_drive_power(0.3f, 0.3f);
                if (light.getLightDetected() > 0.25 && light.getLightDetected() < 0.35) {
                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;

            default:
                //
                // The autonomous actions have been accomplished (i.e. the state has
                // transitioned into its final state.
                //
                break;
        }

        //do_dozer_stuff();

        //
        // Send telemetry data to the driver station.
        //
        update_telemetry(); // Update common telemetry
        telemetry.addData("18", "State: " + v_state);
    } // loop



}
