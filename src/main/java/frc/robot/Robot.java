// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj.XboxController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.SparkBase.ControlType;

/**
 * INTERACTIVE MOTOR REVOLUTION TEST CODE
 *
 * This code lets you test motors interactively using an Xbox controller.
 *
 * CONTROLLER BUTTONS:
 * - D-Pad Left/Right: Cycle through different motors
 * - A Button: Spin selected motor 1 revolution
 * - B Button: Stop motor immediately
 *
 * DASHBOARD:
 * - Shows current motor ID and name
 * - Shows motor position and status
 *
 * MOTOR TEST ORDER:
 * 1. Back Right Drive (ID 4)
 * 2. Back Right Turn (ID 30)
 * 3. Back Left Drive (ID 7)
 * 4. Back Left Turn (ID 9)
 * 5. Front Left Drive (ID 10)
 * 6. Front Left Turn (ID 6)
 * 7. Front Right Drive (ID 8)
 * 8. Front Right Turn (ID 5)
 */
public class Robot extends TimedRobot {
  public static double periodSeconds = 0.0; // Track time between Periodic() calls
  public static RobotMap map = new RobotMap(); // Initialize RobotMap to manage subsystems

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  // Motor IDs and names for testing
  private static final int[] MOTOR_IDS = {4, 30, 7, 9, 10, 6, 8, 5};
  private static final String[] MOTOR_NAMES = {
    "Back Right Drive",
    "Back Right Turn",
    "Back Left Drive",
    "Back Left Turn",
    "Front Left Drive",
    "Front Left Turn",
    "Front Right Drive",
    "Front Right Turn"
  };

  // Current motor selection
  private int currentMotorIndex = 0;

  // Test motor components
  private SparkMax testMotor;
  private RelativeEncoder testEncoder;
  private SparkClosedLoopController testPIDController;

  // Controller
  private RobotController controller;

  // Button tracking (for edge detection)
  private boolean lastAButton = false;
  private boolean lastBButton = false;
  private boolean lastDPadLeft = false;
  private boolean lastDPadRight = false;

  // Test state
  private boolean testRunning = false;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    // Initialize controller
    controller = new RobotController(Constants.CONTROLLER_PORT); // Initialize controller object

    System.out.println("=".repeat(60));
    System.out.println("INTERACTIVE MOTOR TEST INITIALIZED");
    System.out.println("=".repeat(60));
    System.out.println("D-Pad Left/Right: Select motor");
    System.out.println("A Button: Spin motor 1 revolution");
    System.out.println("B Button: Stop motor");
    System.out.println("=".repeat(60));

    // Initialize first motor
    // initializeMotor(currentMotorIndex);
  }

  /**
   * Initialize or switch to a new motor for testing
   */
  private void initializeMotor(int index) {
    // Stop and clean up old motor if it exists
    if (testMotor != null) {
      testMotor.set(0);
      testMotor.close();
    }

    int motorID = MOTOR_IDS[index];
    String motorName = MOTOR_NAMES[index];

    System.out.println("\n" + "=".repeat(60));
    System.out.println("SWITCHING TO: " + motorName + " (CAN ID " + motorID + ")");
    System.out.println("=".repeat(60));

    // Create new motor
    testMotor = new SparkMax(motorID, MotorType.kBrushless);

    // Create configuration object (REVLib 2025 style)
    SparkMaxConfig config = new SparkMaxConfig();

    // Configure PID for position control (slot 0)
    config.closedLoop
        .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
        .pid(0.1, 0.0, 0.0)  // P, I, D
        .outputRange(-0.3, 0.3);  // Max 30% speed

    // Apply configuration to motor
    testMotor.configure(config, SparkMax.ResetMode.kResetSafeParameters, SparkMax.PersistMode.kNoPersistParameters);

    // Get built-in encoder (NEO motors have integrated encoders)
    testEncoder = testMotor.getEncoder();

    // Get closed loop controller for position control
    testPIDController = testMotor.getClosedLoopController();

    // Reset encoder
    testEncoder.setPosition(0);

    // Update dashboard
    SmartDashboard.putNumber("Motor ID", motorID);
    SmartDashboard.putString("Motor Name", motorName);
    SmartDashboard.putNumber("Motor Index", index + 1);
    SmartDashboard.putNumber("Total Motors", MOTOR_IDS.length);

    System.out.println("Motor initialized. Ready to test.");
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {
    System.out.println("\n" + "=".repeat(60));
    System.out.println("TELEOP ENABLED - INTERACTIVE MOTOR TEST READY");
    System.out.println("=".repeat(60));
    System.out.println("Current Motor: " + MOTOR_NAMES[currentMotorIndex]);
    System.out.println("  D-Pad Left/Right: Change motor");
    System.out.println("  A Button: Spin 1 revolution");
    System.out.println("  B Button: Stop");
    System.out.println("=".repeat(60));

    // Make sure motor is stopped
    // testMotor.set(0);
    testRunning = false;
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    CommandScheduler.getInstance().run();
    controller.teleopPeriodic();
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    if (testMotor != null) {
      testMotor.set(0);
    }
    testRunning = false;
    System.out.println("\nRobot disabled - motor stopped.");
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}