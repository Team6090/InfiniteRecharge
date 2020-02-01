package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

public class ColorSensor extends CommandBase {
  private final I2C.Port i2cPort = I2C.Port.kOnboard;
  private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
  private final ColorMatch colorMatcher = new ColorMatch();
  String colorString;

  /** Adds color values defined in Constants class to the color matcher. */
  public void addColors() {
    colorMatcher.addColorMatch(Const.Color.BLUE_TARGET);
    colorMatcher.addColorMatch(Const.Color.GREEN_TARGET);
    colorMatcher.addColorMatch(Const.Color.RED_TARGET);
    colorMatcher.addColorMatch(Const.Color.YELLOW_TARGET);
  }

  /**
   * As defined in addColors(), checks predefined color values against what is
   * being read from the sensor.
   */
  public String checkColor() {
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);
    if (match.color == Const.Color.BLUE_TARGET) {
      colorString = "Blue";
    } else if (match.color == Const.Color.RED_TARGET) {
      colorString = "Red";
    } else if (match.color == Const.Color.GREEN_TARGET) {
      colorString = "Green";
    } else if (match.color == Const.Color.YELLOW_TARGET) {
      colorString = "Yellow";
    } else {
      colorString = "Unknown";
    }
    return colorString;
  }

  /** Ouput RGB values to the Smart Dashboard. */
  public void colorToSmartDashboard() {
    SmartDashboard.putNumber("ColorSensor/Red", colorSensor.getRed());
    SmartDashboard.putNumber("ColorSensor/Green", colorSensor.getGreen());
    SmartDashboard.putNumber("ColorSensor/Blue", colorSensor.getBlue());
    SmartDashboard.putString("ColorSensor/Color", colorString);
  }
}