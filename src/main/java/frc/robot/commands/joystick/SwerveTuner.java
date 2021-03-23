// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.joystick;

import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.geometry.SwerveVector;
import net.bancino.robotics.swerveio.command.SwerveDriveTeleopCommand;

import java.io.PrintStream;

import edu.wpi.first.wpilibj.XboxController;

/**
 * This class was used to tune the angle PID controller. It is currently not in
 * use but serves as documentation.
 */
public class SwerveTuner extends SwerveDriveTeleopCommand {

  private XboxController xbox;
  private PrintStream csv;

  /** Creates a new SwerveTuner. */
  public SwerveTuner(SwerveDrive swerve, SwerveDriveTeleopCommand inherit, XboxController xbox) {
    super(swerve, inherit);
    this.xbox = xbox;
  }

  public void initialize(SwerveDrive swerve) {
    try {
      this.csv = new PrintStream("/home/lvuser/pid.csv");
      csv.println("Slot,Setpoint,Feedback");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute(SwerveDrive swerve, SwerveVector joystickVector) {
    double angleSetpoint = xbox.getYButton() ? 45 : 0;
    swerve.drive(joystickVector, angleSetpoint);
    csv.printf("%d,%.3f,%.3f\n", swerve.getAnglePID().getActiveProfile() * 100, angleSetpoint,
        swerve.getGyro().getAngle());
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
