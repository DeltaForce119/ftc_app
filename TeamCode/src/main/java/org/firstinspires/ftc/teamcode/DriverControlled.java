package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

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
    static final double slideSpeed = 0.2; // Linear slide motor speed
    static final double climbSpeed = 0.2; // Climbing mechanism motor speed
    static final double scoreSpeed = 0.2; // Scoring mechanism motor speed
    double cupSpeed = 0.3; // Cup motor speed
    double intakeSpeed = 0.7; // Mineral intake servo speed
    double servoScore = 0.6; // Servo scoring position
    double servoRetract = 0; // Servo retracted position
    double fl; double fr; double bl; double br; // Variables used for drive

    @Override
    public void runOpMode() {
        map.init(hardwareMap); // Initialize hardware map

        // Wait for the game to start
        waitForStart();
        runtime.reset();

        // Run until driver presses STOP
        while(opModeIsActive()){

            if(map.motorCup.getCurrentPosition() <= -300)
                map.motorCup.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            else map.motorCup.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //-//-----------\\-\\
            //-// GAMEPAD 1 \\-\\
            //-//-----------\\-\\

            // Collect minerals into cup
            if(gamepad1.a) // If "A" is pressed, collect minerals into cup
                map.servoIntake.setPower(-intakeSpeed);
            else if(gamepad1.b) // If "B" is pressed, eject minerals from cup
                map.servoIntake.setPower(intakeSpeed);
            else map.servoIntake.setPower(0);

            // Move climb mechanism
            if(gamepad1.dpad_left){
                if(map.motorClimb.getCurrentPosition() <= 2880)
                    map.motorClimb.setPower(climbSpeed);
                else map.motorClimb.setPower(0);
            }else if(gamepad1.dpad_right){
                if(map.motorClimb.getCurrentPosition() >= 50)
                    map.motorClimb.setPower(-climbSpeed);
                else map.motorClimb.setPower(0);
            }else map.motorClimb.setPower(0);

            // Move linear slides
            if(gamepad1.right_trigger > 0) {
                if(map.motorSlide.getCurrentPosition() <= 3800){
                    map.motorSlide.setPower(gamepad1.right_trigger);
                }
            } else if(gamepad1.left_trigger > 0){
                if(map.motorSlide.getCurrentPosition() >= 100){
                    map.motorSlide.setPower(-gamepad1.left_trigger);
                }
            }else map.motorSlide.setPower(0);

            //-//-----------\\-\\
            //-// GAMEPAD 2 \\-\\
            //-//-----------\\-\\

            // Move collection cup
            if(gamepad1.right_bumper){
                if(map.motorCup.getCurrentPosition() > -600)
                    map.motorCup.setPower(-cupSpeed);
                else map.motorCup.setPower(0);
            }else if(gamepad1.left_bumper){
                if(map.motorCup.getCurrentPosition() < -50)
                    map.motorCup.setPower(cupSpeed);
                else map.motorCup.setPower(0);
            }else if(gamepad1.y && map.motorCup.getCurrentPosition() <= - 300)
                map.motorCup.setPower(0.05);
            else if(gamepad1.y && map.motorCup.getCurrentPosition() >= - 200)
                map.motorCup.setPower(-0.05);
            else map.motorCup.setPower(0);

            // Move scoring arm
            if(gamepad1.dpad_up || gamepad2.dpad_up){
                if(map.motorScore.getCurrentPosition() <= 5000)
                    map.motorScore.setPower(scoreSpeed);
            }else if(gamepad1.dpad_down || gamepad2.dpad_down){
                if(map.motorScore.getCurrentPosition() >= 50)
                    map.motorScore.setPower(-scoreSpeed);
            }else map.motorScore.setPower(0);

            // Move servos
            if((gamepad2.x || gamepad1.x) && map.motorScore.getCurrentPosition() > 1000){
                map.servoScore.setPosition(servoScore);
            }else{ // Else, move them to initial position
                map.servoScore.setPosition(servoRetract);
            }

            //-//-----------\\-\\
            //-//   DRIVE   \\-\\
            //-//-----------\\-\\

            fl = -gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x;
            fr = -gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x;
            bl = -gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x;
            br = -gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x;

            map.motorLF.setPower(fl);
            map.motorRF.setPower(fr);
            map.motorLB.setPower(bl);
            map.motorRB.setPower(br);

            //-//-------------------\\-\\
            //-// TELEMETRY & OTHER \\-\\
            //-//-------------------\\-\\

            telemetry.addData(">", "Run Time: " + runtime.toString());
            telemetry.addData(">", "ClimbMech_Position: " + map.motorClimb.getCurrentPosition());
            telemetry.addData(">", "Slide_Position: " + map.motorSlide.getCurrentPosition());
            telemetry.addData(">", "Cup_Position: " + map.motorCup.getCurrentPosition());
            telemetry.addData(">", "ScoringMech_Position: " + map.motorScore.getCurrentPosition());
            telemetry.addData("Motors", "LF (%.2f), RF (%.2f), LB (%.2f), RB (%.2f)", fl, fr, bl, br);
            telemetry.update();
        }
    }
}