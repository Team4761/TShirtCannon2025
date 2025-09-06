package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.Constants;

/**
 * This will simply open up the solenoid and then close it after 0.2 seconds.
 */
public class ShootCommand extends Command {
    
    // Will store when the command ends
    private long endTime;

    @Override
    public void initialize() {
        if (Robot.map.shooter != null) {
            this.cancel();
        }
        else {
            // Open the air flow
            Robot.map.shooter.setSolenoid(true);
            endTime = System.currentTimeMillis() + Constants.SHOOTER_SOLENOID_DURATION_MS;
        }
    }

    @Override
    public void end(boolean interrupted) {
        // Close the air flow
        Robot.map.shooter.setSolenoid(false);
        System.out.println(interrupted ? "Solenoid interrupted and closed!" : "Solenoid closed!");
    }

    @Override
    public boolean isFinished() {
        if (endTime <= System.currentTimeMillis())
            return true;
        return false;
    }
}
