package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.RobotContainer;

public class SetBarrelSpeedCommand extends InstantCommand {
    public SetBarrelSpeedCommand(double speed) {
        super(() -> RobotContainer.shooter.setBarrelSpeed(speed), RobotContainer.shooter);
    }
}
