/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Intake;

public class IntakeWithJoystick extends CommandBase {

  private Intake intake;
  private XboxController xbox;
  private XboxController.Button intakeButton;

  /**
   * Creates a new IntakeWithJoystick.
   */
  public IntakeWithJoystick(Intake intake, XboxController xbox, XboxController.Button intakeButton) {
    addRequirements(intake);
    this.intake = intake;
    this.xbox = xbox;
    this.intakeButton = intakeButton;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (xbox.getRawButton(intakeButton.value)) {
      intake.run();
    } else {
      intake.stop();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
