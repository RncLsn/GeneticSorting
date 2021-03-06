package genetic_sorting.batch_gp;

import java.util.Properties;

/**
 * @author Alessandro Ronca
 */
class BatchProperties extends Properties {

    public BatchProperties () {
        setIntProperty("initialPopulation", 1000);
        setIntProperty("maxTreeDepth", 6);
        setIntProperty("testCases", 15);
        setIntProperty("maxListLength", 30);
        setIntProperty("maxListValue", 60);
        setDoubleProperty("mutationPct", 0.1);
        setDoubleProperty("crossoverPct", 0.9);
        setDoubleProperty("crossoverExpansion", 1.8);
        setIntProperty("generations", 50);

        setIntProperty("runs", 20);

        setIntProperty("generalTestCases", 1000);
        setIntProperty("generalMaxListLength", 100);
        setIntProperty("generalMaxListValue", 200);
    }

    Object setIntProperty (String key, int value) {
        return setProperty(key, Integer.toString(value));
    }

    Object setDoubleProperty (String key, double value) {
        return setProperty(key, Double.toString(value));
    }

    public int getIntProperty (String key) {
        return Integer.parseInt(getProperty(key));
    }

    public double getDoubleProperty (String key) {
        return Double.parseDouble(getProperty(key));
    }

    @Override
    public String toString () {
        return "BatchProperties" + super.toString();
    }
}
