package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class Constants {
    
    // Controller
    public static final int CONTROLLER_PORT = 0;    // Port on the Driverstation


    // Swerve
    class Modules {
        class FrontLeft {
            public static final int TURN = 6;
            public static final int DRIVE = 10;
            public static final int CAN = 1;
        }

        class FrontRight {
            public static final int TURN = 5;
            public static final int DRIVE = 8;
            public static final int CAN = 4;
        }

        class BackLeft {
            public static final int TURN = 7;
            public static final int DRIVE = 9;
            public static final int CAN = 3;
        }

        class BackRight {
            public static final int TURN = 30;
            public static final int DRIVE = 4;
            public static final int CAN = 2;
        }
    }

    public static final double WHEEL_RADIUS = 0.04995d;             // WIP in meters
    public static final int SWERVE_TURNING_ENCODER_RESOLUTION = 4096;  // WIP Number of ticks it takes to turn the wheel (swerve) completely.
    // public static final int SWERVE_MOVING_ENCODER_RESOLUTION = 16384;  // WIP Number of ticks it takes the driving wheel to make a full rotation.
    public static final int SWERVE_TURNING_GEAR_RATIO = 1;             // WIP The gear ratio to divide by when converting the encoder ticks to distance.
    public static final int SWERVE_MOVING_GEAR_RATIO = 1;              // WIP The gear ratio to divide by when converting the encoder ticks to distance.

    public static final double MAX_ANGULAR_VELOCITY = Units.degreesToRadians(180);          // WIP Radians per second.
    public static final double MAX_ANGULAR_ACCELERATION = Units.degreesToRadians(360);      // WIP Radians per second squared.
    public static final double MAX_DRIVE_SPEED = 0.4;     // In percents where 1.0 = 100%

    public static final Rotation2d GYRO_OFFSET = new Rotation2d(0.0);  // The number inside is in degrees and must be measured.

    public static final boolean FIELD_RELATIVE = true;    // DETERMINES IF IT IS FIELD OR ROBOT ORIENTED.

    // Shooter
    public static final int SOLENOID_PORT = 0;              // WIP PWM port I believe.
    public static final int SHOOTER_TURN_MOTOR_PORT = 14;    // CAN port.
    public static final int SHOOTER_PIVOT_MOTOR_PORT = 13;   // CAN port.
    public static final int SHOOTER_BARREL_SWITCH_PORT = 0; // WIP DIO port.

    public static final int SHOOTER_BARREL_TURN_MOTOR_RESOLUTION = 1;       // The number of ticks it takes to turn the barrel rotation motor one full time.
    public static final int SHOOTER_BARREL_GEAR_RATIO = 36;                 // WIP The gear ratio to divide by when converting the encoder units to radians
    public static final int SHOOTER_PIVOT_MOTOR_RESOLUTION = 4096;          // The number of ticks it takes to pivot the barrel fully around a full time (hopefully never happens...)
    public static final int SHOOTER_PIVOT_GEAR_RATIO = 75;                  // WIP The gear ratio to divide by when converting the encoder units to radians.

    public static final double SHOOTER_BARREL_TURN_SPEED = .2d;        // Speed to turn the barrel at when rotating between positions, between -1 and 1
    public static final double SHOOTER_BARREL_SPACING_ANGLE = 45;      // Turning angle to get from one barrel to another, in degrees

    public static final int SHOOTER_SOLENOID_DURATION_MS = 200; // How long to keep the solenoid out for when shooting, in milliseconds
}