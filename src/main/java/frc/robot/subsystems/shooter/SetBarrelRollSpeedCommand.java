package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class SetBarrelRollSpeedCommand extends InstantCommand {
    public SetBarrelRollSpeedCommand(ShooterSubsystem shooter, double speed) {
        super(() -> shooter.setBarrelRollSpeed(speed), shooter);
    }
}
