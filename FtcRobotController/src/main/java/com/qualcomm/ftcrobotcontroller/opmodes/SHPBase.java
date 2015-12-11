/* Copyright (c) 2014 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
public class SHPBase extends OpMode {


    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor leftBack, rightBack;
    DcMotor dozer, output;
    DcMotor tapeMeasure, tape2;
    LightSensor light;

    DcMotorController dozerController;

    Servo zipLineGetter, leftTape, brazo;

    float zipPos;

    float leftTapepos, brazoPos;


    /**
     * Constructor
     */
    public SHPBase() {

    }

    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {


		/*
         * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        leftBack = hardwareMap.dcMotor.get("motor_3");
        rightBack = hardwareMap.dcMotor.get("motor_4");
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        dozer = hardwareMap.dcMotor.get("motor_5");
        dozer.setDirection(DcMotor.Direction.REVERSE);
        output = hardwareMap.dcMotor.get("motor_6");
        tapeMeasure = hardwareMap.dcMotor.get("motor_7");
        tape2 = hardwareMap.dcMotor.get("motor_8");

        light = hardwareMap.lightSensor.get("light_1");

        dozerController = hardwareMap.dcMotorController.get("mc_1");

        zipLineGetter = hardwareMap.servo.get("servo_1");
        brazo = hardwareMap.servo.get("servo_2");
        leftTape = hardwareMap.servo.get("servo_3");

    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }


    //--------------------------------------------------------------------------
    //
    // a_left_drive_power
    //

    /**
     * Access the left drive motor's power level.
     */
    double a_left_drive_power() {
        double l_return = 0.0;

        if (motorLeft != null) {
            l_return = motorLeft.getPower();
        }

        return l_return;

    } // a_left_drive_power

    //--------------------------------------------------------------------------
    //
    // a_right_drive_power
    //

    /**
     * Access the right drive motor's power level.
     */
    double a_right_drive_power() {
        double l_return = 0.0;

        if (motorRight != null) {
            l_return = motorRight.getPower();
        }

        return l_return;

    } // a_right_drive_power

    //--------------------------------------------------------------------------
    //
    // set_drive_power
    //

    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    void set_drive_power(double p_left_power, double p_right_power)

    {
        if (motorLeft != null) {
            motorLeft.setPower(p_left_power);
        }
        if (motorRight != null) {
            motorRight.setPower(p_right_power);
        }
        if (leftBack != null) {
            leftBack.setPower(p_left_power);
        }
        if (rightBack != null) {
            rightBack.setPower(p_right_power);
        }

    } // set_drive_power

    //--------------------------------------------------------------------------
    //
    // run_using_left_drive_encoder
    //

    /**
     * Set the left drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_using_left_drive_encoder()

    {
        if (motorLeft != null) {
            motorLeft.setMode
                    (DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    } // run_using_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_using_right_drive_encoder
    //

    /**
     * Set the right drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_using_right_drive_encoder()

    {
        if (motorRight != null) {
            motorRight.setMode
                    (DcMotorController.RunMode.RUN_USING_ENCODERS
                    );
        }

    } // run_using_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_using_encoders
    //

    /**
     * Set both drive wheel encoders to run, if the mode is appropriate.
     */
    public void run_using_encoders()

    {
        //
        // Call other members to perform the action on both motors.
        //
        run_using_left_drive_encoder();
        run_using_right_drive_encoder();

    } // run_using_encoders

    //--------------------------------------------------------------------------
    //
    // run_without_left_drive_encoder
    //

    /**
     * Set the left drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_without_left_drive_encoder()

    {
        if (motorLeft != null) {
            if (motorLeft.getMode() ==
                    DcMotorController.RunMode.RESET_ENCODERS) {
                motorLeft.setMode
                        (DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    } // run_without_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_without_right_drive_encoder
    //

    /**
     * Set the right drive wheel encoder to run, if the mode is appropriate.
     */
    public void run_without_right_drive_encoder()

    {
        if (motorRight != null) {
            if (motorRight.getMode() ==
                    DcMotorController.RunMode.RESET_ENCODERS) {
                motorRight.setMode
                        (DcMotorController.RunMode.RUN_WITHOUT_ENCODERS
                        );
            }
        }

    } // run_without_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // run_without_drive_encoders
    //

    /**
     * Set both drive wheel encoders to run, if the mode is appropriate.
     */
    public void run_without_drive_encoders()

    {
        //
        // Call other members to perform the action on both motors.
        //
        run_without_left_drive_encoder();
        run_without_right_drive_encoder();

    } // run_without_drive_encoders

    //--------------------------------------------------------------------------
    //
    // reset_left_drive_encoder
    //

    /**
     * Reset the left drive wheel encoder.
     */
    public void reset_left_drive_encoder()

    {
        if (motorLeft != null) {
            motorLeft.setMode
                    (DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } // reset_left_drive_encoder

    //--------------------------------------------------------------------------
    //
    // reset_right_drive_encoder
    //

    /**
     * Reset the right drive wheel encoder.
     */
    public void reset_right_drive_encoder()

    {
        if (motorRight != null) {
            motorRight.setMode
                    (DcMotorController.RunMode.RESET_ENCODERS
                    );
        }

    } // reset_right_drive_encoder

    //--------------------------------------------------------------------------
    //
    // reset_drive_encoders
    //

    /**
     * Reset both drive wheel encoders.
     */
    public void reset_drive_encoders()

    {
        //
        // Reset the motor encoders on the drive wheels.
        //
        reset_left_drive_encoder();
        reset_right_drive_encoder();

    } // reset_drive_encoders

    //--------------------------------------------------------------------------
    //
    // a_left_encoder_count
    //

    /**
     * Access the left encoder's count.
     */
    int a_left_encoder_count() {
        int l_return = 0;

        if (motorLeft != null) {
            l_return = motorLeft.getCurrentPosition();
        }

        return l_return;

    } // a_left_encoder_count

    //--------------------------------------------------------------------------
    //
    // a_right_encoder_count
    //

    /**
     * Access the right encoder's count.
     */
    int a_right_encoder_count()

    {
        int l_return = 0;

        if (motorRight != null) {
            l_return = motorRight.getCurrentPosition();
        }

        return l_return;

    } // a_right_encoder_count

    //--------------------------------------------------------------------------
    //
    // has_left_drive_encoder_reached
    //

    /**
     * Indicate whether the left drive motor's encoder has reached a value.
     */
    boolean has_left_drive_encoder_reached(double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (motorLeft != null) {
            //
            // Has the encoder reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs(motorLeft.getCurrentPosition()) > p_count) {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reached

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reached
    //

    /**
     * Indicate whether the right drive motor's encoder has reached a value.
     */
    boolean has_right_drive_encoder_reached(double p_count)

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        if (motorRight != null) {
            //
            // Have the encoders reached the specified values?
            //
            // TODO Implement stall code using these variables.
            //
            if (Math.abs(motorRight.getCurrentPosition()) > p_count) {
                //
                // Set the status to a positive indication.
                //
                l_return = true;
            }
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reached

    //--------------------------------------------------------------------------
    //
    // have_drive_encoders_reached
    //

    /**
     * Indicate whether the drive motors' encoders have reached a value.
     */
    boolean have_drive_encoders_reached
    (double p_left_count
            , double p_right_count
    )

    {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached the specified values?
        //
        if (has_left_drive_encoder_reached(p_left_count) &&
                has_right_drive_encoder_reached(p_right_count)) {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_encoders_reached

    //--------------------------------------------------------------------------
    //
    // drive_using_encoders
    //

    /**
     * Indicate whether the drive motors' encoders have reached a value.
     */
    boolean drive_using_encoders
    (double p_left_power
            , double p_right_power
            , double p_left_count
            , double p_right_count
    )

    {
        //
        // Assume the encoders have not reached the limit.
        //
        boolean l_return = false;

        //
        // Tell the system that motor encoders will be used.
        //
        run_using_encoders();

        //
        // Start the drive wheel motors at full power.
        //
        set_drive_power(p_left_power, p_right_power);

        //
        // Have the motor shafts turned the required amount?
        //
        // If they haven't, then the op-mode remains in this state (i.e this
        // block will be executed the next time this method is called).
        //
        if (have_drive_encoders_reached(p_left_count, p_right_count)) {
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
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // drive_using_encoders

    //--------------------------------------------------------------------------
    //
    // has_left_drive_encoder_reset
    //

    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_left_drive_encoder_reset() {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the left encoder reached zero?
        //
        if (a_left_encoder_count() == 0) {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_left_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // has_right_drive_encoder_reset
    //

    /**
     * Indicate whether the left drive encoder has been completely reset.
     */
    boolean has_right_drive_encoder_reset() {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Has the right encoder reached zero?
        //
        if (a_right_encoder_count() == 0) {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // has_right_drive_encoder_reset

    //--------------------------------------------------------------------------
    //
    // have_drive_encoders_reset
    //

    /**
     * Indicate whether the encoders have been completely reset.
     */
    boolean have_drive_encoders_reset() {
        //
        // Assume failure.
        //
        boolean l_return = false;

        //
        // Have the encoders reached zero?
        //
        if (has_left_drive_encoder_reset() && has_right_drive_encoder_reset()) {
            //
            // Set the status to a positive indication.
            //
            l_return = true;
        }

        //
        // Return the status.
        //
        return l_return;

    } // have_drive_encoders_reset

    public void update_telemetry()

    {

        //
        // Send telemetry data to the driver station.
        //
        telemetry.addData
                ("01"
                        , "Left Drive: "
                                + a_left_drive_power()
                                + ", "
                                + a_left_encoder_count()
                );
        telemetry.addData
                ("02"
                        , "Right Drive: "
                                + a_right_drive_power()
                                + ", "
                                + a_right_encoder_count()
                );

    } // update_telemetry

    //--------------------------------------------------------------------------
    //
    // update_gamepad_telemetry
    //

    /**
     * Update the telemetry with current gamepad readings.
     */
    public void update_gamepad_telemetry()

    {
        //
        // Send telemetry data concerning gamepads to the driver station.
        //
        telemetry.addData("05", "GP1 Left: " + -gamepad1.left_stick_y);
        telemetry.addData("06", "GP1 Right: " + -gamepad1.right_stick_y);
        telemetry.addData("07", "GP2 Left: " + -gamepad2.left_stick_y);
        telemetry.addData("08", "GP2 X: " + gamepad2.x);
        telemetry.addData("09", "GP2 Y: " + gamepad2.y);
        telemetry.addData("10", "GP1 LT: " + gamepad1.left_trigger);
        telemetry.addData("11", "GP1 RT: " + gamepad1.right_trigger);

    } // update_gamepad_telemetry

    //--------------------------------------------------------------------------
    //
    // set_first_message
    //

    /**
     * Update the telemetry's first message with the specified message.
     */
    public void set_first_message(String p_message)

    {
        telemetry.addData("00", p_message);

    } // set_first_message

    //--------------------------------------------------------------------------
    //
    // set_error_message
    //

    /**
     * Update the telemetry's first message to indicate an error.
     */
    public void set_error_message(String p_message)

    {
        set_first_message("ERROR: " + p_message);

    } // set_error_message


}
