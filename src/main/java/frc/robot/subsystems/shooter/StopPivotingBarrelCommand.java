package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;

public class StopPivotingBarrelCommand extends InstantCommand {
    public StopPivotingBarrelCommand() {
        super(RobotContainer.shooter::stopPivotingBarrel, RobotContainer.shooter);
    }
}
