package com.qualcomm.ftcrobotcontroller.opmodes;

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

        telemetry.addData("Left Encoder Pos: ", a_left_encoder_count());
        telemetry.addData("Right Encoder Pos: ", a_right_encoder_count());
    }
}
