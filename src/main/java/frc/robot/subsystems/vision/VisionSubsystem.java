package frc.robot.subsystems.vision;

import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class VisionSubsystem extends SubsystemBase {
    private final PhotonCamera camera = new PhotonCamera(Constants.Vision.CAMERA_NAME);
    private double angleToAprilCode = 0;
    private double distToAprilCode = 0;
    private boolean tracking = false;

    public void disenableTracker() {
        this.tracking = !this.tracking;
        System.out.println(this.tracking);
    }

    public boolean isTracking() {
        return tracking;
    }

    public double getAngleToAprilCode() {
        return angleToAprilCode;
    }

    public double getDistToAprilCode() {
        return distToAprilCode;
    }

    public double getAdjustedDistToAprilCode() {
        return distToAprilCode - Constants.Vision.BACKUP_DIST;
    }

    public void processAprilTags() {
        PhotonPipelineResult results = camera.getLatestResult();

        if (results.hasTargets()) {
            for (PhotonTrackedTarget target : results.getTargets()) {
                if (target.getFiducialId() == 22) {
                    Transform3d cameraToTarget = target.getBestCameraToTarget();

                    if (cameraToTarget != null) {
                        double x = cameraToTarget.getX();
                        System.out.println(x);
                        double y = cameraToTarget.getY();

                        double newAngle = -1 * Math.toDegrees(Math.atan2(y, x));
                        //if (Math.abs(newAngle - this.angleToAprilCode) > Constants.Vision.ANGLE_DEADBAND * 100)
                            this.angleToAprilCode = newAngle;
                            this.distToAprilCode = -1 * Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
                        return;
                    }
                }
            }
        }

        this.angleToAprilCode = 0;
        this.distToAprilCode = 0;
    }
}
