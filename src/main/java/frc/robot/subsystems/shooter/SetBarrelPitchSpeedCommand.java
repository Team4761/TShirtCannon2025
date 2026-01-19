package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetBarrelPitchSpeedCommand extends InstantCommand {
    public SetBarrelPitchSpeedCommand(ShooterSubsystem shooter, double speed) {
        super(() -> shooter.setBarrelPitchSpeed(speed), shooter);
    }
}
