package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by andrew on 9/17/15.
 */
public class LegacyTestOp extends OpMode {
    public LegacyTestOp() {
    }

    DcMotor test;

    public void init() {
        test = hardwareMap.dcMotor.get("motor_5");
    }

    public void loop() {
        test.setPower(gamepad1.left_stick_y);
    }
}
