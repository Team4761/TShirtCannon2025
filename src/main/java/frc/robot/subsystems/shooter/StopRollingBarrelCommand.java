package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class StopRollingBarrelCommand extends InstantCommand {
    public StopRollingBarrelCommand(ShooterSubsystem shooter) {
        super(shooter::stopRollingBarrel, shooter);
    }
}
