package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by andrew on 9/18/15.
 */
public class TankOp extends OpMode {

    DcMotor left, right, conveyor, grapple, grappleGuide;

    public void init() {
        left = hardwareMap.dcMotor.get("motor_1");
        right = hardwareMap.dcMotor.get("motor_2");
        conveyor = hardwareMap.dcMotor.get("conveyor");
        grapple = hardwareMap.dcMotor.get("winch");
        grappleGuide = hardwareMap.dcMotor.get("grapple");
    }

    public void loop() {
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float rightValue = throttle - direction;
        float leftValue = throttle + direction;

        // clip the right/left values so that the values never exceed +/- 1
        rightValue = Range.clip(rightValue, -1, 1);
        leftValue = Range.clip(leftValue, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        rightValue = (float)scaleInput(rightValue);
        leftValue =  (float)scaleInput(leftValue);

        // write the values to the motors
        right.setPower(rightValue);
        left.setPower(leftValue);
    }

    public void stop() {

    }

    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

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
