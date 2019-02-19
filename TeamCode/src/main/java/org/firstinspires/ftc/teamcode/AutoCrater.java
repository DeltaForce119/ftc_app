package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.vision.MasterVision;
import org.firstinspires.ftc.teamcode.vision.SampleRandomizedPositions;


@Autonomous
//@Disabled
public class AutoCrater extends LinearOpMode{
    // DEFINE ROBOT'S HARDWARE
    Hardware map = new Hardware();
    ElapsedTime runtime = new ElapsedTime();
    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     driveSpeed              = 1.0;
    MasterVision vision;
    SampleRandomizedPositions goldPosition;

    @Override
    public void runOpMode() throws InterruptedException {
        map.init(hardwareMap);
        map.motorCup.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        map.motorClimb.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;// recommended camera direction
        parameters.vuforiaLicenseKey = "Ab8Qtuj/////AAABmYn5G67UzUoIuS1bav0dl4chsJNfEqvmQoLmOp0aAcw562yPmFjcldoEqiuHPgHeWzguCrZGjoVtSGhPNXeoMlDdAhcADTgly9e2I5hVeUM3z0W4+mFGMi3fH+zgePOGBu8UqQpkgSyLFpdYKrBJDDydkmOU69T0BGfhYepkzwibxJ7ZndhW4E/YLgoDlQXWd6uSB8V4VQmhq4QymGG9EgMqtQBLyDLwcU19NKie3U6cbTzezh5kJo2vQkRULZMhsPnGlGk/HpKG6PbcXwQqFMxUjBMJSroWTWCznFGcXc1gHiiASlOoFSrjRXev+wBnkGke8/15ueaZT93tkEbP7vrym/RQPxH7hZ871IqqCzpy";

        vision = new MasterVision(parameters, hardwareMap, false, MasterVision.TFLiteAlgorithm.INFER_RIGHT);
        vision.init();// enables the camera overlay. this will take a couple of seconds

        waitForStart();

        while(opModeIsActive()){
            vision.enable();
            goldPosition = vision.getTfLite().getLastKnownSampleOrder();
            telemetry.addData("goldPosition was", goldPosition);// giving feedback

            switch (goldPosition){ // using for things in the autonomous program
                case LEFT:
                    vision.disable();
                    telemetry.addLine("going to the left");
                    map.motorCup.setTargetPosition(-450);
                    map.motorCup.setPower(0.3);
                    sleep(250);
                    map.motorCup.setPower(0);
                    telemetry.addLine("path complete");
                    sleep(30000);
                    break;
                case CENTER:
                    vision.disable();
                    telemetry.addLine("going straight");
                    telemetry.addLine("path complete");
                    sleep(30000);
                    break;
                case RIGHT:
                    vision.disable();
                    telemetry.addLine("going to the right");
                    telemetry.addLine("path complete");
                    sleep(30000);
                    break;
                case UNKNOWN:
                    telemetry.addLine("staying put");
                    break;
            }
            telemetry.update();
        }

        vision.shutdown();
    }

    public void encoderDrive(double speed, double LFinches, double RFinches, double LBinches, double RBinches){
        int newLFtarget;
        int newRFtarget;
        int newLBtarget;
        int newRBtarget;

        if (opModeIsActive()){

            // Determine new target position
            newLFtarget = map.motorLF.getCurrentPosition() + (int)(LFinches * COUNTS_PER_INCH);
            newRFtarget = map.motorRF.getCurrentPosition() + (int)(RFinches * COUNTS_PER_INCH);
            newLBtarget = map.motorLB.getCurrentPosition() + (int)(LBinches * COUNTS_PER_INCH);
            newRBtarget = map.motorRB.getCurrentPosition() + (int)(RBinches * COUNTS_PER_INCH);
            map.motorLF.setTargetPosition(newLFtarget);
            map.motorRF.setTargetPosition(newRFtarget);
            map.motorLB.setTargetPosition(newLBtarget);
            map.motorRB.setTargetPosition(newRBtarget);

            // Turn on RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorLB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Reset the timeout and start motion
            runtime.reset();
            map.motorLF.setPower(Math.abs(speed));
            map.motorRF.setPower(Math.abs(speed));
            map.motorLB.setPower(Math.abs(speed));
            map.motorRB.setPower(Math.abs(speed));

            while (opModeIsActive() && (map.motorLF.isBusy() && map.motorRF.isBusy() && map.motorLB.isBusy() && map.motorRB.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLFtarget, newRFtarget);
                telemetry.addData("Path2", "Running at %7d :%7d", map.motorLF.getCurrentPosition(), map.motorRF.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            map.motorLF.setPower(0);
            map.motorRF.setPower(0);
            map.motorLB.setPower(0);
            map.motorRB.setPower(0);

            // Turn off RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorLB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorRB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);

        }
    }
}