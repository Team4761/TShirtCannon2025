package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class StopPivotingBarrelCommand extends InstantCommand {
    public StopPivotingBarrelCommand(ShooterSubsystem shooter) {
        super(shooter::stopPivotingBarrel, shooter);
    }
}
