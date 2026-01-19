package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class RollBarrelDegreesCommand extends InstantCommand {
    public RollBarrelDegreesCommand(ShooterSubsystem shooter, double degrees) {
        super(() -> shooter.rollBarrelDegrees(degrees), shooter);
    }
}