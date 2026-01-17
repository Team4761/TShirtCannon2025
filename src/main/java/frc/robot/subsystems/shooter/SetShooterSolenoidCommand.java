package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetShooterSolenoidCommand extends InstantCommand {
    public SetShooterSolenoidCommand(ShooterSubsystem shooter, boolean state) {
        super(() -> shooter.setSolenoidState(state), shooter);
    }
}
