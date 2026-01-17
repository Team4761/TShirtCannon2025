package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.InstantCommand;

public class PivotBarrelCommand extends InstantCommand {
    public PivotBarrelCommand(ShooterSubsystem shooter, double degrees) {
        super(() -> shooter.pivotBarrelDegrees(degrees), shooter);
    }
}
