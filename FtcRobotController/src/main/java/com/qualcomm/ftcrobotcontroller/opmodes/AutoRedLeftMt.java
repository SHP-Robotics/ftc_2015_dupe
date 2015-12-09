package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by andrew on Dec 04, 2015 as part of FtcRobotController in ${PACKAGE_NAME}.
 */
public class AutoRedLeftMt extends SHPBase {

    private int v_state = -1;
    private int loops = 0;
    private int dozerEncPos = 0;

    public AutoRedLeftMt() {

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

    } // start

    @Override
    public void loop()

    {
        //----------------------------------------------------------------------
        //
        // State: Initialize (i.e. state_0).
        //
        switch (v_state) {
            case -1:
                dozer.setPower(0.3);
                loops++;
                if (loops > 150) {
                    dozer.setPower(-0.05);
                    //dozer.setPowerFloat();
                    v_state++;
                }
                break;
            //
            // Synchronize the state machine and hardware.
            //
            case 0:
                //
                // Reset the encoders to ensure they are at a known good value.
                //
                reset_drive_encoders();
                // dozer.setMode(DcMotorController.RunMode.RESET_ENCODERS);

                //
                // Transition to the next state when this method is called again.
                //
                v_state++;

                break;
            //
            // Drive forward until the encoders exceed the specified values.
            //
            case 1:
                //
                // Tell the system that motor encoders will be used.  This call MUST
                // be in this state and NOT the previous or the encoders will not
                // work.  It doesn't need to be in subsequent states.
                //
                run_using_encoders();
                //dozer.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

                //
                // Start the drive wheel motors at full power.
                //
                set_drive_power(1.0f, 1.0f);


                //
                // Have the motor shafts turned the required amount?
                //
                // If they haven't, then the op-mode remains in this state (i.e this
                // block will be executed the next time this method is called).
                //
                if (have_drive_encoders_reached(4965, 4965)) {
                    //
                    // Reset the encoders to ensure they are at a known good value.
                    //
                    reset_drive_encoders();

                    //
                    // Stop the motors.
                    //
                    set_drive_power(0.0f, 0.0f);


                    //
                    // Transition to the next state when this method is called
                    // again.
                    //
                    v_state++;
                }
                break;
            //
            // Wait...
            //
            case 2:
                if (have_drive_encoders_reset()) {
                    v_state++;
                }
                break;
            //
            // Turn left until the encoders exceed the specified values.
            //
            case 3:
                run_using_encoders();
                set_drive_power(-1.0f, 1.0f);
                if (have_drive_encoders_reached(1000, 1000)) {
                    reset_drive_encoders();
                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;
            //
            // Wait...
            //
            case 4:
                if (have_drive_encoders_reset()) {
                    v_state++;
                }
                break;
            //
            // Turn right until the encoders exceed the specified values.
            //
            case 5:
                run_using_encoders();
                set_drive_power(1.0f, 1.0f);
                if (have_drive_encoders_reached(7550, 7550)) {
                    reset_drive_encoders();
                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;
            //
            // Wait...
            //
            case 6:
                if (have_drive_encoders_reset()) {
                    v_state++;
                }
                break;

            case 7:
                run_using_encoders();
                set_drive_power(-1.0f, 1.0f);
                if (have_drive_encoders_reached(3650, 3650)) {
                    reset_drive_encoders();
                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;
            //
            // Wait...
            //
            case 8:
                if (have_drive_encoders_reset()) {
                    v_state++;
                }
                break;

            case 9:
                run_using_encoders();
                set_drive_power(1.0f, 1.0f);
                if (have_drive_encoders_reached(2850, 2850)) {
                    reset_drive_encoders();
                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;
            //
            // Wait...
            //
            case 10:
                if (have_drive_encoders_reset()) {
                    v_state++;
                    //brazo.setPosition(0);
                }
                break;

            case 11:
                run_using_encoders();
                set_drive_power(-1.0f, 1.0f);
                if (have_drive_encoders_reached(200, 200)) {
                    reset_drive_encoders();
                    set_drive_power(0.0f, 0.0f);
                    v_state++;
                }
                break;

            case 12:

                if (brazo.getPosition() < 0.1) {
                    brazo.setPosition(0);
                    v_state++;
                } else {
                    if (brazoPos > 0.011) {
                        brazoPos -= 0.003;
                    }
                    brazo.setPosition(brazoPos);
                }

                break;

            //
            // Perform no action - stay in this case until the OpMode is stopped.
            // This method will still be called regardless of the state machine.
            //*/
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
        telemetry.addData("19", "BrazoPos: " + brazoPos);
    } // loop

    public void do_dozer_stuff() {
        loops++;

        if (loops % 17 == 0)
            dozerController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.READ_ONLY);


        if (dozerController.getMotorControllerDeviceMode() == DcMotorController.DeviceMode.READ_ONLY) {
            dozerEncPos = dozer.getCurrentPosition();
            loops = 0;
            dozerController.setMotorControllerDeviceMode(DcMotorController.DeviceMode.WRITE_ONLY);
        }
    }

}
