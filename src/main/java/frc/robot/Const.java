package frc.robot;

import com.revrobotics.ColorMatch;

/**
 * The constants class stores all of our robot wiring constants.
 */
public class Const {
    public static class CAN {
        public static final int FRONT_RIGHT_DRIVE_MOTOR = 5; /* Module 1 */
        public static final int FRONT_LEFT_DRIVE_MOTOR = 6; /* Module 2 */
        public static final int REAR_LEFT_DRIVE_MOTOR = 7; /* Module 3 */
        public static final int REAR_RIGHT_DRIVE_MOTOR = 8; /* Module 4 */

        public static final int FRONT_RIGHT_PIVOT_MOTOR = 1; /* Module 1 */
        public static final int FRONT_LEFT_PIVOT_MOTOR = 2; /* Module 2 */
        public static final int REAR_LEFT_PIVOT_MOTOR = 3; /* Module 3 */
        public static final int REAR_RIGHT_PIVOT_MOTOR = 4; /* Module 4 */
    }
    public static class Color {
        public static final edu.wpi.first.wpilibj.util.Color BLUE_TARGET = ColorMatch.makeColor(0.143, 0.427, 0.429);
        public static final edu.wpi.first.wpilibj.util.Color GREEN_TARGET = ColorMatch.makeColor(0.197, 0.561, 0.240);
        public static final edu.wpi.first.wpilibj.util.Color RED_TARGET = ColorMatch.makeColor(0.561, 0.232, 0.114);
        public static final edu.wpi.first.wpilibj.util.Color YELLOW_TARGET = ColorMatch.makeColor(0.361, 0.524, 0.113);
    }
}