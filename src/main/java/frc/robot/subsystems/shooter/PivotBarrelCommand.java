package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;

public class PivotBarrelCommand extends InstantCommand {
    public PivotBarrelCommand(double degrees) {
        super(() -> RobotContainer.shooter.pivotBarrelDegrees(degrees), RobotContainer.shooter);
    }
}
