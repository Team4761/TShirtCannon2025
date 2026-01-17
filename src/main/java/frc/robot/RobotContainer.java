package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.shooter.PivotBarrelCommand;
import frc.robot.subsystems.shooter.ShootCommand;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.shooter.StopPivotingBarrelCommand;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import frc.robot.subsystems.vision.VisionSubsystem;
import swervelib.SwerveDrive;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class RobotContainer {
  // Subsystems
  private final CommandXboxController controller;

  private final SlewRateLimiter xlimiter;
  private final SlewRateLimiter ylimiter;
  private static final SwerveSubsystem swerve = new SwerveSubsystem();

  private static final ShooterSubsystem shooter = new ShooterSubsystem();

  private static final VisionSubsystem vision = new VisionSubsystem();
  private static final PhotonCamera camera = new PhotonCamera(Constants.Vision.CAMERA_NAME);
  private double angleToAprilCode = 0;

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
            () -> xlimiter.calculate(MathUtil.applyDeadband(controller.getLeftY(), 0.08) * multiplier),
            () -> ylimiter.calculate(MathUtil.applyDeadband(controller.getLeftX(), 0.08) * multiplier),
            () -> MathUtil.applyDeadband(controller.getRightX() + this.angleToAprilCode / 50, 0.08)
        )
    );
  }

  public void processAprilTags() {
    PhotonPipelineResult results = camera.getLatestResult();

    if (results.hasTargets()) {
        for (PhotonTrackedTarget target : results.getTargets()) {
            if (target.getFiducialId() == 22) {
                Transform3d cameraToTarget = target.getBestCameraToTarget();

                if (cameraToTarget != null) {
                    // Extract the translation (X, Y, Z) from the pose
                    double x = cameraToTarget.getX(); // Forward/backward distance
                    double y = cameraToTarget.getY(); // Left/right distance
                    double z = cameraToTarget.getZ(); // Up/down distance

                    this.angleToAprilCode = -1 * Math.atan(y/x) * 100;
                    System.out.println(this.angleToAprilCode);
                }
            }
        }
    }
  }

  private void configBindings() {
    if (swerve != null) {
      // Reset 'forwards' direction of robot when in operator relative mode
      controller.x().onTrue(swerve.reZeroCommand());
    }

    if (shooter != null) {
      // Reload left or right
      controller.rightTrigger().onTrue(new PivotBarrelCommand(shooter, 45));
      controller.leftTrigger().onTrue(new PivotBarrelCommand(shooter, -45));

      // Slightly adjust left or right
      controller.leftBumper().onTrue(new PivotBarrelCommand(shooter, -5));
      controller.rightBumper().onTrue(new PivotBarrelCommand(shooter, 5));

      // Fire the cannon
      controller.a().onTrue(new ShootCommand(shooter));
    }
  }

  public static ShooterSubsystem shooter() { return shooter; }
  public static SwerveSubsystem swerve() { return swerve; }
  public static VisionSubsystem visionSubsystem() { return vision; }
}