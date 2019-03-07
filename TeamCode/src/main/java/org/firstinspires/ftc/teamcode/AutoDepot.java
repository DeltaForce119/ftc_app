package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.vision.MasterVision;
import org.firstinspires.ftc.teamcode.vision.SampleRandomizedPositions;

@Autonomous
public class AutoDepot extends LinearOpMode {

    // DEFINE ROBOT'S HARDWARE
    Hardware map = new Hardware();
    ElapsedTime runtime = new ElapsedTime();
    static final double COUNTS_PER_MOTOR_REV = 1120;    // AndyMark ticks
    static final double DRIVE_GEAR_REDUCTION = 2.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;    // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    double driveSpeed = 1;
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
                    drive(0.1,1);
                    strafe(strafeSpeed, 4, 'l');
                    rotate(rotateSpeed, 3, 'l');
                    retractClimb();
                    drive(driveSpeed, 21);
                    rotate(rotateSpeed, 8.5, 'r');
                    //moveSlide(-0.8, -1300);
                    //depositMarker();
                    //moveSlide(0.8, 0);
                    drive(driveSpeed, -3);
                    rotate(rotateSpeed, 16, 'r');
                    drive(driveSpeed, -15);
                    rotate(rotateSpeed,15, 'r');
                    strafe(strafeSpeed, 3, 'r');
                    drive(driveSpeed, 18);
                    lowerCup();
                    sleep(30000);
                    break;
                case CENTER:
                    telemetry.addLine("going straight");
                    unLatch(-1, -22500);
                    drive(0.1,1);
                    strafe(strafeSpeed, 4, 'l');
                    retractClimb();
                    rotate(rotateSpeed, 2, 'r');
                    drive(driveSpeed, 23);
                    rotate(0.5, 2, 'l');
                    drive(driveSpeed, 3);
                    depositMarker();
                    drive(driveSpeed, -15);
                    rotate(rotateSpeed, 14.5, 'r');
                    drive(driveSpeed, -27);
                    rotate(rotateSpeed, 7, 'r');
                    drive(0.8, -6);
                    rotate(rotateSpeed, 15, 'r');
                    strafe(0.5, 7, 'r');
                    drive(0.5, 2);
                    lowerCup();
                    sleep(30000);
                    break;
                case RIGHT:
                    telemetry.addLine("going to the right");
                    unLatch(-1, -22500);
                    strafe(strafeSpeed, 4, 'l');
                    drive(0.1,1);
                    retractClimb();
                    rotate(rotateSpeed, 7.5, 'r');
                    drive(driveSpeed, 20);
                    drive(driveSpeed, -6);
                    rotate(rotateSpeed, 8.5, 'r');
                    drive(driveSpeed, -5);
                    rotate(rotateSpeed, 14, 'l');
                    moveSlide(-0.8, -1300);
                    depositMarker();
                    moveSlide(0.8, 0);
                    rotate(rotateSpeed, 13.5, 'r');
                    drive(driveSpeed, -27);
                    rotate(rotateSpeed, 7, 'r');
                    drive(0.8, -8);
                    rotate(rotateSpeed, 15, 'r');
                    drive(driveSpeed, 3.5);
                    lowerCup();
                    sleep(30000);
                    break;
                case UNKNOWN:
                    telemetry.addLine("staying put");
                    drive(0.1,1);
                    strafe(strafeSpeed, 4, 'l');
                    rotate(rotateSpeed, 5, 'l');
                    //retractClimb();
                    drive(driveSpeed, 21);
                    rotate(rotateSpeed, 10, 'r');
                    //moveSlide(-0.8, -1300);
                    //depositMarker();
                    //moveSlide(0.8, 0);
                    drive(driveSpeed, -3);
                    rotate(rotateSpeed, 20, 'r');
                    drive(driveSpeed, -17);
                    rotate(rotateSpeed,15, 'r');
                    strafe(strafeSpeed, 8, 'r');
                    drive(driveSpeed, 10);
                    //lowerCup();
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

    private void moveSlide(double speed, int ticks){
        map.motorSlide.setTargetPosition(ticks);
        map.motorSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        map.motorSlide.setPower(Math.abs(speed));
        while(map.motorSlide.isBusy()){
            telemetry.addData("Status: ", "Moving slide mechanism");
            telemetry.update();
        }
        map.motorSlide.setPower(0);
        map.motorSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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