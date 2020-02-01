/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.jlimelight.Limelight.CameraMode;
import net.bancino.robotics.jlimelight.Limelight.LedState;
import net.bancino.robotics.jlimelight.Limelight.StreamMode;

public class AlignWithTarget extends CommandBase {

    Limelight limelight;

    public AlignWithTarget(Limelight limelight) {
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
        limelight.setLedMode(LedState.FORCE_ON);
        limelight.setCameraMode(CameraMode.VISION);
        limelight.setStreamingMode(StreamMode.STANDARD);
    }

    @Override
    public void execute() {
        double[] camTran = limelight.getCamTran();
        SmartDashboard.putNumberArray("Limelight/CamTran: ", camTran);
    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
