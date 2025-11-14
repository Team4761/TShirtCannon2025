package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class RobotController extends XboxController {
    private int currentModule = 0;
    private CommandScheduler commandScheduler = CommandScheduler.getInstance();

    /**
     * Creates a new RobotController object that talks with an xbox controller connected to the computer
     * @param port The USB port the controller is connected to
     */
    public RobotController(int port) {
        super(port);
    }

    public void teleopPeriodic() {
        if (Robot.map.swerve != null) {
            commandScheduler.schedule(Robot.map.swerve.driveCommand(() -> -getLeftY(), () -> -getLeftX(), () -> -getRightX()));

            if (getXButtonPressed()) {
                Robot.map.swerve.reZero();
            }
        }
    }

    /**
     * Must be called from testPeriodic() method in Robot.java
     */
    public void testPeriodic() {
        // Motor selection
        if (getLeftBumperButton()) {
            currentModule = (currentModule - 1) % 4;
        }
        if (getRightBumperButton()) {
            currentModule = (currentModule + 1) % 4;
        }

        if (Robot.map.swerve != null) {
            // Robot.map.swerve.testMotor(currentModule, getLeftX(), getLeftY());
            // TODO: Display current module stats on smart dashboard
        }
    }
}
