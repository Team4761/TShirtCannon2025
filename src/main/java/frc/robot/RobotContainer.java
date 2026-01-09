package frc.robot;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import swervelib.SwerveDrive;

public class RobotContainer {
  // Subsystems
  private final XboxController controller;
  private final SlewRateLimiter xlimiter;
  private final SlewRateLimiter ylimiter;
  public final SwerveSubsystem swerve;
  public final ShooterSubsystem shooter;

  // Internal
  private int currentModule = 0;
  private final CommandScheduler scheduler = CommandScheduler.getInstance();

  public RobotContainer() {
    controller = new XboxController(0);
    xlimiter = new SlewRateLimiter(10);
    ylimiter = new SlewRateLimiter(10);
    swerve = new SwerveSubsystem();
    shooter = new ShooterSubsystem();
  }

  public void teleopInit() {
    int multiplier = 2;
    scheduler.schedule(swerve.driveCommand(() -> xlimiter.calculate(controller.getLeftY() * multiplier), () -> ylimiter.calculate(controller.getLeftX() * multiplier), () -> controller.getRightX()));
  }

  public void teleopPeriodic() {
      if (swerve != null) {
          if (controller.getXButtonPressed()) {
              scheduler.schedule(swerve.reZeroCommand());
          }
      }
      if (shooter != null) {
          if (controller.getAButton()) {
              scheduler.schedule(shooter.setBarrelSpeed(0.2));
          } else {
              scheduler.schedule(shooter.setBarrelSpeed(0.0));
          }
      }
  }

  /**
   * Must be called from testPeriodic() method in Robot.java
  */
  public void testPeriodic() {
    // Motor selection
    if (controller.getLeftBumperButton()) {
        currentModule = (currentModule - 1) % 4;
    }
    if (controller.getRightBumperButton()) {
        currentModule = (currentModule + 1) % 4;
    }

    if (swerve != null) {
        // TODO: Spin single motor
        // TODO: Display current module stats on smart dashboard
    }
  }

  public SwerveSubsystem getSwerve() {
    return swerve;
  }

  public ShooterSubsystem getShooter() {
    return shooter;
  }
}