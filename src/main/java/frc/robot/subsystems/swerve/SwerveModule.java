// Much of the code is heavily inspired by https://github.com/wpilibsuite/allwpilib/blob/main/wpilibjExamples/src/main/java/edu/wpi/first/wpilibj/examples/swervebot/SwerveModule.java
package frc.robot.subsystems.swerve;

import com.revrobotics.spark.SparkMax;

// import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import frc.robot.Constants;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import com.revrobotics.spark.SparkLowLevel.MotorType;

/**
 * This represents a single swerve module.
 * This consists of a motor for steering/turning the wheel and another for driving the wheel.
 * The module is controlled using PID and Feed Forward while trying to get to a setpoint.
 */
public class SwerveModule {

    private static final double TALON_UNITS_TO_METERS = 2 * Math.PI * Constants.WHEEL_RADIUS / Constants.SWERVE_MOVING_ENCODER_RESOLUTION / Constants.SWERVE_MOVING_GEAR_RATIO;
    // private static final double CANCODER_UNITS_TO_RADIANS = 2 * Math.PI / Constants.SWERVE_TURNING_ENCODER_RESOLUTION / Constants.SWERVE_TURNING_GEAR_RATIO;
    // private static final double CANCODER_UNITS_TO_RADIANS = 2 * Math.PI / (150/7);
    // private static final double CANCODER_UNITS_TO_RADIANS = 5 / 1.044 / 0.75;
    private static final double CANCODER_UNITS_TO_RADIANS = 2.0 * Math.PI;

    // These are both motor controllers (that connect to the CAN bus)
    private TalonFX driveMotor;
    private SparkMax turnMotor;

    private Rotation2d offset;

    // These encoders get the position (rotation) of the motor, building up over time.
    // This means it is totally possible to get 800 radians as the rotation if it was run for long enough.
    private CANcoder turnEncoder;


    // This is the PID controller for the module.
    // kP = proportional: This changes the speed based on how far away the motor is from its desired position.
    // kI = integral: This changes the speed based on how long the program has been running for (highly recommended to keep this at 0)
    // kD = derivative: This changes the speed based on the current speed (no need to get faster if you're already going fast).
    private final ProfiledPIDController drivePIDController = new ProfiledPIDController(
        4.5, 
        0, 
        0,
        new TrapezoidProfile.Constraints(10000, (5 * Math.PI)));

    // A ProfiledPIDController is the same as above but also includes a max speed and max acceleration.
    private final ProfiledPIDController turningPIDController = new ProfiledPIDController(
        1.5,
        0,
        0,
        new TrapezoidProfile.Constraints(Constants.MAX_ANGULAR_VELOCITY, Constants.MAX_ANGULAR_ACCELERATION)
    );


    // Feed forward literally predicts the future and determines a MINIMUM speed to maintain the current position.
    // Typically this isn't needed, so the values (ks and kv) are set to 0 for now (Nov 5, 2024).
    private final SimpleMotorFeedforward driveFeedforward = new SimpleMotorFeedforward(0, 0);
    private final SimpleMotorFeedforward turnFeedforward = new SimpleMotorFeedforward(0.1, 0.1);

    /**
     * This represents a single swerve module.
     * @param turnMotorPort This is the port for the motor that rotates the wheel's direction. CAN port.
     * @param driveMotorPort This is the port driving the wheel. CAN port.
     * @param encoderPort This is the port for the CANCoder. CAN port.
     * @param offset The offset required to make the motor point forwards.
     */
    public SwerveModule(int turnMotorPort, int driveMotorPort, int encoderPort, Rotation2d offset) {
        driveMotor = new TalonFX(driveMotorPort);
        turnMotor = new SparkMax(turnMotorPort, MotorType.kBrushless);

        turnEncoder = new CANcoder(encoderPort);
        this.offset = offset;

        // This makes it so that +180 degrees is the same as -180 degrees as far as the wheel rotation is concerned.
        // This prevents the wheel from snapping 180 degrees when it needs to rotate in the opposite direction.
        // This also allows the program to automatically optimize the shortest rotation needed to move in the correct direction.
        turningPIDController.enableContinuousInput(-Math.PI, Math.PI);
    }


    public Rotation2d getTurnPosition() {
        return new Rotation2d(turnEncoder.getPosition().getValueAsDouble() * CANCODER_UNITS_TO_RADIANS).minus(offset); 
    }

    public Rotation2d getTurnVelocity() {
        return new Rotation2d(turnEncoder.getVelocity().getValueAsDouble() * CANCODER_UNITS_TO_RADIANS);
    }

    public double getDrivePosition() {
        return driveMotor.getPosition().getValueAsDouble() * TALON_UNITS_TO_METERS;
    }

    public double getDriveVelocity() {
        return driveMotor.getVelocity().getValueAsDouble() * TALON_UNITS_TO_METERS;
    }



    /**
     * Gets the current state of the entire module in a form that WPILib can understand.
     * This includes the speed of the wheel and the current rotation.
     * @return The current state of the module.
     */
    public SwerveModuleState getState() {
        return new SwerveModuleState(
            getDriveVelocity(), 
            getTurnPosition()
        );
    }


    /**
     * Returns the current position of the module in a form that WPILib can understand.
     * This includes the current distance travelled by the motor and its rotation.
     * @return The current position of the module.
     */
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(
            getDrivePosition(), 
            getTurnPosition()
        );
    }


    /**
     * Gets the current rotation of the module based on the turnMotor.
     */
    public Rotation2d getRotation() {
        return getTurnPosition();
    }


    /**
     * Sets the desired state (setpoint) of the module.
     * @param desiredState Desired state with speed (meters per second) and angle (Rotation2d).
     */
    public void setDesiredState(SwerveModuleState desiredState) {
        Rotation2d encoderRotation = getTurnPosition();

        // Optimize the reference state to avoid spinning further than 90 degrees.
        // This will find the optimal path to get to the desired rotation.
        desiredState = SwerveModuleState.optimize(desiredState, encoderRotation);

        // Scale speed by cosine of angle error. This scales down movement perpendicular to the desired
        // direction of travel that can occur when modules change directions. This results in smoother
        // driving.
        desiredState.speedMetersPerSecond = desiredState.speedMetersPerSecond * Math.cos(desiredState.angle.getRadians() - encoderRotation.getRadians());

        // Calculate the drive output using the drive PID controller.
        final double driveOutput = drivePIDController.calculate(getDriveVelocity(), desiredState.speedMetersPerSecond);
        final double driveFF = driveFeedforward.calculate(desiredState.speedMetersPerSecond);

        // Calculate the turning motor output using the turning PID controller.
        final double turnOutput = turningPIDController.calculate(getTurnPosition().getRadians(), desiredState.angle.getRadians());
        final double turnFF = turnFeedforward.calculate(turningPIDController.getSetpoint().velocity);

        driveMotor.setVoltage(driveOutput + driveFF);
        turnMotor.setVoltage(-(turnOutput + turnFF));
    }


    /**
     * Sets the motor speed. FOR DEBUGGING PURPOSES ONLY.
     * @param speed A number between -1 and 1 where 1 represents 100% speed.
     */
    public void setTurnSpeed(double speed) {
        turnMotor.set(speed);
    }

    /**
     * Sets the motor speed. FOR DEBUGGING PURPOSES ONLY.
     * @param speed A number between -1 and 1 where 1 represents 100% speed.
     */
    public void setDriveSpeed(double speed) {
        driveMotor.set(speed);
    }
}
