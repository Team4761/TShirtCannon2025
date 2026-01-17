package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetBarrelSpeedCommand extends InstantCommand {
    public SetBarrelSpeedCommand(ShooterSubsystem shooter, double speed) {
        super(() -> shooter.setBarrelSpeed(speed), shooter);
    }
}
