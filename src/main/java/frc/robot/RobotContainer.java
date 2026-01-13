package frc.robot;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.shooter.PivotBarrelCommand;
import frc.robot.subsystems.shooter.ShootCommand;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.shooter.StopPivotingBarrelCommand;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import swervelib.SwerveDrive;

public class RobotContainer {
  // Subsystems
  private final XboxController controller;
  private final SlewRateLimiter xlimiter;
  private final SlewRateLimiter ylimiter;
  public static final SwerveSubsystem swerve = new SwerveSubsystem();
  public static final ShooterSubsystem shooter = new ShooterSubsystem();

  // Internal
  private int currentModule = 0;
  private final CommandScheduler scheduler = CommandScheduler.getInstance();

  public RobotContainer() {
    controller = new XboxController(0);
    xlimiter = new SlewRateLimiter(10);
    ylimiter = new SlewRateLimiter(10);
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
          if (controller.getAButtonPressed()) {
              System.out.println("A button pressed");
              scheduler.schedule(new PivotBarrelCommand(45));
          } else if (controller.getLeftBumperButtonPressed()) {
            scheduler.schedule(new PivotBarrelCommand(15));
          } else if (controller.getRightBumperButtonPressed()) {
              System.out.println("Right bumper pressed");
              scheduler.schedule(new ShootCommand());
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
}