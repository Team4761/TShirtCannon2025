package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/**
 * Opens the solenoid briefly to fire, then closes it.
 */
public class ShootCommand extends SequentialCommandGroup {
    public ShootCommand(ShooterSubsystem shooter) {
        addCommands(
            new SetShooterSolenoidCommand(shooter, true),
            new WaitCommand(0.2),
            new SetShooterSolenoidCommand(shooter, false)
        );
    }
}
