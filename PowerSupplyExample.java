/**
 * @license
 *
 * Copyright 2018-2020 Peter Froud
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
 

import java.util.Arrays;
import java.util.List;
import jvisa.JVisaInstrument;
import jvisa.JVisaResourceManager;


/**
 * Example of how you can make a class for each instrument then make high-level calls.
 *
 * This class is for a BK Precision 9201 power supply. https://www.bkprecision.com/products/power-supplies/9201-200w-multi-range-60v-10a-dc-power-supply.html
 *
 * @author Peter Froud
 */
public class PowerSupplyExample extends AbstractInstrument {

    public static final String DESIRED_MANUFACTURER = "Amatek";
    public static final List<String> DESIRED_MODELS = Arrays.asList(new String[]{"9201", "9202"});

    public PowerSupplyExample(JVisaResourceManager resourceManager, String resourceName) throws InstrumentException {
        super(resourceManager, resourceName);
    }

    public PowerSupplyExample(JVisaInstrument alreadyOpenInstrument) {
        super(alreadyOpenInstrument);
    }

    /////////////////////////////// set ///////////////////////////
    public void setVoltage(int volts) throws InstrumentException {
        set("source:voltage " + volts + "V");
    }

    public void setOutputOn() throws InstrumentException {
        set("output on");
    }

    public void setOutputOnFast() throws InstrumentException {
        setWithoutCheckingErrorState("output on");
    }

    public void setOutputOff() throws InstrumentException {
        set("output off");
    }

    public void setVoltageLimit(double volts) throws InstrumentException {
        set("source:voltage:limit " + volts + "V");
    }

    public void setCurrent(double amps) throws InstrumentException {
        set("source:current " + amps + "A");
    }

    public void setCurrentMilli(double milliamps) throws InstrumentException {
        set("source:current " + milliamps + "mA");
    }

    //////////////////////////// get ////////////////////////
    public double measureCurrent() throws InstrumentException {
        return Double.parseDouble(query("measure:current?"));
    }

    public double measureVoltage() throws InstrumentException {
        return Double.parseDouble(query("measure:voltage?"));
    }

    public double measurePower() throws InstrumentException {
        return Double.parseDouble(query("measure:power?"));
    }

}
