package frc.robot;

import java.net.URISyntaxException;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.Limelight;
import frc.robot.commands.GridAlign;
import frc.robot.commands.Rumble;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.subsystems.poseTracker.PoseTracker;
import frc.robot.util.Controller;
import frc.robot.util.InstantiatorCommand;
import frc.robot.util.limelight.LimelightAPI;

public class RobotContainer {
  /* Controllers */
  private final Controller driverController = new Controller(0);
  private final Controller manipulatorController = new Controller(1);

  /* Subsystems */
  private final Drivetrain drivetrain = new Drivetrain(driverController);
  private final PoseTracker poseTracker = new PoseTracker(drivetrain);

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {
    Controller.onPress(driverController.A, new InstantCommand(this.drivetrain::zeroHeading));

    Controller.onPress(driverController.Y, new ConditionalCommand(
      // on true, instantiate and schedule align command
      new InstantiatorCommand(() -> new GridAlign(drivetrain, poseTracker)),
      // on false rumble for 1 second
      new Rumble(driverController, Constants.GridAlign.kRumbleTime),
      // conditional upon a valid april tag
      LimelightAPI::validTargets
    ));

    Controller.onHold(driverController.X, new InstantCommand(() -> {
      LimelightAPI instance;
      try {
        instance = new LimelightAPI(true);

        //Pose2d pose = instance.getActualPose2d();


        // SmartDashboard.putNumber("ry botpose_targetspace", LimelightAPI.getPose("botpose_targetspace").getRotation().getAngle());
        // SmartDashboard.putNumber("ry botpose", LimelightAPI.getPose("botpose").getRotation().getAngle() * 180 / Math.PI);
        // SmartDashboard.putNumber("ry campose", LimelightAPI.getPose("campose").getRotation().getAngle());

        
        // SmartDashboard.putNumber("sreal tx", pose.getX());
        // SmartDashboard.putNumber("real tz", pose.getY());
        // SmartDashboard.putNumber("real ry", pose.getRotation().getDegrees());


        SmartDashboard.putNumber("ry letsgo yessirt", LimelightAPI.getPose("campose_targetspace").getRotation().getAngle() * 180 / Math.PI);

      } catch (URISyntaxException e) {
        // TODO Auto-generated catch block
        System.out.println("fuckkkkkk");
        e.printStackTrace();
      }
    }));
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}