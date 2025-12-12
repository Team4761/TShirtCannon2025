package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.swerve.SwerveSubsystem;
import swervelib.SwerveDrive;

public class RobotContainer {
  // Subsystems
  private final XboxController controller;
  private final SwerveSubsystem swerve;
  private final ShooterSubsystem shooter;

  // Internal
  private int currentModule = 0;
  private final CommandScheduler scheduler = CommandScheduler.getInstance();

  public RobotContainer() {
    controller = new XboxController(0);
    swerve = new SwerveSubsystem();
    shooter = new ShooterSubsystem();
  }

  public void teleopInit() {
    scheduler.schedule(swerve.driveCommand(() -> controller.getLeftY(), () -> controller.getLeftX(), () -> controller.getRightX()));
  }

  public void teleopPeriodic() {
      if (swerve != null) {
          if (controller.getXButtonPressed()) {
              scheduler.schedule(swerve.reZeroCommand());
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