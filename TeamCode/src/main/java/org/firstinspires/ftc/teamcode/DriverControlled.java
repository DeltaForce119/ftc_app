package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * DriverControlled for team Delta Force
 * Created on 21.11.2018 by Tavi
 */

@TeleOp(name="DriverControlled", group="DF")

public class DriverControlled extends LinearOpMode {

    // DEFINE ROBOT'S HARDWARE
    Hardware map = new Hardware();

    // INSTANTIATE AND DEFINE VARIABLES
    ElapsedTime runtime = new ElapsedTime();
    static final double slideSpeed = 0.8; // Linear slide motor speed
    static final double climbSpeed = 1; // Climbing mechanism motor speed
    static final double scoreSpeed = 0.8; // Scoring mechanism motor speed
    double cupSpeed = 0.3; // Cup motor speed
    double intakeSpeed = 1; // Mineral intake servo speed
    double fl; double fr; double bl; double br; // Variables used for drive
    double LF; double RF; double LB; double RB;

    @Override
    public void runOpMode() {
        map.init(hardwareMap); // Initialize hardware map
        resetEncoders();

        // Wait for the game to start
        waitForStart();
        runtime.reset();

        // Run until driver presses STOP
        while(opModeIsActive()){

            if(map.motorCup.getCurrentPosition() <= -400)
                map.motorCup.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            else map.motorCup.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //-//-----------\\-\\
            //-// GAMEPAD 1 \\-\\
            //-//-----------\\-\\

            // Collect minerals into cup
            if(gamepad1.a){ // If "A" is pressed, collect minerals into cup
                map.servoIntakeL.setPower(intakeSpeed);
                map.servoIntakeR.setPower(intakeSpeed);
            }else if(gamepad1.b) { // If "B" is pressed, eject minerals from cup
                map.servoIntakeL.setPower(-intakeSpeed);
                map.servoIntakeR.setPower(-intakeSpeed);
            }else if(gamepad1.left_stick_button){
                map.servoIntakeL.setPower(0);
                map.servoIntakeR.setPower(0);
            }

            // Move climb mechanism
            if(gamepad1.dpad_left || gamepad2.dpad_left){
                if(map.motorClimb.getCurrentPosition() >= -5050)
                    map.motorClimb.setPower(-climbSpeed);
                else map.motorClimb.setPower(0);
            }else if(gamepad1.dpad_right || gamepad2.dpad_right){
                if(map.motorClimb.getCurrentPosition() <= -10)
                    map.motorClimb.setPower(climbSpeed);
                else map.motorClimb.setPower(0);
            }else map.motorClimb.setPower(0);

            // Move linear slides
            if(gamepad1.right_trigger > 0) {
                if(map.motorSlide.getCurrentPosition() >= -1380){
                    map.motorSlide.setPower(-slideSpeed);
                }else map.motorSlide.setPower(0);
            }else if(gamepad1.left_trigger > 0){
                if(map.motorSlide.getCurrentPosition() <= 0){
                    map.motorSlide.setPower(1);
                }else map.motorSlide.setPower(0);
            }else map.motorSlide.setPower(0);

            //-//-----------\\-\\
            //-// GAMEPAD 2 \\-\\
            //-//-----------\\-\\

            // Move collection cup
            if(gamepad1.right_bumper || gamepad2.right_bumper){
                if(map.motorCup.getCurrentPosition() > -600)
                    map.motorCup.setPower(-cupSpeed);
                else map.motorCup.setPower(0);
            }else if(gamepad1.left_bumper || gamepad2.left_bumper){
                if(map.motorCup.getCurrentPosition() < -50)
                    map.motorCup.setPower(0.5);
                else map.motorCup.setPower(0);
            }else if((gamepad1.y || gamepad2.y) && map.motorCup.getCurrentPosition() <= - 300)
                map.motorCup.setPower(0.05);
            else if((gamepad1.y || gamepad2.y) && map.motorCup.getCurrentPosition() >= - 200)
                map.motorCup.setPower(-0.05);
            else map.motorCup.setPower(0);

            // Move scoring arm
            if(gamepad1.dpad_up || gamepad2.dpad_up){
                if(map.motorScore.getCurrentPosition() >= -2600 && map.motorCup.getCurrentPosition() < -160)
                    map.motorScore.setPower(-scoreSpeed);
                else map.motorScore.setPower(0);
            }else if(gamepad1.dpad_down || gamepad2.dpad_down){
                if(map.motorScore.getCurrentPosition() <= -1 && map.motorCup.getCurrentPosition() < -160)
                    map.motorScore.setPower(scoreSpeed);
                else map.motorScore.setPower(0);
            }else map.motorScore.setPower(0);

            // Move servo
            if(gamepad2.a || gamepad1.x)
                map.servoScore.setPower(-0.4);
            else if(gamepad2.b)
                map.servoScore.setPower(0.4);
            else if(gamepad2.x)
                map.servoScore.setPower(-0.7);
            else map.servoScore.setPower(0);

            //-//-----------\\-\\
            //-//   DRIVE   \\-\\
            //-//-----------\\-\\

            fl = -gamepad1.left_stick_y + gamepad1.right_stick_x + gamepad1.left_stick_x;
            fr = -gamepad1.left_stick_y - gamepad1.right_stick_x - gamepad1.left_stick_x;
            bl = -gamepad1.left_stick_y + gamepad1.right_stick_x - gamepad1.left_stick_x;
            br = -gamepad1.left_stick_y - gamepad1.right_stick_x + gamepad1.left_stick_x;

            /*
            LF = Range.clip(fl,-1, 1);
            RF = Range.clip(fr,-1, 1);
            LB = Range.clip(bl,-1, 1);
            RB = Range.clip(br,-1, 1);
            */

            map.motorLF.setPower(fl);
            map.motorRF.setPower(fr);
            map.motorLB.setPower(bl);
            map.motorRB.setPower(br);

            //-//-------------------\\-\\
            //-// TELEMETRY & OTHER \\-\\
            //-//-------------------\\-\\

            telemetry.addData(">", "ClimbMech_Position: " + map.motorClimb.getCurrentPosition());
            telemetry.addData(">", "Slide_Position: " + map.motorSlide.getCurrentPosition());
            telemetry.addData(">", "Cup_Position: " + map.motorCup.getCurrentPosition());
            telemetry.addData(">", "ScoringMech_Position: " + map.motorScore.getCurrentPosition());
            telemetry.addData("Motors", "LF (%.2f), RF (%.2f), LB (%.2f), RB (%.2f)", fl, fr, bl, br);
            telemetry.addData(">", "LF: " + map.motorLF.getCurrentPosition());
            telemetry.addData(">", "RF: " + map.motorRF.getCurrentPosition());
            telemetry.addData(">", "LB: " + map.motorLB.getCurrentPosition());
            telemetry.addData(">", "RB: " + map.motorRB.getCurrentPosition());

            telemetry.update();
        }
    }

    public void resetEncoders(){
        // RESET MOTOR ENCODERS
        map.motorLF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorRF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorLB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorRB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorScore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorClimb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorCup.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // SET RUN MODE
        map.motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorCup.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorClimb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        map.motorScore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}