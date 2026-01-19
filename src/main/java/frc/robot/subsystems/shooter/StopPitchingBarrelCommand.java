package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class StopPitchingBarrelCommand extends InstantCommand {
    public StopPitchingBarrelCommand(ShooterSubsystem shooter) {
        super(shooter::stopPitchingBarrel, shooter);
    }
}
