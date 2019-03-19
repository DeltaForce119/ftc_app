package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.vision.MasterVision;
import org.firstinspires.ftc.teamcode.vision.SampleRandomizedPositions;

@Autonomous
public class DoubleSampling extends LinearOpMode {

    // DEFINE ROBOT'S HARDWARE
    Hardware map = new Hardware();
    ElapsedTime runtime = new ElapsedTime();
    static final double COUNTS_PER_MOTOR_REV = 383.6;    // Motor ticks
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;    // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    double driveSpeed = 0.3;
    double rotateSpeed = 0.4;
    double strafeSpeed = 0.4;
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
                    unLatch(1, 13200);
                    strafe(strafeSpeed, 6, 'l');
                    drive(0.1,1);
                    retractClimb();
                    sleep(500);
                    rotate(rotateSpeed, 3, 'l');
                    drive(driveSpeed, 18);
                    drive(driveSpeed, -8);
                    rotate(rotateSpeed, 17, 'r');
                    drive(driveSpeed, -17);
                    rotate(rotateSpeed, 8, 'l');
                    strafe(0.2, 10, 'l');
                    strafe(0.2, 1.5, 'r');
                    drive(driveSpeed, -35);
                    depositMarker();
                    strafe(strafeSpeed, 22, 'r');
                    strafe(strafeSpeed, 21, 'l');
                    drive(driveSpeed, 24);
                    strafe(0.2, 6, 'l');
                    drive(driveSpeed, 24);
                    lowerCup();
                    sleep(30000);
                    break;
                case CENTER:
                    telemetry.addLine("going straight");
                    unLatch(1, 13200);
                    strafe(strafeSpeed, 4, 'l');
                    drive(0.1,1);
                    rotate(rotateSpeed, 1.5, 'r');
                    retractClimb();
                    drive(driveSpeed, 15);
                    drive(driveSpeed, -4);
                    rotate(rotateSpeed, 11.5, 'r');
                    drive(driveSpeed, -25);
                    rotate(rotateSpeed, 7.5, 'l');
                    strafe(0.2, 9, 'l');
                    strafe(0.2, 1.5, 'r');
                    drive(driveSpeed, -21);
                    depositMarker();
                    strafe(strafeSpeed, 16, 'r');
                    strafe(strafeSpeed, 15, 'l');
                    drive(driveSpeed, 19);
                    strafe(0.2, 6, 'l');
                    drive(driveSpeed, 21);
                    lowerCup();
                    sleep(30000);
                    break;
                case RIGHT:
                    telemetry.addLine("going to the right");
                    unLatch(1, 13200);
                    strafe(strafeSpeed, 4, 'l');
                    drive(0.1,1);
                    retractClimb();
                    rotate(rotateSpeed, 5.5, 'r');
                    drive(driveSpeed, 19);
                    drive(driveSpeed, -7.5);
                    rotate(rotateSpeed, 9.5, 'r');
                    drive(driveSpeed, -30);
                    rotate(rotateSpeed, 9, 'l');
                    strafe(0.2, 9, 'l');
                    strafe(0.2, 1.5, 'r');
                    drive(driveSpeed, -25);
                    depositMarker();
                    drive(driveSpeed, 10);
                    strafe(strafeSpeed, 10, 'r');
                    strafe(strafeSpeed, 9, 'l');
                    drive(driveSpeed, 16);
                    strafe(0.2, 8, 'l');
                    drive(driveSpeed, 15);
                    lowerCup();
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

    private void drive(double speed, double inches) {
        int newLFtarget;
        int newRFtarget;
        int newLBtarget;
        int newRBtarget;

        if (opModeIsActive()) {

            // Determine new target position
            newLFtarget = map.motorLF.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            newRFtarget = map.motorRF.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
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

            if(direction == 'l'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newLBtarget = map.motorLB.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRBtarget = map.motorRB.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
                map.motorLF.setTargetPosition(newLFtarget);
                map.motorRF.setTargetPosition(newRFtarget);
                map.motorLB.setTargetPosition(newLBtarget);
                map.motorRB.setTargetPosition(newRBtarget);

            }else if(direction == 'r'){
                // Determine new target position
                newLFtarget = map.motorLF.getCurrentPosition() + (int)(inches * COUNTS_PER_INCH);
                newRFtarget = map.motorRF.getCurrentPosition() - (int)(inches * COUNTS_PER_INCH);
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
            map.servoMarker.setPosition(0.4);
            sleep(500);
            map.servoMarker.setPosition(0);
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