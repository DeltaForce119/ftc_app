/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="ControlRobot", group="Linear Opmode")
@Disabled
public class ControlRobot extends LinearOpMode {

    // Declarare motoare
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor extindereBrat = null;
    private DcMotor bratRidicare = null;
    private DcMotor bratColectare = null;
    private CRServo servoAspirare = null;
    private CRServo servoRabatare = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initializare din telefon
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");
        extindereBrat = hardwareMap.get(DcMotor.class, "extindereBrat");
        bratRidicare = hardwareMap.get(DcMotor.class, "bratRidicare");
        bratColectare = hardwareMap.get(DcMotor.class, "bratColectare");
        servoAspirare = hardwareMap.get(CRServo.class, "servoAspirare");
        servoRabatare = hardwareMap.get(CRServo.class, "servoRabatare");

        // Setare directie
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        extindereBrat.setDirection(DcMotorSimple.Direction.FORWARD);
        bratRidicare.setDirection(DcMotorSimple.Direction.FORWARD);
        bratColectare.setDirection(DcMotorSimple.Direction.FORWARD);

        // Motoare la puterea zero
        extindereBrat.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bratRidicare.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bratColectare.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            // Declarare variabile
            double leftPower;
            double rightPower;
            int extindereMaxima = 50000;
            int ridicareMaxima = -50000;
            int colectareMaxima = 50000;

            // Formule pentru a conduce
            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            // Trimite putere motoarelor
            leftDrive.setPower(leftPower);
            rightDrive.setPower(rightPower);

            if(gamepad2.left_stick_x < 1 && extindereBrat.getCurrentPosition() < extindereMaxima)
                extindereBrat.setPower(0.2);
            else if(gamepad2.left_stick_x > 1 && extindereBrat.getCurrentPosition() > 10)
                extindereBrat.setPower(-0.2);
            else extindereBrat.setPower(0);

            if(gamepad2.right_stick_x < 1 && bratRidicare.getCurrentPosition() > ridicareMaxima)
                bratRidicare.setPower(-0.2);
            else if(gamepad2.right_stick_x > 1 && bratRidicare.getCurrentPosition() < -10)
                bratRidicare.setPower(0.2);
            else bratRidicare.setPower(0);

            if(gamepad2.right_trigger > 0 && bratColectare.getCurrentPosition() < colectareMaxima)
                bratColectare.setPower(0.2);
            else if(gamepad2.left_trigger > 0 && bratColectare.getCurrentPosition() > 10)
                bratColectare.setPower(-0.2);
            else bratColectare.setPower(0);

            if(gamepad1.a)
                servoAspirare.setPower(-0.5);
            if(gamepad1.b)
                servoAspirare.setPower(0.5);
            else servoAspirare.setPower(0);

            if(gamepad1.x)
                servoRabatare.setPower(-0.5);
            else if(gamepad1.y)
                servoRabatare.setPower(0.5);
            else servoRabatare.setPower(0);

            // Trimite date driverilor
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.addData("Encoder", "bratExtindere" + extindereBrat.getCurrentPosition());
            telemetry.addData("Encoder", "bratRidicare" + bratRidicare.getCurrentPosition());
            telemetry.addData("Encoder", "bratColectare" + bratColectare.getCurrentPosition());
            telemetry.update();
        }
    }
}