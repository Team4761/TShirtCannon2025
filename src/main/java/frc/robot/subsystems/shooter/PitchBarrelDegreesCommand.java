package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class PitchBarrelDegreesCommand extends InstantCommand {
    public PitchBarrelDegreesCommand(ShooterSubsystem shooter, double degrees) {
        super(() -> shooter.pitchBarrelDegrees(degrees), shooter);
    }
}