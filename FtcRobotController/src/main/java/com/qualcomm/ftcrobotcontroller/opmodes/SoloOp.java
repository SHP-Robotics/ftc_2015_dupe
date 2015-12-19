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
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
public class SoloOp extends OpMode {


    DcMotor motorRight;
    DcMotor motorLeft;
    DcMotor leftBack, rightBack;
    DcMotor dozer, output;
    DcMotor tapeMeasure, tape2;

    Servo zipLineGetter, leftTape, brazo;

    float zipPos;

    float leftTapepos, brazoPos;

    /**
     * Constructor
     */
    public SoloOp() {

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

        motorLeft.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        motorRight.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);


        zipLineGetter = hardwareMap.servo.get("servo_1");
        brazo = hardwareMap.servo.get("servo_2");
        leftTape = hardwareMap.servo.get("servo_3");

        brazoPos = 1;

    }

    public void start() {
        resetStartTime();
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        if (getRuntime() < 120) {
            float throttle = -gamepad1.left_stick_y;
            float direction = gamepad1.left_stick_x;
            float right = throttle - direction;
            float left = throttle + direction;


            if (gamepad1.right_bumper && zipPos > 0.005) {
                zipPos -= 0.005;
            } else if (zipPos < 0.99) {
                zipPos += gamepad1.right_trigger / 200;
            }

            if (gamepad1.left_bumper && leftTapepos > 0.005) {
                leftTapepos -= 0.005;
            } else if (leftTapepos < 0.99) {
                leftTapepos += gamepad1.left_trigger / 200;
            }

            if (brazoPos < 0.05 && gamepad1.right_stick_x > 0) {
                brazoPos -= gamepad1.right_stick_x / 20;
            } else if (brazoPos > 0.95 && gamepad1.right_stick_x < 0) {
                brazoPos -= gamepad1.right_stick_x / 20;
            } else {
                brazoPos -= gamepad1.right_stick_x / 20;
            }

            // clip the right/left values so that the values never exceed +/- 1
            right = Range.clip(right, -1, 1);
            left = Range.clip(left, -1, 1);

            // scale the joystick value to make it easier to control
            // the robot more precisely at slower speeds.
            right = (float) scaleInput(right);
            left = (float) scaleInput(left);

            // write the values to the motors
            motorRight.setPower(right);
            motorLeft.setPower(left);
            rightBack.setPower(right);
            leftBack.setPower(left);

            tapeMeasure.setPower(gamepad1.y ? -1 : gamepad1.a ? 1 : 0);
            tape2.setPower(gamepad1.dpad_up ? 1 : gamepad1.dpad_down ? -1 : 0);

            dozer.setPower(0.85 * gamepad1.right_stick_y);
            output.setPower(gamepad1.dpad_left ? 0.55 : gamepad1.dpad_right ? -0.55 : 0);

            try {
                zipLineGetter.setPosition(1 - zipPos);
                leftTape.setPosition(leftTapepos);
                brazo.setPosition(Range.clip(brazoPos, 0, 1));
            } finally {

            }


            telemetry.addData("Text", "*** Robot Data***");
            telemetry.addData("01 Time remaining: ", 120 - getRuntime());
            telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", left));
            telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        }
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }

}
