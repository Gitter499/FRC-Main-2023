package frc.robot;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.GridAlign;
import frc.robot.subsystems.drivetrain.Drivetrain;
import frc.robot.util.XboxController;
import frc.robot.util.XboxController.Button;
import frc.robot.util.limelight.Limelight;

public class RobotContainer {

  /* Controllers */
  private final XboxController driverController = new XboxController(0);
  private final XboxController manipulatorController = new XboxController(1);

  /* Subsystems */
  private final Drivetrain drivetrain = new Drivetrain(driverController);

  private final SendableChooser<Command> autoChooser = new SendableChooser<>();

  public RobotContainer() {

    configureButtonBindings();

    Limelight limelight = new Limelight();
    BooleanSupplier isAprilTagVisible = () -> limelight.tV == 1;

    this.driverController.whenPressed(Button.Y, new ConditionalCommand(new InstantCommand((() -> {
      SmartDashboard.putBoolean("visible", true);

      SmartDashboard.putNumber("dX", limelight.camTran.getX());
      SmartDashboard.putNumber("dZ", limelight.camTran.getZ());

      SmartDashboard.putNumber("AprilTag ID", limelight.aprilTagID); 

    })), new InstantCommand(() -> {
      SmartDashboard.putBoolean("visible", false);
    }), isAprilTagVisible));

    // this.driverController.whenPressed(Button.Y,
    // new ConditionalCommand(new GridAlign(drivetrain), new
    // SequentialCommandGroup(new InstantCommand(() -> {
    // driverController.setRumble(true);
    // }), new WaitCommand(Constants.GridAlign.kRumbleTime), new InstantCommand(()
    // -> {
    // driverController.setRumble(false);
    // })), isAprilTagVisible));

    this.drivetrain.setDefaultCommand(new RunCommand(
        () -> this.drivetrain.arcadeDrive(driverController.getAxisValue(XboxController.Axis.LEFT_Y),
            driverController.getAxisValue(XboxController.Axis.RIGHT_X)),
        drivetrain));

    /* Add autos here */
    // autoChooser.addOption("name", auto);
  }

  private void configureButtonBindings() {
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}