package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;

public class SetShooterSolenoidCommand extends InstantCommand {
    public SetShooterSolenoidCommand(boolean state) {
        super(() -> RobotContainer.shooter.setSolenoidState(state), RobotContainer.shooter);
    }
}
