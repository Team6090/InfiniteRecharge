package frc.robot.subsystems;

import frc.robot.Const;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

/**
 * The Elevator subsystem controls the elevator mechanism
 *
 * This subsystem consists of the following components: 
 * - The elevator motor (1x Spark Max using internal NEO encoder.) 
 * - The adjustment wheel (1x Talon SRX/Victor SPX controller on either CAN or PWM)
 * - The color sensor for the color wheel
 *
 * This subsystem should provide the following functions: 
 * - Run a position loop on the elevator
 * - Run a speed loop on the elevator
 * - Run a speed loop on the adjustment wheel
 * - Run a position loop for the color wheel (both matching color and rotations)
 */
public class Elevator extends SubsystemBase {

    private final CANSparkMax elevatorMotor = new CANSparkMax(Const.CAN.ELEVATOR_MOTOR, MotorType.kBrushless);
    private final WPI_VictorSPX wheelMotor = new WPI_VictorSPX(Const.CAN.ELEVATOR_WHEEL);
    private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
    private final CANPIDController elevatorPID = new CANPIDController(elevatorMotor);

    private final Solenoid lockEnable = new Solenoid(pwmCanId, Const.Pneumatic.ELEVATOR_LOCK_ENABLE);
    private final Solenoid lockDisable = new Solenoid(pwmCanId, Const.Pneumatic.ELEVATOR_LOCK_DISABLE);
    
    private WheelColor targetColor, previousColor, currentColor;
    private int revCount = 0;

    private boolean locked = false;
    private static final double elevatorP = RobotContainer.config().getDouble("elevatorP");
    private static final double elevatorI = RobotContainer.config().getDouble("elevatorI");

    public static enum WheelColor {
        RED, GREEN, BLUE, YELLOW
    };

    public Elevator() {
        elevatorPID.setP(elevatorP);
        elevatorPID.setI(Const.PID.elevatorI);
        elevatorMotor.setIdleMode(IdleMode.kBrake);
        setLocked(false);
    }

    /**
     * Position loop control for the elevator.
     * 
     * @param position Encoder reference for elevator setpoint.
     */
    public void setElevatorPosition(double position) {
        elevatorPID.setReference(position, ControlType.kSmartMotion);
    }

    /**
     * Velocity loop control for the elevator.
     * 
     * @param speed Speed reference for elevator.
     */
    public void setElevatorSpeed(double speed) {
        elevatorMotor.set(speed);
    }

    /**
     * Returns the encoder position of the elevator motor.
     */
    public double getElevatorEncoder() {
        return elevatorMotor.getEncoder().getPosition();
    }

    /** Zeroes the elevator motors encoder to 0. */
    public void zeroElevatorEncoder() {
        elevatorMotor.getEncoder().setPosition(0);
    }

    /**
     * Sets the color wheels speed.
     * 
     * @param speed The speed, between -1 and 1, of the motor.
     */
    public void setWheelSpeed(double speed) {
        wheelMotor.set(speed);
    }

    /**
     * Rotates the wheel three times.
     * Returns true when the rotation is done.
     */
    public boolean rotateColorWheel() {
        /* This is the first run, set the starting color. */
         currentColor = toWheelColor(colorSensor.getColor());
        /* Get the current color to see if it's different. */
        //WheelColor WheelColor = toWheelColor(colorSensor.getColor());
        /** Counts each new color, meaning the cheese slices. */
        if (previousColor != currentColor) {
            revCount++;
        }
        /** If we have reached our rotation count, stop spinning */
        if (revCount >= Const.Elevator.NUMBER_OF_COLOR_CHANGES) {
            setWheelSpeed(0);
            /* Reset everything to the starting configuration for the next run. */
            revCount = 0;
            currentColor = null;
            previousColor = null;
            return true;
        } else {
            setWheelSpeed(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
            return false;
        }
    }

    /**
     * A convertor to feed color sensor data through to the enum WheelColor.
     * 
     * @param color Raw color output data from the Color Sensor.
     * @return The enum, RED, GREEN, BLUE, RED, to match that color.
     */
    public WheelColor toWheelColor(Color color) {
        switch (color.toString()) {
        case "Red":
            return WheelColor.RED;
        case "Green":
            return WheelColor.GREEN;
        case "Blue":
            return WheelColor.BLUE;
        case "Yellow":
            return WheelColor.YELLOW;
        default:
            return null;
        }
    }

    /**
     * Finds the color, offset by 2 counterclockwise, away from the given color.
     * @param color The color you're really looking to put under the sensor. Input
     * as a single character: R, G, B, Y.
     * @return The offset color, the one the robot will be reading. Returns as a
     * WheelColor.
     */
    public WheelColor toTargetColor(WheelColor color) {
        switch (color) {
        case RED:
            targetColor = WheelColor.BLUE;
        case GREEN:
            targetColor = WheelColor.YELLOW;
        case BLUE:
            targetColor = WheelColor.RED;
        case YELLOW:
            targetColor = WheelColor.GREEN;
        }
        return targetColor;
    }

    /**
     * Spins the wheelMotor until you're on you're targeted color.
     * @param color The desired color, usually the game specific message.
     */
    public boolean goToColor(WheelColor color) {
        if (toWheelColor(colorSensor.getColor()) != toTargetColor(color)) {
            wheelMotor.set(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
            return false;
        } else {
            wheelMotor.set(0);
            return true;
        }
    }

    /**
     * Finds the color from the color sensor.
     * @return The color read from the sensor.
     */
    public Color getColor() {
        return colorSensor.getColor();
    }

    /**
     * Returns true if the sensor is reading the specified color.
     * @return True or false, whichever happens to be accurate.
     */
    public boolean atColor(WheelColor color) {
        return toWheelColor(colorSensor.getColor()) == toTargetColor(color);
    }

    /**
     * Lock the elevator with the solenoid to keep it from falling in endgame.
     * @param locked Whether or not the elevator is locked, locked should be
     * the default configuration.
     */
    public void setLocked(boolean locked) {
        lockDisable.set(!locked);
        lockEnable.set(locked);
        this.locked = locked;
    }

    public boolean isLocked() {
        return locked;
    }

    public ColorSensorV3 getColorSensor() {
        return colorSensor;
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Subsystems/Elevator/Locked", isLocked());
        SmartDashboard.putNumber("Subsystems/Elevator/Speed", elevatorMotor.get());
        SmartDashboard.putNumber("Subsystems/Elevator/Position", elevatorMotor.getEncoder().getPosition());
        SmartDashboard.putNumber("Subsystems/Elevator/Wheel Speed", wheelMotor.get());
        SmartDashboard.putString("Subsystems/Elevator/Color Sensor", colorSensor.getColor().toString());
    }
}
