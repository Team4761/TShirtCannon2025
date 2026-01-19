package frc.robot.subsystems.shooter;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.SmartMotor;

/**
 * Has a motor (NEO) for turning the barrel and a solenoid to shoot.
 * There is also a switch that tells the code when the barrel has rotated.
 */
public class ShooterSubsystem extends SubsystemBase {
    public static boolean activelyRotating = false;
    public static final boolean HAS_SWITCH = false;
    
    private Solenoid solenoid;
    private SmartMotor rollMotor;
    private SmartMotor pitchMotor;
    private DigitalInput barrelSwitch;
    
    public ShooterSubsystem() {
        solenoid = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.SOLENOID_PORT);
        rollMotor = SmartMotor.Builder.newInstance()
                        .port(Constants.SHOOTER_TURN_MOTOR_PORT)
                        .motorType(MotorType.kBrushless)
                        .PID(0.1, 0, 0)
                        .outputRange(-0.7, 0.7)
                        .angleLimits(-1, -1)
                        .build();
        pitchMotor = SmartMotor.Builder.newInstance()
                        .port(Constants.SHOOTER_PIVOT_MOTOR_PORT)
                        .motorType(MotorType.kBrushless)
                        .PID(0.1, 0, 0)
                        .outputRange(-0.7, 0.7)
                        .angleLimits(-1, -1)
                        .build();

        if (HAS_SWITCH)
            this.barrelSwitch = new DigitalInput(Constants.SHOOTER_BARREL_SWITCH_PORT);
    }


    /**
     * Currently just used for logging telemetry data.
     */
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Barrel Pitch", pitchMotor.getAngle());
        SmartDashboard.putNumber("Barrel Rotation", rollMotor.getAngle());
    }


    /**
     * Opens and closes the solenoid.
     * @param state True to open the solenoid. False to close it.
     */
    public void setSolenoidState(boolean state) {
        solenoid.set(state);
    }


    /**
     * Sets the speed of the rotation of the barrel.
     * +speed = counter clock wise = rotate to the left
     * @param speed A number between -1 and 1 where 1 represents 100% speed where + = counterclock wise.
     */
    public void setBarrelRollSpeed(double speed) {
        rollMotor.setSpeed(speed);
    }

    /**
     * Sets the speed of the pivot of the barrel up and down.
     * +speed = up
     * @param speed A number between -1 and 1 where 1 represents 100% speed where + = up.
     */
    public void setBarrelPitchSpeed(double speed) {
        pitchMotor.setSpeed(speed);
    }


    /**
     * Rotates the barrel in roll by the specified number of degrees.
     * @param degrees A number between -360 and 360 in degrees
     */
    public void rollBarrelDegrees(double degrees) {
        this.rollMotor.turn(degrees);
    }

    /**
     * Rotates the barrel in pitch by the specified number of degrees.
     * @param degrees A number between -360 and 360 in degrees.
     */
    public void pitchBarrelDegrees(double degrees) {
        this.pitchMotor.turn(degrees);
    }

    /**
     * Stops barrel roll movement when using closed-loop turning.
     */
    public void stopRollingBarrel() {
        this.rollMotor.stopTurning();
    }

    /**
     * Stops barrel pitch movement when using closed-loop turning.
     */
    public void stopPitchingBarrel() {
        this.pitchMotor.stopTurning();
    }


    /**
     * By using the encoder units of the NEO, this gets the current rotation of the barrel.
     * @return The current rotation of the barrel after converting from encoder units to rotation where + = counterclock wise
     */
    public Rotation2d getBarrelRollRotation() {
        return new Rotation2d(Math.toRadians(rollMotor.getAngle()));
    }


    /**
     * By using the encoder units of the NEO, this gets the current pivot of the barrel.
     * @return The current pivot of the barrel after converting from encoder units to rotation where + ~> upwards.
     */
    public Rotation2d getBarrelPitchRotation() {
        return new Rotation2d(Math.toRadians(pitchMotor.getAngle()));
    }


    /**
     * Gets whether or not the barrel is in place based upon the switch.
     * This does not account for if the switch is null, since the code that calls this method should handle it.
     * @return True if the barrel is lined up. False if it is not.
     */
    public boolean getSwitchState() {
        return this.barrelSwitch.get();
    }
}
