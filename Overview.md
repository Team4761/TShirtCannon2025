```
java/
└── frc/
    └── robot/
        ├── subsystems/
        │   ├── shooter/
        │   │   ├── RotateBarrelCommand.java
        │   │   ├── ShootAndRotateCommand.java
        │   │   ├── ShootCommand.java
        │   │   └── ShooterSubsystem.java
        │   └── swerve/
        │       ├── MoveCommand.java
        │       ├── SwerveModule.java
        │       └── SwerveSubsystem.java
        ├── Constants.java
        ├── Main.java
        ├── RobocketsController.java
        ├── Robot.java
        └── RobotMap.java
```

## 1. Entry point/Main class (Main.java)
- Entry point: `Main.java`
- Creates new robot class instance, starts the instance of the class

## 2. Main robot class (Robot.java)
- Initializes `RobotMap.java`, which stores a constructor that initializes each of our subsystems
- Creates a controller instance using `RobocketsController.java`
- Creates and sets up options for tool for a `SendableChooser`, a tool for presenting a selection of options to the SmartDashboard
- robotInit() method
	- Creates options and the dropdown selector
- robotPeriodic() method
	- Sets the value of periodSeconds to the time period between calls to Periodic() functions
- autonomousInit() method
	- Logs which auto is selected
- autonomousPeriodic() method
	- Runs all scheduled commands by our subsystems
- teleopPeriodic() method
	- Runs all scheduled commands by our subsystems
	- Runs controller teleopPeriodic() method for each 'tick' for controller updates/methods

## 3. Controller events handler (RobotController.java)
- Creates instance of command scheduler for adding events to the queue
- Defines limiter factors for joystick axes. This prevents any sudden movements, e.g. a sudden flip to the left by -5, instead limiting to -3 to smooth movements
- Defines a RobotController constructor, which takes in a port parameter to connect to as the controller
- teleopPeriodic() method defines keybinds
	- A button: Tilt up at .15 speed
	- Y button: Tilt cannon <u><b>down</b></u> at .15 speed
	- Neither A nor Y: Stop tilting cannon; kill speed

	- X button: ReZero direction of robot, relative to its current direction

	- B button: Turn barrel CW 
	- Left bumper: Shoot; turn barrel CCW
	- Right bumper: Shoot; turn barrel CW

	- Dpad up: Tilt cannon <u><b>down</b></u> at .15 speed
	- Dpad down: Tilt up at .15 speed
	- Dpad right: Rotate barrels CW
	- Dpad left: Rotate barrels CCW

	- Left joystick x: X change for robot
	- Left joystick y: Y change for robot
	- Right joystick x: Directional change for robot (angle)
- testPeriod methods used to define motor test functions
	- Left bumper: \[WRAPS 0-5] change motor ID (current accessed motor) by -1
	- Right bumper: \[WRAPS 0-5] change motor ID (current accessed motor) by +1
	- X button: \[DOESN'T WRAP] increment motor ID by 1

## 4. Shooter subsystem
### a. RotateBarrelCommand.java
- Defines constructor functionality for switches deciding rotation clamps, and logic for CCW/CW
- Defines initialize method
	- Defines the command for initializing a barrel rotation in a certain direction
	- Defines artificial limits (rotationGoal) for the rotation
- Defines end method
	- Stops rotation
	- Pretty prints cause (interruption vs clean end)
- Defines functionality for switches controlling barrel rotation

### b. ShootAndRotateCommand.java
- Shoots barrel with solenoid
- Rotates barrel

### c. ShootCommand.java
- Defines constructor
	- Checks if shooter exists
	- Opens solenoid
	- Defines endtime for closing solenoid
- Defines end function which closes solenoid and pretty prints status
- Defines periodic check for a function to test if timeout wait for solenoid is finished

### d. ShooterSubsytem.java
- Defines constructor
	- Defines variable for solenoid
	- Defines variable for motor for turning barrel 
	- Defines variable for motor for tilting barrel
	- Sets encoder position
	- Initializes switch if it exists
- Defines periodic method
	- Pretty prints status of all 
- Provides methods for interacting with all the motors, and getting the current position (rotation) of each one

## 5. Swerve subsystem
### a. MoveCommand.java
- Defines constructor with
	- X change (in meters) +x is fwd
	- Y change (in meters) +y is left
	- Rotation vector (CCW)
- Initialize method which returns calculated target position
- Execute method
	- Calculates remaining transformation between current pos and goal pos
	- Drives swerve based on the signum function of the distance left vector multiplied by the max drive speed constant

### b. SwerveModule.java
- SparkMaxs for drive motor and turn motor as well as CANcoder
- PID Controller setup for drive and turning motors
- SwerveModule method, represents a single swerve module
	- Gets the SparkMax drive motor based on port
	- Gets the SparkMax turn motor based on port
	- gets turn encoder (CANcoder)
	- Applies config to PID to allow optimal paths + avoid snapping at 180deg
- Provides methods for interacting with both motors, getting their position and velocity
- Defines default methods required for WPI lib
- setDesiredState method for setting destination (goal) of module
	- Optimizes path
	- Scales speed by cosine of angle error
	- Calculates PID values, and in turn, the voltages required
- Defines setTurnSpeed and setDriveSpeed methods for debugging

### c. SwerveSubsytem.java
- Provides locational data about the Robot to itself
- Defines method for resetting position
- Pretty print method for data "periodic"
- Drive method for desired speeds and to move, based on field relativity
	- Set desired positions + states based on passed values
	- Pretty print data
- Define gyro getters for position and rotation
- Define debugging methods for getting SwerveModule instances of each module