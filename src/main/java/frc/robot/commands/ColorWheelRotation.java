package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Elevator;
import com.revrobotics.ColorSensorV3;


public class ColorWheelRotation extends CommandBase {

    Elevator elevator;
    ColorSensorV3 colorSensor;
    public Elevator.WheelColor currentColor, previousColor;
    public int revCount;
    public boolean rotationComplete;
    private static final double elevatorColorChanges = RobotContainer.config().getDouble("elevatorColorChanges");
    private static final double colorWheelSpeed = RobotContainer.config().getDouble("colorWheelSpeed");

    public ColorWheelRotation(Elevator elevator) {
        this.colorSensor = elevator.getColorSensor();
        this.elevator = elevator;
        addRequirements(elevator);
    }
        public void initialize(){
            revCount = 0;
            rotationComplete = false;
        }

        public void execute(){
            currentColor = elevator.toWheelColor(colorSensor.getColor());
            if (previousColor != currentColor){
                revCount++;
                previousColor = elevator.toWheelColor(colorSensor.getColor());
                previousColor = currentColor;
            }
            /* Ask Bancino if this is correct inplace of "Const.Elevator.NUMBER_OF_COLOR_CHANGES" */
            if (revCount >= Const.Elevator.elevatorColorChanges) {
                /* Reset everything to the starting configuration for the next run. */
                elevator.setWheelSpeed(0);
                revCount = 0;
                previousColor = null;
                rotationComplete = true;
            } else {
                elevator.setWheelSpeed(Const.Speed.colorWheelSpeed);
            }
        }
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return rotationComplete;
    }
}