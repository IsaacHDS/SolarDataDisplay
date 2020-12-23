package net.isaacparker.solardatadisplay;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.StringReader;

public class VictronData {

    public String PID; //PID
    public String FW; //FW
    public String SerialNumber; //SER#
    public double BatteryVoltage; //V
    public double BatteryCurrent; //I
    public double PanelVoltage; //VPV
    public double PanelWatts; //PPV
    public int ChargeState; //CS
    public int ErrorCode; //ERR
    public boolean LoadOutputState; //LOAD
    public double LoadCurrent; // IL
    public double YieldTotal; //H19
    public double YieldToday; //H20
    public double MaxWattToday; //H21
    public double YieldYesterday; //H22
    public double MaxWattYesterday; //H23
    public int DayNumber; //HSDS

    public VictronData(){

    }

    public VictronData loadData(String data){
        try{
            BufferedReader bufReader = new BufferedReader(new StringReader(data));
            String line = null;
            while((line = bufReader.readLine()) != null)
            {
                String[] splitLine = line.split("\t", -1);
                String label = splitLine[0];
                String value = splitLine[1];
                switch(label){
                    case "PID":
                        this.PID = value;
                        break;
                    case "FW":
                        this.FW = value;
                        break;
                    case "SER#":
                        this.SerialNumber = value;
                        break;
                    case "V":
                        this.BatteryVoltage = Double.parseDouble(value) / 1000;
                        break;
                    case "I":
                        this.BatteryCurrent = Double.parseDouble(value) / 1000;
                        break;
                    case "VPV":
                        this.PanelVoltage = Double.parseDouble(value) / 1000;
                        break;
                    case "PPV":
                        this.PanelWatts = Double.parseDouble(value);
                        break;
                    case "CS":
                        this.ChargeState = Integer.parseInt(value);
                        break;
                    case "ERR":
                        this.ErrorCode = Integer.parseInt(value);
                    case "LOAD":
                        if(value == "ON")
                            this.LoadOutputState = true;
                        else
                            this.LoadOutputState = false;
                        break;
                    case "IL":
                        this.LoadCurrent = Double.parseDouble(value) / 1000;
                        break;
                    case "H19":
                        this.YieldTotal = Double.parseDouble(value) * 0.01;
                        break;
                    case "H20":
                        this.YieldToday = Double.parseDouble(value) * 0.01;
                        break;
                    case "H21":
                        this.MaxWattToday = Double.parseDouble(value);
                        break;
                    case "H22":
                        this.YieldYesterday = Double.parseDouble(value) * 0.01;
                        break;
                    case "H23":
                        this.MaxWattYesterday = Double.parseDouble(value);
                        break;
                    case "HSDS":
                        this.DayNumber = Integer.parseInt(value);
                        break;
                }
                return this;
            }
        }catch (Exception e){
            Log.i("SDD", data);
            Log.i("SDD", e.toString());
            return this;
        }
        return this;
    }

    public String convertErrorCode(Integer errorCode){
        String result =  "Unknown error code";
        switch (errorCode){
            case 0:
                result = "No error";
                break;
            case 2:
                result = "Battery voltage too high";
                break;
            case 17:
                result = "Charger temperature too high";
                break;
            case 18:
                result = "Charger over current";
                break;
            case 19:
                result = "Charger current reversed";
                break;
            case 20:
                result = "Bulk time limit exceeded";
                break;
            case 21:
                result = "Current sensor issue (sensor bias/sensor broken)";
                break;
            case 26:
                result = "Terminals overheated";
                break;
            case 33:
                result = "Input voltage too high (solar panel)";
                break;
            case 34:
                result = "Input current too high (solar panel)";
                break;
            case 38:
                result = "Input shutdown (due to excessive battery voltage)";
                break;
            case 116:
                result = "Factory calibration data lost";
                break;
            case 117:
                result = "Invalid/incompatible firmware";
                break;
            case 119:
                result = "User settings invalid";
                break;
        }
        return result;
    }

    public String convertState(int state){
        String result = "Unknown State";
        switch (state){
            case 0:
                result = "Off";
                break;
            case 2:
                result = "Fault";
                break;
            case 3:
                result = "Bulk";
                break;
            case 4:
                result = "Absorption";
                break;
            case 5:
                result = "Float";
                break;
        }
        return result;
    }

    public String toString(){
        return "PID: " + PID + "\n" +
                "FW: " + FW + "\n" +
                "Serial Number: " + SerialNumber + "\n" +
                "BV: " + String.valueOf(BatteryVoltage) + "v\n" +
                "BC: " + String.valueOf(BatteryCurrent) + "amps\n" +
                "PV: " + String.valueOf(PanelVoltage) + "v\n" +
                "PW: " + String.valueOf(PanelWatts) + "W\n" +
                "Yield Today: " + String.valueOf(YieldToday) + "kWh\n" +
                "Max Watt Today: " + String.valueOf(MaxWattToday) + "W\n" +
                "Yield Yesterday: " + String.valueOf(YieldYesterday) + "kWh\n" +
                "Max Watt Yesterday: " + String.valueOf(MaxWattYesterday) + "W\n" +
                "Yield Total: " + String.valueOf(YieldTotal) + "kWh\n" +
                "State: " + convertState(ChargeState) + "\n" +
                "Error: " + convertErrorCode(ErrorCode) + "\n";
    }
}
