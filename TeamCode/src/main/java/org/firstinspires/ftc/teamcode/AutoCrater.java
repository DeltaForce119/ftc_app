package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.vision.MasterVision;
import org.firstinspires.ftc.teamcode.vision.SampleRandomizedPositions;

@Autonomous
public class AutoCrater extends LinearOpMode {

    // DEFINE ROBOT'S HARDWARE
    Hardware map = new Hardware();
    ElapsedTime runtime = new ElapsedTime();
    static final double COUNTS_PER_MOTOR_REV = 1120;    // AndyMark ticks
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;    // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    double driveSpeed = 1.0;
    double rotateSpeed = 0.8;
    double strafeSpeed = 0.8;
    MasterVision vision;
    SampleRandomizedPositions goldPosition;

    @Override
    public void runOpMode() throws InterruptedException {
        map.init(hardwareMap);
        resetEncoders();
        VuforiaLocalizer.Parameters parametersV = new VuforiaLocalizer.Parameters();
        parametersV.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;// recommended camera direction
        parametersV.vuforiaLicenseKey = "Ab8Qtuj/////AAABmYn5G67UzUoIuS1bav0dl4chsJNfEqvmQoLmOp0aAcw562yPmFjcldoEqiuHPgHeWzguCrZGjoVtSGhPNXeoMlDdAhcADTgly9e2I5hVeUM3z0W4+mFGMi3fH+zgePOGBu8UqQpkgSyLFpdYKrBJDDydkmOU69T0BGfhYepkzwibxJ7ZndhW4E/YLgoDlQXWd6uSB8V4VQmhq4QymGG9EgMqtQBLyDLwcU19NKie3U6cbTzezh5kJo2vQkRULZMhsPnGlGk/HpKG6PbcXwQqFMxUjBMJSroWTWCznFGcXc1gHiiASlOoFSrjRXev+wBnkGke8/15ueaZT93tkEbP7vrym/RQPxH7hZ871IqqCzpy";

        vision = new MasterVision(parametersV, hardwareMap, true, MasterVision.TFLiteAlgorithm.INFER_RIGHT);
        vision.init();// enables the camera overlay. this will take a couple of seconds
        vision.enable();// enables the tracking algorithms. this might also take a little time

        // Wait for start
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData(">", "Robot Ready.");
            telemetry.update();
        }

        vision.disable();// disables tracking algorithms. this will free up your phone's processing power for other jobs.

        goldPosition = vision.getTfLite().getLastKnownSampleOrder();

        while(opModeIsActive()){
            telemetry.addData("goldPosition was", goldPosition);// giving feedback

            switch (goldPosition){ // using for things in the autonomous program
                case LEFT:
                    telemetry.addLine("going to the left");
                    unLatch(-1, -22500);
                    strafe(strafeSpeed, 4, 'l');
                    drive(0.1,1);
                    retractClimb();
                    rotate(rotateSpeed, 3, 'l');
                    drive(driveSpeed, 19);
                    drive(driveSpeed, -5);
                    rotate(rotateSpeed, 19, 'r');
                    drive(driveSpeed, -15);
                    rotate(rotateSpeed, 6, 'r');
                    drive(0.8, -7);
                    rotate(rotateSpeed, 15, 'r');
                    drive(driveSpeed, 19);
                    map.servoIntakeR.setPower(-1);
                    map.servoIntakeL.setPower(-1);
                    depositMarker();
                    drive(driveSpeed, -14);
                    rotate(rotateSpeed, 27.5, 'l');
                    drive(driveSpeed, 14);
                    lowerCup();
                    sleep(30000);
                    break;
                case CENTER:
                    telemetry.addLine("going straight");
                    unLatch(-1, -22500);
                    strafe(strafeSpeed, 4, 'l');
                    drive(0.1,1);
                    rotate(rotateSpeed, 3, 'r');
                    retractClimb();
                    drive(driveSpeed, 15);
                    drive(driveSpeed, -3);
                    rotate(rotateSpeed, 11.5, 'r');
                    drive(driveSpeed, -25);
                    rotate(rotateSpeed, 6, 'r');
                    drive(0.8, -7);
                    rotate(rotateSpeed, 15, 'r');
                    drive(driveSpeed, 14.5);
                    map.servoIntakeR.setPower(-1);
                    map.servoIntakeL.setPower(-1);
                    depositMarker();
                    drive(driveSpeed, -13);
                    rotate(1, 27, 'l');
                    drive(driveSpeed, 12);
                    lowerCup();
                    sleep(30000);
                    break;
                case RIGHT:
                    telemetry.addLine("going to the right");
                    unLatch(-1, -22500);
                    strafe(strafeSpeed, 4, 'l');
                    drive(0.1,1);
                    retractClimb();
                    rotate(rotateSpeed, 6, 'r');
                    drive(driveSpeed, 20);
                    drive(driveSpeed, -6);
                    rotate(rotateSpeed, 10, 'r');
                    drive(driveSpeed, -30);
                    rotate(driveSpeed, 6, 'r');
                    drive(0.8, -5);
                    rotate(rotateSpeed, 15, 'r');
                    drive(driveSpeed, 20);
                    map.servoIntakeR.setPower(-1);
                    map.servoIntakeL.setPower(-1);
                    depositMarker();
                    drive(driveSpeed, -14);
                    rotate(rotateSpeed, 29, 'l');
                    drive(driveSpeed, 15);
                    lowerCup();
                    sleep(30000);
                    break;
                case UNKNOWN:
                    telemetry.addLine("staying put");
                    unLatch(-1, -22500);
                    strafe(strafeSpeed, 4, 'l');
                    drive(0.1,1);
                    rotate(rotateSpeed, 3, 'r');
                    retractClimb();
                    drive(driveSpeed, 15);
                    drive(driveSpeed, -3);
                    rotate(rotateSpeed, 11.5, 'r');
                    drive(driveSpeed, -25);
                    rotate(rotateSpeed, 6, 'r');
                    drive(0.8, -7);
                    rotate(rotateSpeed, 15, 'r');
                    drive(driveSpeed, 14.5);
                    map.servoIntakeR.setPower(-1);
                    map.servoIntakeL.setPower(-1);
                    depositMarker();
                    drive(driveSpeed, -13);
                    rotate(1, 27, 'l');
                    drive(driveSpeed, 12);
                    lowerCup();
                    sleep(30000);
                    break;
            }

            telemetry.update();
        }

        vision.shutdown();
    }

    private void drive(double speed, double inches) {
        int newLFtarget;
        int newRFtarget;
        int newLBtarget;
        int newRBtarget;

        if (opModeIsActive()) {

            // Determine new target position
            newLFtarget = map.motorLF.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH);
            newRFtarget = map.motorRF.getCurrentPosition() - (int) (inches * COUNTS_PER_INCH);
            newLBtarget = map.motorLB.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            newRBtarget = map.motorRB.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            map.motorLF.setTargetPosition(newLFtarget);
            map.motorRF.setTargetPosition(newRFtarget);
            map.motorLB.setTargetPosition(newLBtarget);
            map.motorRB.setTargetPosition(newRBtarget);

            // Turn on RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorLB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Start motion
            map.motorLF.setPower(Math.abs(-speed));
            map.motorRF.setPower(Math.abs(-speed));
            map.motorLB.setPower(Math.abs(speed));
            map.motorRB.setPower(Math.abs(speed));

            while (opModeIsActive() && (map.motorLF.isBusy() && map.motorRF.isBusy() && map.motorLB.isBusy() && map.motorRB.isBusy())) {
                // Display it for the driver.
                telemetry.addData(">","Driving to:" + inches);
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
        }
    }

    private void strafe(double speed, double inches, char direction){
        int newLFtarget;
        int newRFtarget;
        int newLBtarget;
        int newRBtarget;

        if (opModeIsActive()){

            if(direction == 'r'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorLB.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }else if(direction == 'l'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorLB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }

            // Turn on RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorLB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Start motion
            map.motorLF.setPower(Math.abs(speed));
            map.motorRF.setPower(Math.abs(speed));
            map.motorLB.setPower(Math.abs(speed));
            map.motorRB.setPower(Math.abs(speed));

            while (opModeIsActive() && (map.motorLF.isBusy() && map.motorRF.isBusy() && map.motorLB.isBusy() && map.motorRB.isBusy())) {
                // Display it for the driver.
                telemetry.addData(">","Strafing: " + inches);
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

            sleep(25);
        }
    }

    private void rotate(double speed, double inches, char direction){
        int newLFtarget;
        int newRFtarget;
        int newLBtarget;
        int newRBtarget;

        if (opModeIsActive()){

            if(direction == 'r'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorLB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }else if(direction == 'l'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorLB.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }

            // Turn on RUN_TO_POSITION
            map.motorLF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRF.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorLB.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorRB.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Start motion
            map.motorLF.setPower(Math.abs(speed));
            map.motorRF.setPower(Math.abs(speed));
            map.motorLB.setPower(Math.abs(speed));
            map.motorRB.setPower(Math.abs(speed));

            while (opModeIsActive() && (map.motorLF.isBusy() && map.motorRF.isBusy() && map.motorLB.isBusy() && map.motorRB.isBusy())) {
                // Display it for the driver.
                telemetry.addData(">","Rotating: " + inches);
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

            sleep(25);
        }
    }

    private void unLatch(double speed, int ticks){
        map.motorClimb.setTargetPosition(ticks);
        map.motorClimb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        map.motorClimb.setPower(Math.abs(speed));
        while(map.motorClimb.isBusy()){
            telemetry.addData("Status: ", "Moving climb mechanism");
            telemetry.update();
        }
        map.motorClimb.setPower(0);
        map.motorClimb.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        sleep(25);
    }

    private void retractClimb(){
        map.motorClimb.setTargetPosition(0);
        map.motorClimb.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        map.motorClimb.setPower(1);
    }

    private void depositMarker(){
        if(opModeIsActive()) {
            map.motorCup.setTargetPosition(-260);
            map.motorCup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorCup.setPower(0.5);
            map.servoIntakeL.setPower(-1);
            map.servoIntakeR.setPower(-1);
            while(opModeIsActive() && map.motorCup.isBusy()){
                telemetry.addData(">","Depositing marker");
                telemetry.update();
            }
            map.motorCup.setTargetPosition(-50);
            map.motorCup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorCup.setPower(0.3);
        }
    }

    private void lowerCup(){
        if(opModeIsActive()){
            map.motorCup.setTargetPosition(-400);
            map.motorCup.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            map.motorCup.setPower(0.3);
            while(opModeIsActive() && map.motorCup.isBusy()){
                telemetry.addData(">","Lowering cup");
                telemetry.update();
            }
            map.motorCup.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            map.motorCup.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
            map.motorCup.setPower(0);
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
    }
}