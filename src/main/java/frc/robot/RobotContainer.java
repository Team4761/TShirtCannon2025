package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.shooter.PitchBarrelDegreesCommand;
import frc.robot.subsystems.shooter.ShootCommand;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.shooter.RollBarrelDegreesCommand;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import frc.robot.subsystems.vision.DisenableTrackerCommand;
import frc.robot.subsystems.vision.VisionSubsystem;

public class RobotContainer {
    // Subsystems
    private final CommandXboxController controller;

    private final SlewRateLimiter xlimiter;
    private final SlewRateLimiter ylimiter;
    private static final SwerveSubsystem swerve = new SwerveSubsystem();

    private static final ShooterSubsystem shooter = new ShooterSubsystem();

    private static final VisionSubsystem vision = new VisionSubsystem();

    public RobotContainer() {
        controller = new CommandXboxController(Constants.CONTROLLER_PORT);
        xlimiter = new SlewRateLimiter(10);
        ylimiter = new SlewRateLimiter(10);

        configBindings();
        configDefaultCmds();
    }

    public void configDefaultCmds() {
        int multiplier = 2;
        swerve.setDefaultCommand(
                swerve.driveCommand(
                        () -> xlimiter.calculate(
                            MathUtil.applyDeadband(controller.getLeftY(), 0.08) 
                            * multiplier 
                            + ((vision.isTracking() ? 1 : 0) 
                                * (vision.getAdjustedDistToAprilCode() / Math.abs(vision.getAdjustedDistToAprilCode()))
                                * Constants.Vision.FOLLOW_SPEED)),
                        () -> ylimiter.calculate(MathUtil.applyDeadband(controller.getLeftX(), 0.08) * multiplier),
                        () -> MathUtil.applyDeadband(
                            controller.getRightX() 
                            + ((vision.isTracking() ? 1 : 0) 
                                * vision.getAngleToAprilCode()
                                * Constants.Vision.ANGLE_CONVERSION_FACTOR), 0.08)
                )
        );
    }

    private void configBindings() {
        if (swerve != null) {
            // Reset 'forwards' direction of robot when in operator relative mode
            controller.x().onTrue(swerve.reZeroCommand());
        }

        if (shooter != null) {
            // Reload left or right
            controller.rightTrigger().onTrue(new RollBarrelDegreesCommand(shooter, 45));
            controller.leftTrigger().onTrue(new RollBarrelDegreesCommand(shooter, -45));

            // Slightly adjust left or right
            controller.leftBumper().onTrue(new RollBarrelDegreesCommand(shooter, -5));
            controller.rightBumper().onTrue(new RollBarrelDegreesCommand(shooter, 5));

            // Aim cannon up/down
            controller.povUp().onTrue(new PitchBarrelDegreesCommand(shooter, 5));
            controller.povDown().onTrue(new PitchBarrelDegreesCommand(shooter, 0));

            // Fire the cannon
            controller.a().onTrue(new ShootCommand(shooter));
        }

        if (vision != null) {
            // Tracking
            controller.b().onTrue(new DisenableTrackerCommand(vision));
        }
    }

    public static ShooterSubsystem shooter() { return shooter; }
    public static SwerveSubsystem swerve() { return swerve; }
    public static VisionSubsystem visionSubsystem() { return vision; }
}
