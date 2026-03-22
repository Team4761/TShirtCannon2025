package frc.robot.subsystems.leds;
import static edu.wpi.first.units.Units.Percent;
import static edu.wpi.first.units.Units.Second;
import static edu.wpi.first.units.Units.Seconds;

import java.util.Map;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.LEDPattern.GradientType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import static frc.robot.subsystems.leds.ImageCompressor.loadImage;
import static frc.robot.subsystems.leds.ImageCompressor.compressImage;


@SuppressWarnings("unused")
public class LEDSubsystem extends SubsystemBase {
    public static AddressableLED leds;
    public static AddressableLEDBuffer buffer;

    //no, the type "double" is not a mistake
    //note: must make each false condition set it to a UNIQUE out of bounds spot
    public static double intakeTrue = 1.0;
    public static double shootTrue = 1.1;

    private LEDPattern previousPattern;
    private LEDPattern currentPattern;
        
    // We will have a 16x1 strip

    // IMPORTANT: If the LEDs are in GRB instead of RGB again, please infom me (Alex Maniscalco). I already have a fix for it
    // also ask/tell me about any other problems or conflicts this subsystem makes

    /** Available LED patterns:
       <p> displays an image from your pc (you have to set the file location) in 1d
     * <p> LED patterns that aren't finished:
     */
    
    public LEDSubsystem() {
        // Comment out patterns that aren't being used

        leds = new AddressableLED(Constants.leds.LEDS_PORT);
        buffer = new AddressableLEDBuffer(Constants.leds.LEDS_NUMBER_OF_LEDS);
        leds.setLength(Constants.leds.LEDS_NUMBER_OF_LEDS);
        leds.start();
        
        currentPattern = RobocketsLEDPatterns.OFF;
        previousPattern = RobocketsLEDPatterns.OFF;

    }
        
    public void periodic(){
        //displays a single unchanging image
        int[][][] testImageAsCompressed3dArrayComingToThearterMarch32_2111 = compressImage(loadImage("C:/Users/alex/Pictures/test.jpg", false), 1, 16);{
            for (int i = 0; i < testImageAsCompressed3dArrayComingToThearterMarch32_2111.length; i++)
            {
                for (int f = 0; f < testImageAsCompressed3dArrayComingToThearterMarch32_2111[i].length; f++)
                {
                    buffer.setRGB(f + (i * Constants.leds.LEDS_WIDTH), testImageAsCompressed3dArrayComingToThearterMarch32_2111[i][f][0], testImageAsCompressed3dArrayComingToThearterMarch32_2111[i][f][1], testImageAsCompressed3dArrayComingToThearterMarch32_2111[i][f][2]);
                }
            }
        }
        leds.setData(buffer);
    }


    public void setPattern(LEDPattern pattern) {
        previousPattern = currentPattern;
        currentPattern = pattern;
        pattern.applyTo(buffer);
        leds.setData(buffer);
    }


    /**
     * This gets the last pattern that was used. This starts out as RobocketsLEDPatterns.OFF
     * @return The previously used LED Pattern
     */
    public LEDPattern getPreviousPattern() {
        return previousPattern;
    }


    /**
     * This sets the LEDs to the "black" color (which turns them off)
     */
    public void stopLEDs() {
        LEDPattern off = LEDPattern.solid(Color.kBlack);
        off.applyTo(buffer);
        leds.setData(buffer);
    }
        


    // the command that aplies the pattern to the LEDs
    public Command runPattern(LEDPattern pattern) {
        return run(() -> setPattern(pattern));
    }
}