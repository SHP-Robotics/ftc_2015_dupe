package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by andrew on 12/1/15.
 */
public class EncoderReadOp extends SHPBase {

    public EncoderReadOp() {

    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void start() {
        reset_drive_encoders ();
    }

    @Override
    public void loop() {
        run_using_encoders ();
        dozer.setMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        telemetry.addData("Left Encoder Pos: ", a_left_encoder_count());
        telemetry.addData("Right Encoder Pos: ", a_right_encoder_count());
        telemetry.addData("Arm Enc Pos: ", dozer.getCurrentPosition());
    }
}
