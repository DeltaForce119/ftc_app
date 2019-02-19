package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Hardware map for team Delta Force
 * Created on 21.11.2018 by Tavi
 */

public class Hardware
{
    // INSTANTIATE MOTORS
    public DcMotor motorLF = null; // FRONT LEFT MOTOR
    public DcMotor motorRF = null; // FRONT RIGHT MOTOR
    public DcMotor motorLB = null; // BACK LEFT MOTOR
    public DcMotor motorRB = null; // BACK RIGHT MOTOR
    public DcMotor motorSlide = null; // DRAWER SLIDE MOTOR
    public DcMotor motorCup = null; // CUP MECHANISM MOTOR
    public DcMotor motorClimb = null; // CLIMBING MECHANISM MOTOR
    public DcMotor motorScore = null; // SCORING MECHANISM MOTOR

    // INSTANTIATE SERVOS
    public Servo servoScore = null; // SCORING SERVO
    public CRServo servoIntake = null; // INTAKE SERVO

    // CREATE NEW HardwareMap
    HardwareMap robotMap;

    // DEFINE NEW HardwareMap
    public void init(HardwareMap robotMap) {

        // DEFINE MOTORS
        motorLF = robotMap.get(DcMotor.class,"motorLF");
        motorRF = robotMap.get(DcMotor.class,"motorRF");
        motorLB = robotMap.get(DcMotor.class,"motorLB");
        motorRB = robotMap.get(DcMotor.class,"motorRB");
        motorSlide = robotMap.get(DcMotor.class,"motorSlide");
        motorCup = robotMap.get(DcMotor.class, "motorCup");
        motorClimb = robotMap.get(DcMotor.class, "motorClimb");
        motorScore = robotMap.get(DcMotor.class, "motorScore");

        // SET MOTOR DIRECTION
        motorLF.setDirection(DcMotor.Direction.FORWARD);
        motorRF.setDirection(DcMotor.Direction.REVERSE);
        motorLB.setDirection(DcMotor.Direction.FORWARD);
        motorRB.setDirection(DcMotor.Direction.REVERSE);
        motorSlide.setDirection(DcMotor.Direction.REVERSE);
        motorCup.setDirection(DcMotor.Direction.FORWARD);
        motorClimb.setDirection(DcMotor.Direction.FORWARD);
        motorScore.setDirection(DcMotor.Direction.REVERSE);

        // SET MOTOR POWER
        motorLF.setPower(0);
        motorRF.setPower(0);
        motorLB.setPower(0);
        motorRB.setPower(0);
        motorSlide.setPower(0);
        motorCup.setPower(0);
        motorClimb.setPower(0);
        motorScore.setPower(0);

        // RESET MOTOR ENCODERS
        motorLF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorLB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorRB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorScore.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // SET MOTOR MODE
        motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorCup.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorClimb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorScore.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // SET MOTOR ZeroPowerBehavior
        motorLF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorLB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorRB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorCup.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorClimb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motorScore.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // DEFINE SERVOS
        servoScore = robotMap.get(Servo.class, "servoL");
        servoIntake = robotMap.get(CRServo.class, "servoIntake");

        // SET SERVO DIRECTION
        servoScore.setDirection(Servo.Direction.REVERSE);
        servoIntake.setDirection(DcMotorSimple.Direction.REVERSE);

        // SET SERVO POSITION
        servoScore.setPosition(0);
    }
}