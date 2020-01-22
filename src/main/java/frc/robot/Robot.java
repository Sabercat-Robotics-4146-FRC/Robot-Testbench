/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  TalonSRX leftFront;
  TalonSRX leftBack;
  TalonSRX rightFront;
  TalonSRX rightBack;

  Joystick joy;

  // Variables
  double targetVelocity_UnitsPer100ms;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    leftFront = new TalonSRX(5);
    leftBack = new TalonSRX(4);
    rightFront = new TalonSRX(1);
    rightBack = new TalonSRX(3);

    leftFront.configFactoryDefault();
    leftBack.configFactoryDefault();
    rightFront.configFactoryDefault();
    rightBack.configFactoryDefault();

    leftFront.follow(leftBack);
    rightFront.follow(leftBack);
    rightBack.follow(leftBack);

    
    leftFront.setInverted(false);
    leftBack.setInverted(true);
    rightFront.setInverted(false);
    rightBack.setInverted(false);

    leftBack.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    leftBack.setSensorPhase(true);
    // kF: 1023 represents output value to Talon at 100%, 7200 represents Velocity
    // units at 100% output
    leftBack.config_kF(0, 1023.0 / 7200.0);
    leftBack.config_kP(0, .25);
    leftBack.config_kI(0, 0.001);
    leftBack.config_kD(0, 20);

    joy = new Joystick(0);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {

  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // left.set(ControlMode.PercentOutput, -joy.getY());

    /**
     * Convert 500 RPM to units / 100ms. 4096 Units/Rev * 500 RPM / 600 100ms/min in
     * either direction: velocity setpoint is in units/100ms
     */

    if (joy.getRawButton(1)) { // Velocity Control (A)
      targetVelocity_UnitsPer100ms = (-joy.getY()) * 500.0 * 4096 / 600;
      /* 500 RPM in either direction */
      leftBack.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
    } else if (joy.getRawButton(2)) { // Percent Control (B)
      leftBack.set(ControlMode.PercentOutput, -joy.getY());
    } else {
      leftBack.set(ControlMode.PercentOutput, 0);
    }

    // Info
    SmartDashboard.putNumber("Motor Output Percentage", leftBack.getMotorOutputPercent() * 100);
    SmartDashboard.putNumber("Motor Velocity", leftBack.getSelectedSensorVelocity(0));
    // SmartDashboard.putNumber("Motor Amps", leftBack.G);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
